package com.madhav.madhavassignment;


import java.util.*;

public class Customer {
    public String getName() {
        return name;
    }

    public double getPoints() {
        return points;
    }

    public String getCategory() {
        return category;
    }

    public int getId() {
        return id;
    }

    private String name;
    private double customer_money;
    private double points;
    private int id;
    private String category;
    private Deque<String> bought_items=new ArrayDeque<>();
    private Scanner scan=new Scanner(System.in);
    private TreeMap<Integer,TreeMap<Integer,Item>> cart=new TreeMap<>();
    private ArrayList<Restaurant> restaurants=new ArrayList<>();
    Customer(int id,String name,String category,ArrayList<Restaurant> restaurants)
    {
        this.id=id;
        this.name=name;
        this.category=category;
        this.customer_money=1000;
        this.restaurants=restaurants;
    }
    public void add_points(double points)
    {
        this.points+=points;
    }
    public void handle_select()
    {
        System.out.println("Choose Restaurant");
        for(int i=0;i<this.restaurants.size();i++)
        {
            String extra=(this.restaurants.get(i).getCategory().equals("Authentic"))?("("+this.restaurants.get(i).getCategory()+")"):"";
            System.out.println("\t"+(i+1)+")"+this.restaurants.get(i).getName()+" "+extra);
        }
        int restaurant_index=scan.nextInt();
        scan.nextLine();
        Restaurant restaurant=this.restaurants.get(restaurant_index-1);
        restaurant.print_items();
        int item_index=scan.nextInt();
        scan.nextLine();
        System.out.println("Enter item quantity");
        int item_quantity=scan.nextInt();
        scan.nextLine();
        Item item1=restaurant.getItems().get(item_index);
        Item item2=new Item(item1.getName(),item1.getPrice(),item_quantity,item1.getCategory(),item1.getOffer());
        TreeMap<Integer,Item> items=this.cart.get(restaurant_index);
        if(items==null)
        {
            items=new TreeMap<>();
            items.put(item_index,item2);
            cart.put(restaurant_index,items);
        }
        else {
            items.put(item_index,item2);
        }
        System.out.println("Items added to cart");
    }
    private double calculate_cost(Restaurant restaurant,Item item)
    {
        double item_value=0;
        item_value+=(item.getPrice()*item.getQuantity());
        String item_discount_str=item.getOffer();
        double item_discount_per=0;
        if(!item_discount_str.equals("None"))
        {
            item_discount_per=Double.parseDouble(item_discount_str);
        }
        double item_discount=(item_value)*(item_discount_per/100.00);
        item_value-=item_discount;
        return item_value;
    }
    public void handle_cart()
    {
        double delivery_value=40;
        int item_count=0;
        double item_value=0;
        System.out.println("Items in cart -");
        if(this.cart.size()==0)
        {
            System.out.println("No items in a cart");
            return;
        }
        if(this.cart.size()>1)
        {
            System.out.println("You cannot order from more than two restaurants");
        }
        Restaurant restaurant=null;
        for(Map.Entry<Integer,TreeMap<Integer,Item>> entry:this.cart.entrySet())
        {
            int restaurant_index=entry.getKey();
            restaurant=this.restaurants.get(restaurant_index-1);
            String restaurant_name=restaurant.getName();
            if(entry.getValue().size()==0)
            {
                System.out.println("No items in cart");
                return;
            }
            for(Map.Entry<Integer,Item> inner_entry:entry.getValue().entrySet())
            {
                int item_index=inner_entry.getKey();
                Item new_item=inner_entry.getValue();
                item_count+=new_item.getQuantity();
                System.out.println(item_index+" "+restaurant_name+" "+new_item);
                item_value+=calculate_cost(restaurant,new_item);
            }
            break;
        }
        //restaurant discounts
        double overall_discount=0;
        double over_discount=0;
        String restaurant_category=restaurant.getCategory();
        if(restaurant_category.equals("Fast food"))
        {
            over_discount=restaurant.getDiscount();
            overall_discount+=((item_value/100.00)*over_discount);
            item_value-=overall_discount;
        }
        else if(restaurant_category.equals("Authentic"))
        {
            over_discount=restaurant.getDiscount();
            overall_discount+=(item_value)*(over_discount/100.00);
            item_value-=overall_discount;
            if(item_value>100)
            {
                item_value-=50;
            }
        }
        if(this.category.equals("Elite"))
        {
            delivery_value=0;
        }
        else if(this.category.equals("Special"))
        {
            if(item_value>200)
            {
                delivery_value=25;
            }
            else
           {
               delivery_value=20;
            }
        }
        else
        {
            delivery_value=40;
        }
        item_value+=delivery_value;
        System.out.println("delivery charge  - "+delivery_value);
        System.out.println("Total order value - INR "+item_value+"/- ");
        System.out.println("\t1)Proceed to checkout");
        int proceed_value=scan.nextInt();
        scan.nextLine();
        if(proceed_value==1)
        {
            if(handle_money(restaurant,item_value))
            {
                System.out.println(item_count+" items successfully bought for INR "+item_value);
                this.cart.clear();
            }
            else
            {
                System.out.println("There was an error");
            }
        }
    }
    private boolean handle_money(Restaurant restaurant,double item_value)
    {
        double money_to_be_paid=item_value;
        if(this.points>0 && this.points<money_to_be_paid)
        {
            money_to_be_paid-=this.points;
            this.points=0;
        }
        else if(this.points>0 && this.points>money_to_be_paid)
        {
            this.points-=money_to_be_paid;
            money_to_be_paid=0;
        }
        if(money_to_be_paid!=0 && this.customer_money>money_to_be_paid)
        {
            this.customer_money-=money_to_be_paid;
            money_to_be_paid=0;
        }
        else
        {
            System.out.println("U do not have enough money in your cart");
            return false;
        }
        StringBuilder str=new StringBuilder();
        for(Map.Entry<Integer,TreeMap<Integer,Item>> entry:this.cart.entrySet())
        {
            for(Map.Entry<Integer,Item> inner_entry:entry.getValue().entrySet())
            {
                str.append(inner_entry.getValue().toString()+"\n");
            }
            break;
        }
        this.bought_items.push(restaurant.getName()+" "+str.toString());
        restaurant.handle_restaurant_order(item_value,this);
        return true;
    }
    public void check_reward()
    {
        System.out.println("Total rewards = "+this.points);
    }
    public void recent_orders()
    {
        int count=10;
        Iterator<String> iterator=this.bought_items.descendingIterator();
        while (count>0 && iterator.hasNext())
        {
            System.out.println(iterator.next());
            count-=1;
        }
    }
    public void customer_details()
    {
        System.out.println("Customer money = "+this.customer_money);
    }
}