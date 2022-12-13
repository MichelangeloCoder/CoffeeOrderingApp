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
import com.example.coffeeorderingapp.Model.CoffeeModel;
import com.example.coffeeorderingapp.R;

import java.util.List;

public class CoffeeAadapter extends RecyclerView.Adapter<CoffeeAadapter.CoffeeListHolder> {


    List<CoffeeModel> coffeeModelList;

    GetOneCoffee interfacegetCoffee;

    public CoffeeAadapter(GetOneCoffee interfacegetCoffee) {
        this.interfacegetCoffee = interfacegetCoffee;
    }

    @Override
    public CoffeeListHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.coffeeliststyle,parent,false);
        return new CoffeeListHolder(view);
    }

    @Override
    public void onBindViewHolder( CoffeeListHolder holder, int position) {

        holder.coffeename.setText(coffeeModelList.get(position).getCoffeename());
        holder.description.setText(coffeeModelList.get(position).getDescription());

        Glide.with(holder.itemView.getContext()).load(coffeeModelList.get(position).getImageURL()).into(holder.imageView);


    }

    @Override
    public int getItemCount() {
        return coffeeModelList.size();
    }

    public void setCoffeeModelList(List<CoffeeModel> coffeeModelList){
        this.coffeeModelList = coffeeModelList;
    }

    class CoffeeListHolder extends ViewHolder implements View.OnClickListener{

        TextView coffeename,description;
        ImageView imageView;

        public CoffeeListHolder(View itemView) {
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

            interfacegetCoffee.clickedCoffee(getAdapterPosition(),coffeeModelList);

        }
    }

    public interface GetOneCoffee{
        void clickedCoffee(int position, List<CoffeeModel>coffeeModels);
    }
}
