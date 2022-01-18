package com.example.recipe;

import java.io.Serializable;
import java.util.ArrayList;

public class ReceipeData implements Serializable {
    String title;
    String img;
    ArrayList<String> order;
    ArrayList<String> material;

    public ReceipeData(String title, String img, ArrayList<String> order, ArrayList<String> material) {
        this.title = title;
        this.img = img;
        this.order = order;
        this.material = material;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public ArrayList<String> getOrder() {
        return order;
    }

    public void setOrder(ArrayList<String> order) {
        this.order = order;
    }

    public ArrayList<String> getMaterial() {
        return material;
    }

    public void setMaterial(ArrayList<String> material) {
        this.material = material;
    }
}
