package net.ddns.rapidfill.rapidfilldemoday;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Laurentiu on 5/13/2018.
 */

public class Product {

    public String name;
    public String description;
    public String image;
    public String price;
    public ArrayList<String> attributes = new ArrayList<>();
    public ArrayList<String> values = new ArrayList<>();


    public Product(String name, String description, String image, String price) {
        this.name = name;
        this.description = description;
        this.image = image;
        this.price = price;

    }

    public void Test() {
        attributes.add("Poseison");
        attributes.add("Wolfram");
        attributes.add("Herman");
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Product() {

    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImage(String image) {
        this.image = image;
    }
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }
}
