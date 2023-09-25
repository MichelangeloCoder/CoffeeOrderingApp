package com.example.coffeeorderingapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coffeeorderingapp.Adapter.CartAdapter;
import com.example.coffeeorderingapp.Model.CartModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class CoffeeCartFragment extends Fragment {

    CartAdapter mAdapter;
    RecyclerView recyclerView;
    FirebaseFirestore firestore;
    Button orderbutton;
    TextView orderSummary;
    NavController navController;

    List<CartModel> cartModelList = new ArrayList<>();
    int totalOrderCost = 0;
    List<Integer> saveTotalCost = new ArrayList<>();



    public CoffeeCartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_coffee_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        firestore = FirebaseFirestore.getInstance();
        mAdapter = new CartAdapter();
        orderbutton = view.findViewById(R.id.orderNow);

        orderSummary = view.findViewById(R.id.orderSummary);

        recyclerView = view.findViewById(R.id.cartRecView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        firestore.collection("Cart").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {

                    cartModelList.clear();

                    for (DocumentSnapshot documentSnapshot: task.getResult().getDocuments()){

                        CartModel cartModel = documentSnapshot.toObject(CartModel.class);
                        cartModelList.add(cartModel);

                         mAdapter.setCartModelList(cartModelList);
                         recyclerView.setAdapter(mAdapter);



                    }
                }
            }
        });

        //Adding the total of all orders

        firestore.collection("Cart").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if (value!=null){

                    for (DocumentSnapshot documentSnapshot: value.getDocuments()){

                        CartModel cartModel = documentSnapshot.toObject(CartModel.class);
                        //We are adding all prices into a list of an integer
                        int valueofallprices = cartModel.getTotalprice();
                        saveTotalCost.add(valueofallprices);
                    }

                    for (int i=0; i <saveTotalCost.size(); i++){

                        totalOrderCost += Integer.parseInt(String.valueOf(saveTotalCost.get(i)));
                    }

                    orderSummary.setText("Total is " +String.valueOf(totalOrderCost));
                    totalOrderCost = 0;
                    saveTotalCost.clear();


                }

            }
        });

        // Once we clicked on Order now the cart should empty

        orderbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Reseting the quantities of coffies once ordered is placed


                firestore.collection("coffee").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()){

                            QuerySnapshot tasks = task.getResult();

                            for (DocumentSnapshot ds: tasks.getDocuments()) {


                                ds.getReference()
                                        .update("quantity", 0);




                            }
                        }

                    }
                });

                //Clearing the Cart


                firestore.collection("Cart").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()){

                            QuerySnapshot tasks = task.getResult();

                            for (DocumentSnapshot documentSnapshot: tasks.getDocuments()){

                                documentSnapshot.getReference().delete();

                            }
                        }

                    }
                });
                navController.navigate(R.id.action_coffeeCartFragment_to_allCoffeeListFragment2);
                Toast.makeText(getContext(), "Order Placed Successfully", Toast.LENGTH_SHORT).show();
            }
        });




    }
}