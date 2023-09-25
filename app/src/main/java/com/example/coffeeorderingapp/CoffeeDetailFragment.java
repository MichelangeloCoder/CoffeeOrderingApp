package com.example.coffeeorderingapp;

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
import com.example.coffeeorderingapp.CoffeeDetailFragmentDirections;
import com.example.coffeeorderingapp.Model.coffeeModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Objects;


public class CoffeeDetailFragment extends Fragment {

    NavController navController;
    int quantity = 0;
    FirebaseFirestore firebaseFirestore;
    Button add, sub, order;
    TextView coffeename, description, quantityview, orderINFO;
    ImageView imageView;
    String coffeeid, name, coffeedescription, imageURl;
    int price = 0;


    int totalPrice = 0;


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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //View Casting is done below code..

        imageView = view.findViewById(R.id.CoffeeDetailImage);
        coffeename = view.findViewById(R.id.coffeenamedetail);
        description = view.findViewById(R.id.coffeedetaildetail);
        add = view.findViewById(R.id.incrementcoffee);
        sub = view.findViewById(R.id.decrementcoffee);
        quantityview = view.findViewById(R.id.quantityDetailNumber);
        firebaseFirestore = FirebaseFirestore.getInstance();
        navController = Navigation.findNavController(view);
        order = view.findViewById(R.id.ordercoffee);
        orderINFO = view.findViewById(R.id.orderINFO);

        name = CoffeeDetailFragmentArgs.fromBundle(getArguments()).getCoffeename();
        coffeeid = CoffeeDetailFragmentArgs.fromBundle(getArguments()).getId();
        imageURl = CoffeeDetailFragmentArgs.fromBundle(getArguments()).getImageurl();
        coffeedescription = CoffeeDetailFragmentArgs.fromBundle(getArguments()).getDescription();
        price = CoffeeDetailFragmentArgs.fromBundle(getArguments()).getPrice();

        Glide.with(view.getContext()).load(imageURl).into(imageView);
        coffeename.setText(name + " ₹" + String.valueOf(price));
        description.setText(coffeedescription);

        //fetching the recent quantity from firestore and displaying it..

        firebaseFirestore.collection("coffee").document(coffeeid).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                coffeeModel coffeeModel = value.toObject(coffeeModel.class);
                quantity = coffeeModel.getQuantity();
                quantityview.setText(String.valueOf(quantity));

                //Showing the total price in the quantity
                totalPrice = quantity * price;
                orderINFO.setText(String.valueOf("Total Price is " + totalPrice));

            }
        });

        add.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View view) {

                                       if (quantity == 10) {

                                           Toast.makeText(getContext(), "Can't Order More than 10", Toast.LENGTH_SHORT).show();
                                           quantityview.setText(String.valueOf(quantity));
                                       } else {

                                           quantity++; //Increment coffee quantity=quantity+1;
                                           quantityview.setText(String.valueOf(quantity));

                                           //Showing the total price in the quantity
                                           totalPrice = quantity * price;
                                           orderINFO.setText(String.valueOf("Total Price is " + totalPrice));

                                           if (quantity == 0){

                                               firebaseFirestore.collection("Cart").document(name).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                   @Override
                                                   public void onComplete(@NonNull Task<Void> task) {

                                                   }
                                               });
                                           }

                                           ////Updating quantites from the firebase Cart..
                                           firebaseFirestore.collection("coffee").document(coffeeid).update("quantity", quantity).addOnCompleteListener(new OnCompleteListener<Void>() {
                                               @Override
                                               public void onComplete(@NonNull Task<Void> task) {


                                               }
                                           });

                                       }

                                   }
                               }
        );

        //Subtract the quantity of the coffee..

        sub.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View view) {

                                       if (quantity == 0) {

                                           Toast.makeText(getContext(), "Nothing in Cart", Toast.LENGTH_SHORT).show();
                                           quantityview.setText(String.valueOf(quantity));
                                           quantity = 0;
                                           totalPrice = 0;


                                           //If the quantity is zero delete the collection documents
                                           firebaseFirestore.collection("Cart").document(name).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                               @Override
                                               public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                                   if (task.isSuccessful()){

                                                       task.getResult().getReference().delete();
                                                   }

                                               }
                                           });
                                       } else {

                                           quantity--; //Increment coffee quantity=quantity+1;
                                           quantityview.setText(String.valueOf(quantity));

                                           //Showing the total price in the quantity
                                           totalPrice = quantity * price;
                                           orderINFO.setText(String.valueOf("Total Price is " + totalPrice));

                                           //Updating quantites from the firebase Cart..
                                           firebaseFirestore.collection("coffee").document(coffeeid).update("quantity", quantity).addOnCompleteListener(new OnCompleteListener<Void>() {
                                               @Override
                                               public void onComplete(@NonNull Task<Void> task) {


                                               }
                                           });

                                           firebaseFirestore.collection("Cart").document(name).update("quantity", quantity).addOnCompleteListener(new OnCompleteListener<Void>() {
                                               @Override
                                               public void onComplete(@NonNull Task<Void> task) {

                                               }
                                           });

                                       }


                                   }
                               }
        );

        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (quantity == 0) {

                    navController.navigate(R.id.action_coffeeDetailFragment_to_allCoffeeListFragment);
                    Toast.makeText(getContext()," You didn't order coffee yet " + name, Toast.LENGTH_SHORT).show();

                } else {

                    AddToCart();

                    com.example.coffeeorderingapp.CoffeeDetailFragmentDirections.ActionCoffeeDetailFragmentToAllCoffeeListFragment
                                action = CoffeeDetailFragmentDirections.actionCoffeeDetailFragmentToAllCoffeeListFragment();

                        action.setQuantity(quantity);
                        navController.navigate(action);
                    Toast.makeText(getContext()," You've Ordered " + name, Toast.LENGTH_SHORT).show();
                }


            }
        });


    }

    private void AddToCart() {


            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("coffeename",name);
            hashMap.put("quantity",quantity);
            hashMap.put("totalprice",totalPrice);
            hashMap.put("coffeeid",coffeeid);
            hashMap.put("imageURL",imageURl);

            //Creating a new coffee collection for the cart in the firebase..

            firebaseFirestore.collection("Cart").document(name).set(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {


                }
            });

        }
    }