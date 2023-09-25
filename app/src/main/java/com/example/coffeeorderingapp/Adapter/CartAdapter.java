package com.example.coffeeorderingapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.coffeeorderingapp.Model.CartModel;
import com.example.coffeeorderingapp.R;

import java.util.List;


public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CoffeeListHolder> {

    List<CartModel> cartModelList;


    @NonNull
    @Override
    public CoffeeListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cartlistlayout,parent,false);
        return new CoffeeListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CoffeeListHolder holder, int position) {

        Glide.with(holder.itemView.getContext()).load(cartModelList.get(position).getImageURL()).into(holder.imageofCoffee);

        holder.price.setText("Ordered " + String.valueOf(cartModelList.get(position).getQuantity()) + " for ₹ " + String.valueOf(cartModelList.get(position).getTotalprice()));

        holder.name.setText(cartModelList.get(position).getCoffeename());



    }

    @Override
    public int getItemCount() {
        return cartModelList.size();
    }

    public void setCartModelList(List<CartModel> cartModelList) {
        this.cartModelList = cartModelList;
    }

    class CoffeeListHolder extends ViewHolder{

        TextView name, price;
        ImageView imageofCoffee;

        public CoffeeListHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.cartcoffeename);
            price = itemView.findViewById(R.id.orderdetail);
            imageofCoffee = itemView.findViewById(R.id.cartImage);
        }
    }
}
