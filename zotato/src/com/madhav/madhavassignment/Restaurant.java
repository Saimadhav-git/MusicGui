package com.madhav.madhavassignment;

import java.util.*;

public class Restaurant {
    private Scanner scan=new Scanner(System.in);
    private TreeMap<Integer,Item> items=new TreeMap<>();
    private String name;
    private int points;
    private int id;
    private double restaurant_money;
    private String category;
    private int discount;
    private Company company=null;
    public String getName() {
        return name;
    }

    public int getPoints() {
        return points;
    }

    public double getRestaurant_money() {
        return restaurant_money;
    }

    public String getCategory() {
        return category;
    }

    public int getDiscount() {
        return discount;
    }

    Restaurant(int id,String name, String category,Company company)
    {
        this.id=id;
        this.name=name;
        this.category=category;
        this.company=company;
    }
    public TreeMap<Integer, Item> getItems()
    {
        return this.items;
    }
    public void setItems(TreeMap<Integer,Item> items)
    {
        this.items=items;
    }

    public int getId() {
        return id;
    }

    public void addItem()
    {
        String name;
        int price;
        int quantity;
        String category;
        String offer;
        System.out.println("Enter food item details");
        System.out.println("Food name:");
        name=scan.nextLine();
        System.out.println("item price:");
        price=scan.nextInt();
        scan.nextLine();
        System.out.println("item quantity:");
        quantity=scan.nextInt();
        scan.nextLine();
        System.out.println("item category:");
        category=scan.nextLine();
        System.out.println("offer:");
        offer=scan.nextLine();
        Item item=new Item(name,price,quantity,category,offer);
        int len=this.items.size();
        items.put(len+1,item);
        System.out.println(items.size()+" "+item.toString());
    }
    public void print_items()
    {
        System.out.println("Choose item by code");
        for(Map.Entry<Integer,Item> entry:this.items.entrySet())
        {
            System.out.println(entry.getKey()+" "+entry.getValue());
        }
    }
    public void editItem()
    {
        print_items();
        int index=scan.nextInt();
        scan.nextLine();
        Item item=items.get(index);
        System.out.println("Choose an attribute to edit");
        System.out.println(item.indexString());
        int index_to_change=scan.nextInt();
        scan.nextLine();
        switch (index_to_change)
        {
            case 1:
                System.out.println("Enter new name");
                String name=scan.nextLine();
                item.setName(name);
                break;
            case 2:
                System.out.println("Enter new Price");
                int price=scan.nextInt();
                scan.nextLine();
                item.setPrice(price);
                break;
            case 3:
                System.out.println("Enter new Quantity");
                int quantity=scan.nextInt();
                scan.nextLine();
                item.setQuantity(quantity);
                break;
            case 4:
                System.out.println("Enter new Category");
                String category=scan.nextLine();
                item.setCategory(category);
                break;
            case 5:
                System.out.println("Enter new Offer");
                String offer=scan.nextLine();
                item.setOffer(offer);
                break;
            default:
                System.out.println("enter correct attribute");
                break;
        }
        System.out.println(index+" "+this.name+" "+item.toString());
    }
    private void add_restaurent_money(double bill_value,Company company)
    {
        this.restaurant_money+=bill_value;
        double company_money=bill_value/100.00;
        company.add_company_money(company_money);
        this.restaurant_money-=company_money;
    }
    private void add_rewards(double bill_value,Customer customer)
    {   int new_points=0;
        if(this.category.equals("Fast food"))
        {
            new_points=((int)(bill_value/150)*10);
            this.points+=new_points;
            customer.add_points(new_points);
        }
        else if(this.category.equals("Authentic"))
        {
            new_points=((int)(bill_value/200)*25);
            this.points+=new_points;
            customer.add_points(new_points);
        }
        else
        {
            new_points=((int)(bill_value/100)*5);
            this.points+=new_points;
            customer.add_points(new_points);
        }
    }
    public void handle_restaurant_order(double bill_value,Customer customer)
    {
        add_restaurent_money(bill_value,company);
        add_rewards(bill_value,customer);
    }
    public void rewards()
    {
        System.out.println("Reward Points : "+this.points);
    }
    public void discount()
    {
        System.out.println("Offer on bill value - ");
        int discount=scan.nextInt();
        scan.nextLine();
        this.discount=discount;
    }
    public void restaurant_details()
    {
        System.out.println("Restaurant money "+this.restaurant_money);
    }
}





















