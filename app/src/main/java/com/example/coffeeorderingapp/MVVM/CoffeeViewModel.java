package com.example.coffeeorderingapp.MVVM;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.coffeeorderingapp.Model.coffeeModel;

import java.util.List;

public class CoffeeViewModel extends ViewModel implements Repository.CoffeeList {

    MutableLiveData<List<coffeeModel>> mutableLiveData = new MutableLiveData<List<coffeeModel>>();
    Repository repository = new Repository(this);

//whenever CoffeeViewModel is called repository get Coffee is called..
    public CoffeeViewModel() {
        repository.getCoffee();
    }

    public LiveData<List<coffeeModel>> getCoffeeList(){
        return mutableLiveData;
    }

    @Override
    public void coffeeLists(List<coffeeModel> coffeeModels) {
        mutableLiveData.setValue(coffeeModels);
    }
}
