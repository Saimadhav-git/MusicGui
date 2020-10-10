package com.madhav.madhavassignment;

public class Item {
    private String name;
    private int price;
    private int quantity;
    private int id;
    private String category;
    private String offer;
    Item(String name,int price,int quantity,String category,String offer)
    {
        this.name=name;
        this.price=price;
        this.quantity=quantity;
        this.category=category;
        this.offer=offer;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getCategory() {
        return category;
    }

    public String getOffer() {
        return offer;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setOffer(String offer) {
        this.offer = offer;
    }

    @Override
    public String toString() {
        String extra=(this.offer.equals("None"))?"":"% off";

        return
                name+" "+price+" "+quantity+" "+offer+" "+extra+" "+category;
    }
    public String indexString()
    {
        return "\t1) Name\n\t2) Price\n\t3) Quantity\n\t4) Category\n\t5) Offer";
    }
}



















