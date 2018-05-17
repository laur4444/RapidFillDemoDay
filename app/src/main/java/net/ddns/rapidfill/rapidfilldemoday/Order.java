package net.ddns.rapidfill.rapidfilldemoday;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Laurentiu on 5/17/2018.
 */

public class Order {
    String date;
    Date orderDate;
    ArrayList<Product> products;

    public Order(Date date, ArrayList<Product> products) {
        this.orderDate = date;
        this.date = orderDate.toString();
        this.products = products;
    }
}
