package net.ddns.rapidfill.rapidfilldemoday;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Laurentiu on 5/17/2018.
 */

public class Order {
    String date;
    String email;
    Date orderDate;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    String key;
    ArrayList<Product> products;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public Order(Date date, String email, ArrayList<Product> products, String key) {
        this.orderDate = date;
        this.email = email;
        this.date = orderDate.getTime() + "";
        this.products = products;
        this.key = key;
    }
    public Order(){
        this.products = new ArrayList<>();
    }


}
