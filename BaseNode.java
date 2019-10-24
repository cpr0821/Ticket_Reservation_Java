/*
Camryn Rogers
cpr170030
 */
package Tickets;

public abstract class BaseNode {
    
    // Private members
    private int row;
    private char seat;
    private boolean reserved;
    private char ticketType;
    
    // Default constructor (wouldn't let me extend class without it)
    protected BaseNode(){}
    
    // Overloaded constructor (protected because only used by subclasses)
    protected BaseNode(int row, char seat, boolean reserved, char ticketType)
    {
        this.row = row;
        this.seat = seat;
        this.reserved = reserved;
        this.ticketType = ticketType;
    }
    
    // Mutators
    public void setRow(int row) {this.row = row;}
    public void setSeat(char seat) {this.seat = seat;}
    public void setReserved(boolean reserved) {this.reserved = reserved;}
    public void setTicketType(char ticketType) {this.ticketType = ticketType;}
    
    // Accessors
    public int getRow() {return this.row;}
    public char getSeat() {return this.seat;}
    public boolean getReserved() {return this.reserved;}
    public char getTicketType() {return this.ticketType;}
}
