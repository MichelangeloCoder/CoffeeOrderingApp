package com.example.coffeeorderingapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.coffeeorderingapp.Adapter.CoffeeAdapter;
import com.example.coffeeorderingapp.AllCoffeeListFragmentDirections;
import com.example.coffeeorderingapp.MVVM.CoffeeViewModel;
import com.example.coffeeorderingapp.Model.CartModel;
import com.example.coffeeorderingapp.Model.coffeeModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AllCoffeeListFragment extends Fragment implements CoffeeAdapter.GetOneCoffee {

    FirebaseFirestore firebaseFirestore;
    CoffeeAdapter mAdapter;
    RecyclerView recyclerView;
    CoffeeViewModel viewModel;
    NavController navController;
    int quantity = 0;
    TextView quantityonFab;
    List<Integer> savequantity = new ArrayList<>();

    int quantitysum =0;
    FloatingActionButton floatingActionButton;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_coffee_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseFirestore = FirebaseFirestore.getInstance();

        recyclerView = view.findViewById(R.id.recViewAll);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new CoffeeAdapter(this);
        navController = Navigation.findNavController(view);
        quantityonFab = view.findViewById(R.id.quantityonFab);
        floatingActionButton = view.findViewById(R.id.fab);
        viewModel = new ViewModelProvider(getActivity()).get(CoffeeViewModel.class);
        viewModel.getCoffeeList().observe(getViewLifecycleOwner(), new Observer<List<coffeeModel>>() {
            @Override
            public void onChanged(List<coffeeModel> coffeeModels) {

                mAdapter.setCoffeModelList(coffeeModels);
                recyclerView.setAdapter(mAdapter);
            }
        });

        quantity = AllCoffeeListFragmentArgs.fromBundle(getArguments()).getQuantity();

        firebaseFirestore.collection("Cart").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()){


                    for (DocumentSnapshot documentSnapshot: task.getResult().getDocuments()){

                        CartModel cartModel = documentSnapshot.toObject(CartModel.class);
                        int initialquantity = cartModel.getQuantity();

                        savequantity.add(initialquantity);
                    }
                    //for loop is implemented below..

                    for (int i =0; i < savequantity.size(); i++){

                        quantitysum+= Integer.parseInt(String.valueOf(savequantity.get(i)));
                    }

                    quantityonFab.setText(String.valueOf(quantitysum));

                    quantitysum = 0;
                    savequantity.clear();
                }

            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_allCoffeeListFragment_to_coffeeCartFragment2);
            }
        });
    }

    @Override
    public void clickedCoffee(int position, List<coffeeModel> coffeeModels) {

        String coffeeid = coffeeModels.get(position).getCoffeeid();
        String description = coffeeModels.get(position).getDescription();
        String coffeename = coffeeModels.get(position).getCoffeename();
        String imageURL = coffeeModels.get(position).getImageURL();
        int price = coffeeModels.get(position).getPrice();

        AllCoffeeListFragmentDirections.ActionAllCoffeeListFragmentToCoffeeDetailFragment
                action = AllCoffeeListFragmentDirections.actionAllCoffeeListFragmentToCoffeeDetailFragment();

        action.setCoffeename(coffeename);
        action.setDescription(description);
        action.setImageurl(imageURL);
        action.setPrice(price);
        action.setId(coffeeid);

        navController.navigate(action);


    }

}