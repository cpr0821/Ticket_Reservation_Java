/*
Camryn Rogers
cpr170030
 */
package Tickets;

import java.util.*;

public class Order {
    
    int numAdult, numChildren, numSenior, audNum;
    LinkedList<String> seats = new LinkedList();
    
    // Default constructor
    Order(){}
    
    // Overloaded constructor
    Order (int audNum,int numAdult,int numChildren,int numSenior)
    {
        this.audNum = audNum;
        this.numAdult = numAdult;
        this.numChildren = numChildren;
        this.numSenior = numSenior;
    }
    
    // Setters
    public void setAdult(int numAdult){this.numAdult = numAdult;}
    public void setChildren(int numChildren){this.numChildren = numChildren;}
    public void setSenior(int numSenior){this.numSenior = numSenior;}
    public void setAud(int numAud){this.numAdult = numAud;}
    public void setSeats(LinkedList<String> seats){this.seats = seats;}
    
    // Getters
    public int getAdult(){return numAdult;}
    public int getChildren(){return numChildren;}
    public int getSenior(){return numSenior;}
    public int getAud(){return audNum;}
    public LinkedList<String> getSeats(){return seats;}
    
    @ Override
    public String toString()
    {
        return  "Auditorium " + audNum + "\n" + 
                "Number of Adult Tickets: " + "\t" + numAdult + "\n" +
                "Number of Children Tickets:  " + "\t" + numChildren + "\n" + 
                "Number of Senior Tickets: " + "\t" + numSenior;
                
    }
    
}
