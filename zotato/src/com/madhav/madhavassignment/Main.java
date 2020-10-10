package com.madhav.madhavassignment;


import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    private static Scanner scan=new Scanner(System.in);
    private static ArrayList<Restaurant> restaurants=new ArrayList<>();
    private static Company company=new Company("zotato");
    private static ArrayList<Customer> customers=new ArrayList<>();
    private static void get_started()
    {
        Restaurant Shah=new Restaurant(1,"Shah","Authentic",company);
        restaurants.add(Shah);
        Restaurant Ravi=new Restaurant(2,"Ravi's","None",company);
        restaurants.add(Ravi);
        Restaurant Chinese=new Restaurant(3,"The Chinese","Authentic",company);
        restaurants.add(Chinese);
        Restaurant Wang=new Restaurant(4,"Wang's","Fast food",company);
        restaurants.add(Wang);
        Restaurant Paradise=new Restaurant(5,"Paradise","None",company);
        restaurants.add(Paradise);
        Customer Ram=new Customer(1,"Ram","Elite",restaurants);
        customers.add(Ram);
        Customer Sam=new Customer(2,"Sam","Elite",restaurants);
        customers.add(Sam);
        Customer Tim=new Customer(3,"Tim","None",restaurants);
        customers.add(Tim);
        Customer Kim=new Customer(4,"Kim","None",restaurants);
        customers.add(Kim);
        Customer Jim=new Customer(5,"Jim","Special",restaurants);
        customers.add(Jim);
    }
    private static void print_start()
    {
        System.out.println("\t1) Enter as Restaurant Owner");
        System.out.println("\t2) Enter as Customer");
        System.out.println("\t3) CheckUser Details");
        System.out.println("\t4) Company Account details");
        System.out.println("\t5) Exit");
    }
    private static void print_restaurant()
    {
        for(int i=0;i<restaurants.size();i++)
        {
            Restaurant restaurant=restaurants.get(i);
            System.out.println(restaurant.getId()+" "+restaurant.getName()+" ("+restaurant.getCategory()+")");
        }
    }
    private static void print_customers()
    {
        for(int i=0;i<customers.size();i++)
        {
            Customer customer=customers.get(i);
            System.out.println(customer.getId()+" "+customer.getName()+" ("+customer.getCategory()+")");
        }
    }
    private static void print_restaurant_start()
    {
        System.out.println("\t1) Add Item");
        System.out.println("\t2) Edit Item");
        System.out.println("\t3) Print Rewards");
        System.out.println("\t4) Discount on bill value");
        System.out.println("\t5) Exit");
    }
    private static void handle_restaurant()
    {
        System.out.println("Choose restaurant");
        print_restaurant();
        int restaurant_index=scan.nextInt();
        scan.nextLine();
        Restaurant restaurant=restaurants.get(restaurant_index-1);
        boolean exit=false;
        do
        {
            System.out.println("Welcome "+restaurant.getName());
            print_restaurant_start();
            int restaurant_handle_index=scan.nextInt();
            scan.nextLine();
            switch (restaurant_handle_index)
            {
                case 1:restaurant.addItem();break;
                case 2:restaurant.editItem();break;
                case 3:restaurant.rewards();break;
                case 4:restaurant.discount();break;
                case 5:exit=true;
            }
        }while (!exit);

    }
    private static void print_customer_start()
    {
        System.out.println("Customer Menu");
        System.out.println("\t1) Select Restaurant");
        System.out.println("\t2) Checkout cart");
        System.out.println("\t3) Reward won");
        System.out.println("\t4) Print the recent orders");
        System.out.println("\t5) Exit");
    }
    private static void handle_customer()
    {
        print_customers();
        int customer_index=scan.nextInt();
        scan.nextLine();
        Customer customer=customers.get(customer_index-1);
        boolean exit=false;
        do
        {
            System.out.println("Welcome "+customer.getName());
            print_customer_start();
            int customer_handle_index=scan.nextInt();
            scan.nextLine();
            switch (customer_handle_index)
            {
                case 1:customer.handle_select();break;
                case 2:customer.handle_cart();break;
                case 3:customer.check_reward();break;
                case 4:customer.recent_orders();break;
                case 5:exit=true;
            }
        }while (!exit);

    }
    private static void handle_user_details()
    {

        boolean exit=false;
        do {
            System.out.println("1) Customer List\n2) Restaurant List\n3)exit");
            int select_index=scan.nextInt();
            switch (select_index) {
                case 1:
                    print_customers();
                    int customer_index = scan.nextInt();
                    Customer customer = customers.get(customer_index - 1);
                    customer.customer_details();
                    break;
                case 2:
                    print_restaurant();
                    int restaurant_index = scan.nextInt();
                    Restaurant restaurant = restaurants.get(restaurant_index - 1);
                    restaurant.restaurant_details();
                    break;
                case 3:exit=true;break;
            }
        }while (!exit);
    }
    private static void handle_company()
    {
        company.company_details();
    }
    public static void main(String[] args) {
	// write your code here
        get_started();
        boolean exit=false;
        do
        {
            System.out.println("Welcome to Zotato");
            print_start();
            int start_index=scan.nextInt();
            scan.nextLine();
            switch (start_index)
            {
                case 1:handle_restaurant();break;
                case 2:handle_customer();break;
                case 3:handle_user_details();break;
                case 4:handle_company();break;
                case 5:exit=true;break;
            }
        }while (!exit);


    }
}
