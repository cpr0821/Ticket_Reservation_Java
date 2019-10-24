/*
Camryn Rogers
cpr170030
 */
package Tickets;

import java.util.*;

public class Customer {
    
    String user, pass;
    LinkedList<Order> orders = new LinkedList();
    
    Customer(){}
    
    Customer(String user, String pass)
    {
        this.user = user;
        this.pass = pass;
    }
    
    // Setters
    public void setUser(String user){this.user = user;}
    public void setPass(String pass){this.pass = pass;}
    public void setOrders(LinkedList<Order> orders) {this.orders = orders;}
    
    // Getters
    public String getUser(){return this.user;}
    public String getPass(){return this.pass;}
    public LinkedList<Order> getOrders(){return this.orders;}
    
    // View orders           
    public void viewOrders()
    {
        // If no orders yet, tell 'em!
        if (orders.isEmpty())
        {
            System.out.println("No orders yet!");
        }
        
        for(int i = 0; i < orders.size(); i++)
        {
            System.out.println("Order " + (i+1) + ": " + "\n" + orders.get(i).toString());
            
            System.out.print("Seats: ");
            for(int j = 0; j < orders.get(i).seats.size() - 1; j++)
            {
                System.out.print(orders.get(i).seats.get(j) + ", ");
            }
            System.out.print(orders.get(i).seats.get(orders.get(i).seats.size() - 1));
            System.out.print("\n");
        }
    }
    
    public void displayReceipt()
    {
        // If no orders yet, tell 'em!
        if (orders.isEmpty())
        {
            System.out.println("No orders yet!");
        }
        
        // Print individual orders
        int totalA = 0, totalC = 0, totalS = 0;
        int A, C, S;
        
        
        for (int i = 0; i < orders.size(); i++)
        {
            A = orders.get(i).getAdult();
            C = orders.get(i).getChildren();
            S = orders.get(i).getSenior();
            
            System.out.println("Order " + (i+ 1) + ":");
            System.out.println("----------------------------------------------");
            System.out.println("Auditorium " + orders.get(i).getAud());
            System.out.println("Number of Adult Tickets: " + A);
            System.out.println("Number of Children Tickets: " + C);
            System.out.println("Number of Senior Tickets: " + S);
            System.out.println("Seats Reserved: " + orders.get(i).getSeats().toString());
            System.out.println("----------------------------------------------");
            System.out.println("Total amount for order: $" + (A*10 + C*5 + S*7.5));
            System.out.println("**********************************************");
            
            totalA += A;
            totalC += C;
            totalS += S;
        }
        
        // Print totals
        System.out.println("Order Totals:");
        System.out.println("----------------------------------------------");
        System.out.println("Total Number of Adult Tickets: " + totalA);
        System.out.println("Total Number of Children Tickets: " + totalC);
        System.out.println("Total Number of Senior Tickets: " + totalS);
        System.out.println("----------------------------------------------");
        System.out.println("Total amount for all orders: $" + (totalA*10 + totalC*5 + totalS*7.5));
        System.out.println("**********************************************");
        
    }
    
}
