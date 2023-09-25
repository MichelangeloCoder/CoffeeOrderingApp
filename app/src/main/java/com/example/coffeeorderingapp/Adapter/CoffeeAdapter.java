package com.example.coffeeorderingapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.bumptech.glide.Glide;
import com.example.coffeeorderingapp.Model.coffeeModel;
import com.example.coffeeorderingapp.R;

import java.util.List;

public class CoffeeAdapter extends RecyclerView.Adapter<CoffeeAdapter.CoffeeListHolder> {

    List<coffeeModel> coffeModelList;
    GetOneCoffee interfacegetCoffee;

    public CoffeeAdapter(GetOneCoffee interfacegetCoffee) {
        this.interfacegetCoffee = interfacegetCoffee;
    }

    @NonNull
    @Override
    public CoffeeListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.coffeeliststyle,parent,false);
        return new CoffeeListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CoffeeListHolder holder, int position) {

        holder.coffeename.setText(coffeModelList.get(position).getCoffeename());
        holder.description.setText(coffeModelList.get(position).getDescription());

        //Glide to image upload is used.

        Glide.with(holder.itemView.getContext()).load(coffeModelList.get(position).getImageURL()).into(holder.imageView);


    }

    @Override
    public int getItemCount() {
       return coffeModelList.size();
    }

    public void setCoffeModelList(List<coffeeModel> coffeModelList){
        this.coffeModelList = coffeModelList;
    }

    class CoffeeListHolder extends ViewHolder implements View.OnClickListener{

        TextView coffeename,description;
        ImageView imageView;


        public CoffeeListHolder(@NonNull View itemView) {
            super(itemView);

            coffeename = itemView.findViewById(R.id.coffeeName);
            description = itemView.findViewById(R.id.coffeedetail);
            imageView = itemView.findViewById(R.id.coffeeImage);

            coffeename.setOnClickListener(this);
            description.setOnClickListener(this);
            imageView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {

            interfacegetCoffee.clickedCoffee(getAdapterPosition(),coffeModelList);
        }
    }
    public interface GetOneCoffee{
        void clickedCoffee(int position, List<coffeeModel> coffeeModels);
    }
}
