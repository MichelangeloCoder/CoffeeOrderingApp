package com.example.coffeeorderingapp.Model;

import com.google.firebase.firestore.DocumentId;

public class coffeeModel {

    //String & Int Value taken from firebase.
    @DocumentId// its a unique id we can access coffees from firebase..
    String coffeeid;
    String coffeename, description, imageURL;
    int price,quantity;

    //create a empty constructor need by firebase..

    public coffeeModel() {
    }

    public coffeeModel(String coffeeid, String coffeename, String description, String imageURL, int price, int quantity) {
        this.coffeeid = coffeeid;
        this.coffeename = coffeename;
        this.description = description;
        this.imageURL = imageURL;
        this.price = price;
        this.quantity = quantity;
    }

    public String getCoffeeid() {
        return coffeeid;
    }

    public void setCoffeeid(String coffeeid) {
        this.coffeeid = coffeeid;
    }

    public String getCoffeename() {
        return coffeename;
    }

    public void setCoffeename(String coffeename) {
        this.coffeename = coffeename;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "coffeeModel{" +
                "coffeeid='" + coffeeid + '\'' +
                ", coffeename='" + coffeename + '\'' +
                ", description='" + description + '\'' +
                ", imageURL='" + imageURL + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                '}';
    }
}
