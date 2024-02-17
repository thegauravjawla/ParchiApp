package com.example.tempbilling;

public class Item {
    String itemName;
    Float rate;
    Float weight;
    Integer quantity;
    boolean isBundleChecked;
    Float total;

    public String getItemName() {
        return itemName;
    }

    public Float getRate() {
        return rate;
    }

    public Float getWeight() {
        return weight;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public boolean isBundleChecked() {
        return isBundleChecked;
    }

    public Float getTotal() {
        return total;
    }

    public Item(String itemName, Float rate, Float weight, Integer quantity, Boolean isBundleChecked, Float total) {
        this.itemName = itemName;
        this.rate = rate;
        this.weight = weight;
        this.quantity = quantity;
        this.isBundleChecked = isBundleChecked;
        this.total = total;
    }
}

