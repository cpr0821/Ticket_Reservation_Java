/*
Camryn Rogers
cpr170030
 */
package Tickets;

import java.io.*;
import java.util.*;

public class Auditorium {
    
    // Members
    TheaterSeat first;
    private int numCols;
    private int numRows;
    
    // Setters
    public void setNumCols(int numCols){this.numCols = numCols;}
    public void setNumRows(int numRows){this.numRows = numRows;}
    
    // Getters
    public int getNumCols(){return this.numCols;}
    public int getNumRows(){return this.numRows;}
    
    Auditorium(File file) throws FileNotFoundException
    {
        Scanner infile = new Scanner(file);
        String junk = "";
        int row = 0, column = 0;
            
        // Counts how many rows there are 
        while (infile.hasNext())
        {
            junk = infile.nextLine();
            row++;
        }
        
        // Calculates how many columns there are
        column = junk.length();

        /////////////////System.out.println(column);
        // Closes scanner
        infile.close();
        
        // Sets number of rows and columns in auditorium
        this.numCols = column;
        this.numRows = row;
        
        // Creates scanner for the file
        infile = new Scanner(file);

        // Create row 1
        String line = infile.next();
        first = new TheaterSeat();

        // Fill first in with values
        char value = line.charAt(0);
        first.setRow(1);
        first.setSeat('A');
            
        if(value == '.')
            first.setReserved(false);
        else
        {
            first.setReserved(true);
            first.setTicketType(value);
        }
        
        // Link rest of row 1 with first
        for(int i = 1; i < numCols; i++)
        {
            // Create seat object
            TheaterSeat seat = new TheaterSeat();
            
            // Fill in values
            value = line.charAt(i);
            seat.setRow(1);
            seat.setSeat((char)(65 + i));         
            if(value == '.')
                seat.setReserved(false);
            else
            {
                seat.setReserved(true);
                seat.setTicketType(value);
            }
            // Add seat to grid
            add(seat); 
        }
        
        // Set pointer back to beginning of list
        TheaterSeat pointer = first;
        
        // For the mid rows, link down and up
        for(int j = 2; j < numRows + 1; j++)
        {
            // Link first seat in row x to first seat in row x+1
            pointer.setDown(new TheaterSeat());
            TheaterSeat temp = pointer;
            
            // Link up and down
            pointer = pointer.getDown();
            pointer.setUp(temp);
            
            // Fill in values
            line = infile.next();
            value = line.charAt(0);
            pointer.setRow(j);
            pointer.setSeat('A');
            if(value == '.')
            {
                pointer.setReserved(false);
            }
            else
            {
                pointer.setReserved(true);
                pointer.setTicketType(value);
            }
           
            // Create rows after that
            for(int i = 1; i < numCols; i++)
            {
                // Create seat
                TheaterSeat seat = new TheaterSeat();

                // Fill in values
                value = line.charAt(i);
                seat.setRow(j);
                seat.setSeat((char)(65 + i));
                if(value == '.')
                    seat.setReserved(false);
                else
                {
                    seat.setReserved(true);
                    seat.setTicketType(value);
                }
                  
                // If the rows are not the first or last, link up and down, else if last row, just link up
                if (j < numRows)
                    addMidRows(seat, pointer, i); 
                else if (j == numRows)
                    addLastRow(seat, pointer, i);
            }
        }
    }
    
    public void add(TheaterSeat seat)
    {
        // Adds to the right
        TheaterSeat temp = seat;
        TheaterSeat current = first;
        
        // Gets to end of list
        while(current.getRight() != null)
            current = current.getRight();
        
        // Links new theater object at end (right and left)
        current.setRight(temp);
        temp.setLeft(current);
    }
    
    public void addMidRows(TheaterSeat seat, TheaterSeat pointer, int counter)
    {
        // Links seats left right, up and down
        TheaterSeat temp = seat;
        TheaterSeat current = pointer;
        
        // Gets to end of row
        while (current.getRight() != null)
            current = current.getRight();
        
        // Sets at end, links right and left
        current.setRight(temp);
        temp.setLeft(current);
        
        // Links up and down
        TheaterSeat newptr = pointer.getUp();
        for(int i = 0; i < counter; i++)
            newptr = newptr.getRight();
        newptr.setDown(temp);
        temp.setUp(newptr);        
    }
    
    public void addLastRow(TheaterSeat seat, TheaterSeat pointer, int counter)
    {
        // Just links up, left and right
        TheaterSeat temp = seat;
        TheaterSeat current = pointer;
        
        // Gets to end of row
        while (current.getRight() != null)
            current = current.getRight();
        
        // Links left and right
        current.setRight(temp);
        temp.setLeft(current);
        
        // Links up
        TheaterSeat newptr = pointer.getUp();
        for(int i = 0; i < counter; i++)
            newptr = newptr.getRight();
        temp.setUp(newptr);      
    }
}
