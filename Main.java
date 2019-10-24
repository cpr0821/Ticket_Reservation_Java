/* 
Camryn Rogers 
cpr170030
*/

package Tickets;

import java.util.*;
import java.io.*;               // PrintWriter, File and IOException

public class Main{

    public static void main(String[] args) throws IOException, NullPointerException
    {
        // Making scanner for input from user
        Scanner in = new Scanner(System.in);

        // and the start seat number (and letter from user)
        File A1File, A2File, A3File;
        
        // Link file variables to their respective files
        A1File = new File("A1base.txt");
        A2File = new File("A2base.txt");
        A3File = new File("A3base.txt");

        // Creates the three auditoriums
        if(A1File.exists() && A2File.exists() && A3File.exists())
        {
            // Run program
        }
        else
        {
            // If files don't exist, exit program
            System.out.println("One of the files does not exist.");
            return;
        }

        // Create auditoriums
        Auditorium A1 = new Auditorium(A1File);
        Auditorium A2 = new Auditorium(A2File);
        Auditorium A3 = new Auditorium(A3File);
        
        // Create and fill in the hash map with user names and passwords
        HashMap<String, Customer> map = new HashMap<String,Customer>();
        fillHashMap(map);
        
        // Start interface: Ask for username
        boolean flag, exit = false;
        String user, pass;
        do
        {
            System.out.println("Please enter your username: ");
            user = in.next();
            
            int counter = 0;
            flag = false;
            while(counter < 3 && flag == false)
            {
                System.out.println("Please enter your password: ");
                pass = in.next();
                
                if(!map.containsKey(user))
                {
                    // This map doesn't have the username
                }
                else if (pass.equals(map.get(user).getPass()))
                {
                    flag = true;
                }
                counter++;
            }
            // If the user name is admin, display the admin menu
            if(user.equals("admin"))
            {
                // If the admin chooses to exit, the program ends
                if(admin(A1,A2,A3,in))
                {
                    exit = true;
                }
            }
            else if(flag)
            {
                // If it's a customer, call customer function
                Customer customer = map.get(user);
                customer(customer, in, A1, A2, A3);
            }
            else if(!flag)
            {
                // If the password was wrong 3 times, go back to username
            }
            
        } while(exit == false);
        
        // Print all 3 auditoriums to files with ACS
        printToFile(A1, A1File);
        printToFile(A2, A2File);
        printToFile(A3, A3File);
    }
    
    public static void displayAuditorium(Auditorium aud)
    {
        // Prints out the right amount of letters for the columns
        String columnheader = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        
        System.out.print("  ");
        for (int c = 0; c < aud.getNumCols(); c++)
        {
            System.out.print(columnheader.charAt(c));
        }
        System.out.println();
        
        // Prints out the grid (letters are replaced by #)
        TheaterSeat pointer = aud.first;
        TheaterSeat temp = pointer;
        for (int i = 0; i < aud.getNumRows(); i++)
        {   
            System.out.print((i+1) + " ");
            for (int j = 0; j < aud.getNumCols(); j++)
            {
                if (pointer.getReserved())
                    System.out.print("#");                    
                else
                    System.out.print("."); 
                
                pointer = pointer.getRight();
            }
            System.out.println();
            temp = temp.getDown();
            pointer = temp;
        }
    }
    
    public static boolean checkAvailability(Auditorium aud, int rownumber, int startseatint, int total)
    {
        // If the amount of tickets is larger than the size of the row, not available
        if ((startseatint + total) > aud.getNumCols())
            {
                return false;
            }
        
        // Get to row number and start seat int in grid
        TheaterSeat pointer = aud.first;
        for(int i = 1; i < rownumber; i++)
            pointer = pointer.getDown();
        for(int i = 0; i < startseatint; i++)
            pointer = pointer.getRight();    
       
        // Goes through desired seats, if the seat has a letter it's taken
        for (int i = 0; i < total; i++)
        {
            if(pointer.getReserved())
            {
                return false;
            }
            pointer = pointer.getRight();
        }
        return true;        
    }
    
    public static void reserveSeats(Auditorium aud, int rownumber, int startseatint, int adult, int child, int senior)
    {   
        // Get to where seat starts in grid
        TheaterSeat pointer = aud.first;
        for(int i = 1; i < rownumber; i++)
            pointer = pointer.getDown();
        for(int j = 0; j < startseatint; j++)
            pointer = pointer.getRight();
        
        // For number of adult tickets, replaces periods with As
        for(int i = 0; i < adult; i++)
        {
            pointer.setTicketType('A');
            pointer.setReserved(true);
            pointer = pointer.getRight();
        }
        
        // For number of child tickets, replaces periods with Cs
        for(int i = 0; i < child; i++)
        {
            pointer.setTicketType('C');
            pointer.setReserved(true);
            pointer = pointer.getRight();
        }
        
        // For number of senior tickets, replaces periods with Ss
        for(int i = 0; i < senior; i++)
        {
            pointer.setTicketType('S');
            pointer.setReserved(true);
            pointer = pointer.getRight();
        }   
    }
    
    public static TheaterSeat bestAvailable(Auditorium aud, int total)
    {
        // We're gonna go through the grid, row by row, looking for the number of desired consecutive available seats. Then calculate
        // distance, then find which group of seats has the smallest distance between it and the middle of the grid
        boolean reserved = false;
        double distance = 0;
        final double MIDROW = aud.getNumRows() / 2.0;
        final double MIDCOL = aud.getNumCols() / 2.0;
        int columnint;
        double smallestDistance = 1000000;
        TheaterSeat currentFirstSeat = null;
        double midcolumn = 0;
        
        TheaterSeat pointer = aud.first;
        TheaterSeat curr;
        TheaterSeat temp = pointer;
        for(int x = 0; x < aud.getNumRows(); x++)
        {
            for(int i = 0; i < aud.getNumCols() - total; i++)
            {
                // Look through current pointer and next however many to see if number of requested seats are available
                curr = pointer;
                
                for(int j = 0; j < total; j++)
                {
                    if(curr.getReserved())
                    {
                        reserved = true;
                        break;
                    }
                    else
                    {
                       curr = curr.getRight();
                       reserved = false;
                    }
                }
                
                if(reserved == false)
                {
                    //Calculate distance from center
                    columnint = (int) pointer.getSeat() - 64;   // A = 1, B = 2, etc.
                    midcolumn = ((columnint - total + 1) + columnint) / 2.0;
                    distance = Math.hypot((pointer.getRow() - MIDROW),(midcolumn - MIDCOL));

                    // If new distance is smaller than current smallest diestance, make it your new distance
                    // Else if there's a tie, the smallest row is the new smallest distance
                    if(distance < smallestDistance)
                    {
                       smallestDistance = distance;
                       currentFirstSeat = pointer;
                    }
                    else if(distance == smallestDistance)
                    {
                        if(currentFirstSeat.getRow() > pointer.getRow())
                            currentFirstSeat = pointer;
                    }
                }
                pointer = pointer.getRight();
            }
            temp = temp.getDown();
            pointer = temp;
        }    
        return currentFirstSeat;
    }
    
    public static String getTotals(Auditorium A)
    {
         // Variables for info needed for report
        int totalReserved = 0, adultSold = 0, childSold = 0, seniorSold = 0;
        double totalsales = 0;
        
        // Goes through auditorium and counts adult/child/senior tickets and total seats
        TheaterSeat pointer = A.first;
        TheaterSeat temp = pointer;
        for (int i = 0; i < A.getNumRows(); i++)
        {
            for (int j = 0; j < A.getNumCols(); j++)
            {
               if(pointer.getTicketType() == 'A')
               {
                   adultSold++;
               }
               if(pointer.getTicketType() == 'C')
               {
                   childSold++;
               }
               if(pointer.getTicketType() == 'S')
               {
                   seniorSold++;
               }
               pointer = pointer.getRight();
            }
            temp = temp.getDown();
            pointer = temp;
        }
        
        //Calculates total tickets sold and how much they made
        totalReserved = adultSold + childSold + seniorSold;
        totalsales = (adultSold * 10) + (childSold * 5) + (seniorSold * 7.5);
        String totalsalesstring = "$" + String.format("%.2f", totalsales);
        
        // Outputs all of the information
        String output = "\t" + ((A.getNumCols()*A.getNumRows()) - totalReserved) + 
                "\t" + totalReserved + "\t" + adultSold + "\t" + childSold + 
                        "\t" +  seniorSold + "\t" + totalsalesstring;
        return output;
    }
    
    public static void printReport(Auditorium A1, Auditorium A2, Auditorium A3)
    {
        // Call function to get auditorium totals
        System.out.println("Auditorium 1: " + getTotals(A1));
        System.out.println("Auditorium 2: " + getTotals(A2));
        System.out.println("Auditorium 3: " + getTotals(A3));
       
        // Variables for info needed for report
        int totalOpen = 0, totalReserved = 0, adultSold = 0, childSold = 0, seniorSold = 0;
        double totalsales = 0;
        
        // Goes through auditorium and counts adult/child/senior tickets and total seats
        TheaterSeat pointer = A1.first;
        TheaterSeat temp = pointer;
        for (int i = 0; i < A1.getNumRows(); i++)
        {
            for (int j = 0; j < A1.getNumCols(); j++)
            {
               if(pointer.getTicketType() == 'A')
               {
                   adultSold++;
               }
               if(pointer.getTicketType() == 'C')
               {
                   childSold++;
               }
               if(pointer.getTicketType() == 'S')
               {
                   seniorSold++;
               }
               pointer = pointer.getRight();
            }
            temp = temp.getDown();
            pointer = temp;
        }
        pointer = A2.first;
        temp = pointer;
        for (int i = 0; i < A2.getNumRows(); i++)
        {
            for (int j = 0; j < A2.getNumCols(); j++)
            {
               if(pointer.getTicketType() == 'A')
               {
                   adultSold++;
               }
               if(pointer.getTicketType() == 'C')
               {
                   childSold++;
               }
               if(pointer.getTicketType() == 'S')
               {
                   seniorSold++;
               }
               pointer = pointer.getRight();
            }
            temp = temp.getDown();
            pointer = temp;
        }
        pointer = A3.first;
        temp = pointer;
        for (int i = 0; i < A3.getNumRows(); i++)
        {
            for (int j = 0; j < A3.getNumCols(); j++)
            {
               if(pointer.getTicketType() == 'A')
               {
                   adultSold++;
               }
               if(pointer.getTicketType() == 'C')
               {
                   childSold++;
               }
               if(pointer.getTicketType() == 'S')
               {
                   seniorSold++;
               }
               pointer = pointer.getRight();
            }
            temp = temp.getDown();
            pointer = temp;
        }
        
        //Calculates total tickets sold and how much they made
        totalReserved = adultSold + childSold + seniorSold;
        totalOpen = ((A1.getNumCols()*A1.getNumRows()) + (A2.getNumCols()*A2.getNumRows()) +
                (A3.getNumCols()*A3.getNumRows())) - totalReserved;
        totalsales = (adultSold * 10) + (childSold * 5) + (seniorSold * 7.5);
        String totalsalesstring = String.format("%.2f", totalsales);    
       
        // Get output string for totals
        String output = "\t" + totalOpen + "\t" + totalReserved + "\t" + adultSold + 
                "\t" + childSold + "\t" +  seniorSold + "\t" + totalsalesstring;
    
        // Output totals
        System.out.println("Total:        " + output);
    }
    
    public static void inRangeRow(int rownumber, int numOfRows) throws OutOfRangeException
    {
        // Throws out of range exception is the row is not in the auditorium
        if (rownumber < 1 || rownumber > numOfRows)
            throw new OutOfRangeException("Row not in range.");
    }
    
    public static void inRangeColumn(int seatint, int numOfCols) throws OutOfRangeException
    {
        // Throws out of range excecption if column is not in the auditorium
        if (seatint < 0 || seatint > numOfCols - 1)
            throw new OutOfRangeException("Column not in range.");
    }
    
    public static void ticketsInRange(int tickets) throws OutOfRangeException
    {
        // Throws out of range exception if the number of requested tickets is less than zero
        if(tickets < 0)
            throw new OutOfRangeException("Num of tickets is less than sero");
    }
    
    public static void isChar(String seat) throws InputMismatchException
    {
        // If the seat is not a single char, then throw an input mismatch exception
        if (seat.length() != 1)
            throw new InputMismatchException("Seat is not a single character.");
        if (!Character.isLetter(seat.charAt(0)))
            throw new InputMismatchException("Seat is not a letter.");
    } 
    
    public static void fillHashMap(HashMap<String,Customer> map) throws FileNotFoundException
    {
        // Create scanner and variables
        Scanner in = new Scanner(new File("userdb.dat"));
        String user, pass;
        String line = "";
        int spaceIndex;
        
        // Read the entire file and parse into user and pass, put those into a 
        // customer object and use user as key and customer as pass
        while(in.hasNext())
        {
            line = in.nextLine();
            spaceIndex = line.indexOf(' ');
            user = line.substring(0, spaceIndex);
            pass = line.substring(spaceIndex + 1);
            Customer customer = new Customer(user, pass);
            map.put(user, customer);
        }
        // Close scanner
        in.close();
    }
         
    public static void reservationProcess(Auditorium aud, Scanner in, Customer customer, int orderIndex, int audNum, boolean GB)
    {
        String startseat;
        int rownumber = 0, adult = 0, child = 0, senior = 0, startseatint = 0, total = 0;
        boolean continueInput = true;
        
        // Display the auditorium
        displayAuditorium(aud);
                
        // Ask what seats they want to reserve, what type of tickets and validates the input
        do
        {
            try
            {
                System.out.println("Which row would you like to reserve seats on?");
                rownumber = in.nextInt();
                inRangeRow(rownumber, aud.getNumRows());
                continueInput = false;
            } 
            catch(InputMismatchException ex)
            {
                System.out.println("Input must be an integer. Please try again.");
                in.nextLine();
            }  
            catch(OutOfRangeException ex)
            {
                System.out.println("The value you entered was out of range. Please try again.");
            }
        }while(continueInput);

        continueInput = true;
        do 
        {
            try
            {
                System.out.println("What letter would you like to start your consecutive seat reservations?");
                startseat = in.next();
                isChar(startseat);
                startseatint = (int)startseat.charAt(0) - 65;           // A = 0, B = 1, etc.
                inRangeColumn(startseatint, aud.getNumCols());
                continueInput = false;
            }
            catch(InputMismatchException ex)
            {
                System.out.println("Input must be a single character. Please try again.");
                in.nextLine();
            }  
            catch(OutOfRangeException ex)
            {
                System.out.println("The value you entered was out of range. Please try again.");
            }
        }while(continueInput);
                
        continueInput = true;            
        do
        {
            try
            {
                System.out.println("How many adult tickets would you like?");
                adult = in.nextInt();
                ticketsInRange(adult);
                continueInput = false;
            }
            catch (InputMismatchException ex)
            {
                System.out.println("Number of adult tickets must be an integer. Please try again.");
                in.nextLine();
            }
            catch (OutOfRangeException ex)
            {
                System.out.println("Number of adult tickets must be 0 or greater. Please try again.");
            }
        }while(continueInput);
                
        continueInput = true;            
        do
        {
            try
            {
                System.out.println("How many child tickets would you like?");
                child = in.nextInt();
                ticketsInRange(child);
                continueInput = false;
            }
            catch (InputMismatchException ex)
            {
                System.out.println("Number of child tickets must be an integer. Please try again.");
                in.nextLine();
            }
            catch (OutOfRangeException ex)
            {
                System.out.println("Number of child tickets must be 0 or greater. Please try again.");
            }
        } while(continueInput);
                
        continueInput = true;            
        do
        {
            try
            {
                System.out.println("How many senior tickets would you like?");
                senior = in.nextInt();
                ticketsInRange(senior);
                continueInput = false;
            }
            catch (InputMismatchException ex)
            {
                System.out.println("Number of senior tickets must be an integer. Please try again.");
                in.nextLine();
            }
            catch (OutOfRangeException ex)
            {
                System.out.println("Number of senior tickets must be 0 or greater. Please try again.");
            }
        }while(continueInput);
        
        // Get total
        total = child + senior + adult;
        
        // Checks to see if those seats are available
        // If the are, reserve. If not, look for best available seats
        if(checkAvailability(aud, rownumber, startseatint, total))
        {
            // Reserve seats
            reserveSeats(aud, rownumber, startseatint, adult, child, senior);
            
            // If it's a new order, make a new order and add it to the customer's orders
            if(orderIndex == -1)
            {
                Order order = new Order(audNum,adult,child,senior);
                for(int i = 0; i < total; i++)
                {
                    String toAdd = "" + rownumber + (char)(startseatint + 65 + i);
                    order.seats.add(toAdd);
                }
                customer.orders.add(order);
            }
            else
            {
                // If it is an addition, add to current order
                adult += customer.orders.get(orderIndex).getAdult();
                child += customer.orders.get(orderIndex).getChildren();
                senior += customer.orders.get(orderIndex).getSenior(); 
                customer.orders.get(orderIndex).setAdult(adult);
                customer.orders.get(orderIndex).setChildren(child);
                customer.orders.get(orderIndex).setSenior(senior);
                for(int i = 0; i < total; i++)
                {
                    String toAdd = rownumber + "" + (char)(startseatint + 65 + i);
                    customer.orders.get(orderIndex).seats.add(toAdd);
                }
            }
        }
        else if(GB)
        {
            TheaterSeat bestFirstSeat = bestAvailable(aud, total);
                    
            // If there aren't any seats available in the auditorium, report and return to menu
            if (bestFirstSeat == null)
            {
                System.out.println("The requested seats are not available in the auditorium.");
                // Take them back to the menu
            }

            // If there are, ask them if they want them or not. If yes, reserve. If no, back to menu.
            else
            {
                System.out.println("The seats you selected are not available." + 
                        "The next best available seats are from seat " + 
                            bestFirstSeat.getRow() +  bestFirstSeat.getSeat() + 
                                " to seat " + bestFirstSeat.getRow() +
                                        (char)((int)bestFirstSeat.getSeat() + total - 1) +
                                            ". Would you like to reserve the best available seats? (Y/N)");
                String YN = in.next();

                if ("Y".equals(YN))
                {
                    //reserve seats
                    reserveSeats(aud, bestFirstSeat.getRow(), ((int)bestFirstSeat.getSeat() - 65) , adult, child, senior);
                    System.out.println("Your seats have been reserved.");
                    
                    // Create new order for customer
                    Order order = new Order(audNum,adult,child,senior);
                    for(int i = 0; i < total; i++)
                    {
                        String toAdd = "" + bestFirstSeat.getRow() + (char)((int)bestFirstSeat.getSeat() + i);
                        order.seats.add(toAdd);
                    }
                    customer.orders.add(order);
                }       
                else if ("N".equals(YN))
                {
                    // return to main menu
                }
            }
        }
        else if(!GB)
        {
            // Print error message and return to update menu
            System.out.println("The seats you selected are not " +
                "available. You will be returned to the update menu.");
        }
    }

    public static boolean admin(Auditorium A1,Auditorium A2, Auditorium A3, Scanner in)
    {
        int choice;
        
        // Print menu
        do
        {
            System.out.println("1. Print Report");
            System.out.println("2. Logout");
            System.out.println("3. Exit");
            choice = in.nextInt();
        
            if(choice == 1)
            {
                // Print report and return to admin menu
                printReport(A1,A2,A3);
            }
            else if(choice == 2)
            {
                // Return to user/password page
                return false;
            }
            else if(choice == 3)
            {
                // Quit program
                return true;
            }
            else
                // If they enter something wonky, return to user/password
                return false;
            
        }while(choice == 1);
        
        return false; 
    }
    
    public static void customer(Customer customer, Scanner in, Auditorium A1, Auditorium A2, Auditorium A3)
    {
        // Variables for menu choice
        int choice;
        int total;
        
        // Loop until they choose to log out (option 5)
        do
        {
            // Print out the menu
            System.out.println("Please enter the number of the option you wish to select");
            System.out.println("1. Reserve Seats");
            System.out.println("2. View Orders");
            System.out.println("3. Update Order");
            System.out.println("4. Display Receipt");
            System.out.println("5. Log out");
            
            // Get user's choice
            choice = in.nextInt();
            
            // If they want to reserve seats
            if (choice == 1)
            {
                // Print submenu
                System.out.println("Which auditorium would you like to reserve" +
                        " seats in?");
                System.out.println("1. Auditorium 1");
                System.out.println("2. Auditorium 2");
                System.out.println("3. Auditorium 3");
                
                int subchoice = in.nextInt();
                
                if(subchoice == 1)
                {
                    // Reserve and get best avaialable seats
                    reservationProcess(A1,in, customer, -1, 1, true);
                }
                else if (subchoice == 2)
                {
                    // Reserve and get best avaialable seats
                    reservationProcess(A2, in, customer, -1, 2, true);
                }
                else if (subchoice == 3)
                {
                    // Reserve and get best avaialable seats
                    reservationProcess(A3, in, customer, -1, 3, true);
                }
            }
            else if (choice == 2)
            {
                customer.viewOrders();
            }
            else if (choice == 3)
            {
                updateOrder(customer, in, A1, A2, A3);
            }
            else if(choice == 4)
            {
                customer.displayReceipt();
            }
        } while (choice != 5);
        // If it's 5, return to starting point
    }
    
    public static void updateOrder(Customer customer, Scanner in, Auditorium A1, Auditorium A2, Auditorium A3)
    {
        int choice = 0;
        int orderIndex;
        int audNum;
        char ticketType = 'Z';
       
        // If no orders yet, tell 'em!
        if (customer.orders.isEmpty())
        {
            System.out.println("No orders yet!");
            return;
        }
        
        // Display the orders
        customer.viewOrders();
        
        // Get order to update
        System.out.println("Which order would you like to update?");
        orderIndex = in.nextInt() - 1;
        
        do
        {
            // Display update menu
            System.out.println("1. Add tickets to order");
            System.out.println("2. Delete tickets from order");
            System.out.println("3. Cancel order");
            System.out.println("4. Return to main menu");
            choice = in.nextInt();

            // If they want to add tickets to the order
            if(choice == 1)
            {
                // Step through the reservation process (no best available)
                if(customer.getOrders().get(orderIndex).getAud() == 1)
                {
                    reservationProcess(A1, in, customer, orderIndex, 1, false);  
                }
                else if(customer.getOrders().get(orderIndex).getAud() == 2)
                {
                    reservationProcess(A2, in, customer, orderIndex, 2, false);
                }
                else if(customer.getOrders().get(orderIndex).getAud() == 3)
                {
                    reservationProcess(A3, in, customer, orderIndex, 3, false);
                }
            }
            // If they want to delete a ticket from the order
            else if(choice == 2)
            {
                String toRemove = "";

                // Ask the user for row and seat number to remove
                System.out.println("Which row and seat would you like to remove?" +
                        " Please enter your response in the following format: 3A");
                toRemove = in.next();

               // Check to see if this seat is in the order
               System.out.println(customer.orders.get(orderIndex).seats.toString());
               System.out.println(toRemove);
               if(customer.orders.get(orderIndex).seats.contains(toRemove))
               {
                    // Mark that seat as available in the auditorium
                    audNum = customer.orders.get(orderIndex).getAud();
                    if(audNum == 1){ticketType = unreserveOne(A1, toRemove);}
                    if(audNum == 2){ticketType = unreserveOne(A2, toRemove);}
                    if(audNum == 3){ticketType = unreserveOne(A3, toRemove);}
                   
                   // Then delete it from the order
                   customer.orders.get(orderIndex).seats.remove(toRemove);
                   
                   // Delete ticket type from total count
                   if (ticketType == 'A')
                   {
                       // If it is an adult ticket, remove 1 from total adult count
                       customer.orders.get(orderIndex).setAdult((customer.orders.get(orderIndex).getAdult()) - 1);
                   }
                   else if (ticketType == 'C')
                   {
                       // If it is a child ticket, remove 1 from total child count
                       customer.orders.get(orderIndex).setChildren((customer.orders.get(orderIndex).getChildren()) - 1);
                   }
                   else if (ticketType == 'S')
                   {
                       // If it is a senior ticket, remove 1 from total senior count
                       customer.orders.get(orderIndex).setSenior((customer.orders.get(orderIndex).getSenior()) - 1);
                   }
               }
               else
               {
                   // If it isn't in the order, return to submenu.
                   System.out.println("The seat you entered is not in your order." +
                           " You will be returned to the Update Order submenu.");
               }
            }
            // If they want to cancel the order, delete it
            else if (choice == 3)
            { 
                // Mark those seats as available in the auditorium
                audNum = customer.orders.get(orderIndex).getAud();
                if(audNum == 1){unreserveAll(A1, customer.orders.get(orderIndex).seats);}
                if(audNum == 2){unreserveAll(A2, customer.orders.get(orderIndex).seats);}
                if(audNum == 3){unreserveAll(A3, customer.orders.get(orderIndex).seats);}
                
                // Delete order from account
                customer.orders.remove(orderIndex);
            }
        } while(choice != 4);
    }
    
    public static char unreserveOne(Auditorium aud, String toRemove)
    {
        char toReturn;
        char[] letter;
        
        letter = toRemove.substring(1).toCharArray();
        int rownumber = Integer.parseInt(toRemove.substring(0,1));
        int startSeatInt = letter[0] - 65;
        System.out.println(rownumber + " " + startSeatInt);

        // Get to where seat is in grid
        TheaterSeat pointer = aud.first;
        for(int i = 1; i < rownumber; i++)
            pointer = pointer.getDown();
        for(int j = 0; j < startSeatInt; j++)
            pointer = pointer.getRight();
        
        // Mark as unreserve and make it a "."
        toReturn = pointer.getTicketType();
        pointer.setTicketType('.');
        pointer.setReserved(false);
        
        return toReturn;
    }
    
    public static void unreserveAll(Auditorium aud, LinkedList<String> seats)
    {
        String toRemove;
        int rownumber, startSeatInt;
        
        for(int i = 0; i < seats.size(); i++)
        {
            // Get row number and seat number
            toRemove = seats.get(i);
            rownumber = Integer.parseInt(toRemove.substring(0,1));
            startSeatInt = Integer.parseInt(toRemove.substring(1));
            
            // Get to where seat is in grid
            TheaterSeat pointer = aud.first;
            for(int k = 1; k < rownumber; k++)
                pointer = pointer.getDown();
            for(int j = 0; j < startSeatInt; j++)
                pointer = pointer.getRight();
        
            // Mark as unreserve and make it a "."
            pointer.setTicketType('.');
            pointer.setReserved(false);
        }
        
    }
    
    public static void printToFile(Auditorium aud, File file) throws IOException
    {
        FileWriter fileWriter = new FileWriter(file);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        
        String columnheader = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        
        printWriter.print("  ");
        for (int c = 0; c < aud.getNumCols(); c++)
        {
            printWriter.print(columnheader.charAt(c));
        }
        printWriter.println();
 
        TheaterSeat pointer = aud.first;
        TheaterSeat temp = pointer;
        for (int i = 0; i < aud.getNumRows(); i++)
        {   
            printWriter.print((i+1) + " ");
            for (int j = 0; j < aud.getNumCols(); j++)
            {
                if(pointer.getReserved())
                    printWriter.print(pointer.getTicketType());
                else
                    printWriter.print(".");
                pointer = pointer.getRight();
            }
            temp = temp.getDown();
            pointer = temp;
            printWriter.println();
        }
        printWriter.close();  
    }
}

