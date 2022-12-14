package com.example.coffeeorderingapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.coffeeorderingapp.Adapter.CoffeeAadapter;
import com.example.coffeeorderingapp.MVVM.CoffeeViewModel;
import com.example.coffeeorderingapp.Model.CoffeeModel;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AllCoffeeListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllCoffeeListFragment extends Fragment implements CoffeeAadapter.GetOneCoffee {

    FirebaseFirestore firebaseFirestore;
    CoffeeAadapter mAdapter;
    RecyclerView recyclerView;
    CoffeeViewModel viewModel;
    NavController navController;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AllCoffeeListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AllCoffeeListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AllCoffeeListFragment newInstance(String param1, String param2) {
        AllCoffeeListFragment fragment = new AllCoffeeListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_coffee_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);

        recyclerView = view.findViewById(R.id.recViewAll);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new CoffeeAadapter(this);
        navController = Navigation.findNavController(view);
        viewModel = new ViewModelProvider(getActivity()).get(CoffeeViewModel.class);
        viewModel.getCoffeeList().observe(getViewLifecycleOwner(), new Observer<List<CoffeeModel>>() {
            @Override
            public void onChanged(List<CoffeeModel> coffeeModels) {

                mAdapter.setCoffeeModelList(coffeeModels);
                recyclerView.setAdapter(mAdapter);
            }
        });







    }

    @Override
    public void clickedCoffee(int position, List<CoffeeModel> coffeeModels) {

        String coffeeid = coffeeModels.get(position).getCoffeeid();
        String description = coffeeModels.get(position).getDescription();
        String coffeename = coffeeModels.get(position).getCoffeename();
        int price = coffeeModels.get(position).getPrice();
        String imageURL = coffeeModels.get(position).getImageURL();

        AllCoffeeListFragmentDirections.ActionAllCoffeeListFragmentToCoffeeDetailFragment
                action = AllCoffeeListFragmentDirections.actionAllCoffeeListFragmentToCoffeeDetailFragment();

        action.setCoffeename(coffeename);
        action.setDescription(description);
        action.setImageURL(imageURL);
        action.setPrice(price);
        action.setId(coffeeid);

        navController.navigate(action);


    }
}