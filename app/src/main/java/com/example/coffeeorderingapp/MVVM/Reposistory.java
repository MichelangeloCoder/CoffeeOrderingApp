package com.example.coffeeorderingapp.MVVM;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.coffeeorderingapp.Model.CoffeeModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Reposistory {

    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    List<CoffeeModel> coffeeModelList = new ArrayList<>();

    CoffeeList interfacecoffeelist;

    public Reposistory(CoffeeList interfacecoffeelist) {
        this.interfacecoffeelist = interfacecoffeelist;
    }

    public void getCoffee(){

        firebaseFirestore.collection("coffees").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {


                if (task.isSuccessful()){

                    coffeeModelList.clear();

                    for (DocumentSnapshot documentSnapshot:task.getResult().getDocuments()){

                        CoffeeModel coffeeModel = documentSnapshot.toObject(CoffeeModel.class);
                        coffeeModelList.add(coffeeModel);

                        interfacecoffeelist.coffeeLists(coffeeModelList);


                    }
                }



            }
        });
    }

    public interface CoffeeList{
        void coffeeLists(List<CoffeeModel> coffeeModels);
    }
}
