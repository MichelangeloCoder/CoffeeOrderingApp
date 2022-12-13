package com.example.coffeeorderingapp.MVVM;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.coffeeorderingapp.Model.CoffeeModel;

import java.util.List;

public class CoffeeViewModel extends ViewModel implements Reposistory.CoffeeList {

    MutableLiveData<List<CoffeeModel>> mutableLiveData = new MutableLiveData<List<CoffeeModel>>();
    Reposistory reposistory = new Reposistory(this);

    public CoffeeViewModel(){reposistory.getCoffee();
    }


    public LiveData<List<CoffeeModel>> getCoffeeList(){return mutableLiveData;
    }


    @Override
    public void coffeeLists(List<CoffeeModel> coffeeModels) {
        mutableLiveData.setValue(coffeeModels);
    }
}
