package com.example.coffeeorderingapp;

import static com.example.coffeeorderingapp.R.id.quantitydetailnumber;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.coffeeorderingapp.Model.CoffeeModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;


public class CoffeeDetailFragment extends Fragment {

    NavController navController;
    int quantity = 0;
    FirebaseFirestore firebaseFirestore;
    Button add,sub,order;
    TextView coffeename,description,quantityview, orderINFO;
    ImageView imageView;
    String coffeeid,name,coffeedescription,imageURL;
    int price=0;

    ProgressDialog pd;
    int totalPrice=0;




    public CoffeeDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_coffee_detail, container, false);
    }

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imageView = view.findViewById(R.id.CoffeeDetailImage);
        coffeename = view.findViewById(R.id.coffeenamedetail);
        description = view.findViewById(R.id.coffeedetaildetail);
        add = view.findViewById(R.id.Incrementcoffee);
        sub = view.findViewById(R.id.decrementcoffee);
        quantityview = view.findViewById(quantitydetailnumber);
        firebaseFirestore = FirebaseFirestore.getInstance();
        navController = Navigation.findNavController(view);
        order = view.findViewById(R.id.orderdetail);
        orderINFO = view.findViewById(R.id.orderINFO);

        pd = new ProgressDialog(getContext());

        name = CoffeeDetailFragmentArgs.fromBundle(getArguments()).getCoffeename();
        coffeeid = CoffeeDetailFragmentArgs.fromBundle(getArguments()).getId();
        imageURL= CoffeeDetailFragmentArgs.fromBundle(getArguments()).getImageURL();
        coffeedescription = CoffeeDetailFragmentArgs.fromBundle(getArguments()).getDescription();
        price = CoffeeDetailFragmentArgs.fromBundle(getArguments()).getPrice();

        Glide.with(view.getContext()).load(imageURL).into(imageView);
        coffeename.setText(name + " \u20B9" + String.valueOf(price));
        description.setText(coffeedescription);

        //fetching the recent quantity value in firestore and displaying it
        firebaseFirestore.collection("coffees").document(coffeeid).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot value, FirebaseFirestoreException error) {

                CoffeeModel coffeeModel = value.toObject(CoffeeModel.class);
                quantity = coffeeModel.getQuantity();
                quantityview.setText(String.valueOf(quantity));
                //showing the price
                totalPrice = quantity * price;
                orderINFO.setText(String.valueOf("Total Price is " + totalPrice));

            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(quantity == 5) {

                    Toast.makeText(getContext(), "Can't Order more than 5",Toast.LENGTH_SHORT).show();
                    quantityview.setText(String.valueOf(quantity));

                } else {

                    quantity++;//quantity = quantity+1

                    quantityview.setText(String.valueOf(quantity));

                    //Showing the price
                    totalPrice = quantity * price;
                    orderINFO.setText(String.valueOf("Total price is " + totalPrice));

                    //updating the quantity in firebase

                    firebaseFirestore.collection("coffees").document(coffeeid).update("quantity",quantity).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(Task<Void> task) {

                        }
                    });

                }

            }
        });

        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(quantity == 0){

                    Toast.makeText(getContext(),"Nothing is added in cart",Toast.LENGTH_SHORT).show();
                } else {

                    quantity--;
                    quantityview.setText(String.valueOf(quantity));

                    totalPrice = quantity * price;
                    orderINFO.setText(String.valueOf("Total Price is " + totalPrice));

                    //updating the quantity in firebase

                    firebaseFirestore.collection("coffees").document(coffeeid).update("quantity",quantity).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(Task<Void> task) {

                        }
                    });

                }



            }
        });

        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AddtoCart();
            }
        });







    }

    private void AddtoCart() {

        if (quantity == 0){

            navController.navigate(R.id.action_coffeeDetailFragment_to_allCoffeeListFragment);
            Toast.makeText(getContext(),"You did not order coffee" + name,Toast.LENGTH_SHORT).show();
        } else {

            HashMap<String,Object> hashMap = new HashMap<>();
            hashMap.put("coffeename",name);
            hashMap.put("quantity",quantity);
            hashMap.put("totalprice",totalPrice);
            hashMap.put("coffeeid",coffeeid);
            hashMap.put("description",coffeedescription);
            hashMap.put("imageURL",imageURL);

            firebaseFirestore.collection("Cart").document(name).set(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    pd.setMessage("Adding to Cart");
                    pd.show();

                    if(task.isSuccessful()){

                        Toast.makeText(getContext(),"Added to Cart",Toast.LENGTH_SHORT).show();

                        pd.dismiss();
                        CoffeeDetailFragmentDirections.ActionCoffeeDetailFragmentToAllCoffeeListFragment
                                action = CoffeeDetailFragmentDirections.actionCoffeeDetailFragmentToAllCoffeeListFragment();
                        action.setQuantity(quantity);
                        navController.navigate(action);
                    }

                }
            });
        }
    }
}