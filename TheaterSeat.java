/*
Camryn Rogers
cpr170030
 */
package Tickets;

public class TheaterSeat extends BaseNode{
    
    // TheaterSeat objects (pointers) for directions
    TheaterSeat up;
    TheaterSeat down;
    TheaterSeat left;
    TheaterSeat right;
    
    // Constructor
    TheaterSeat(){}
            
    // Overloaded constructor
    TheaterSeat(TheaterSeat up, TheaterSeat down, TheaterSeat left, TheaterSeat right)
    {
        this.up = up;
        this.down= down;
        this.left = left;
        this.right = right;
    }
    
    // Mutators
    public void setUp(TheaterSeat up) {this.up = up;}
    public void setDown(TheaterSeat down) {this.down = down;}
    public void setLeft(TheaterSeat left) {this.left = left;}
    public void setRight(TheaterSeat right) {this.right = right;}
    
    // Accessors
    public TheaterSeat getUp() {return this.up;}
    public TheaterSeat getDown() {return this.down;}
    public TheaterSeat getLeft() {return this.left;}
    public TheaterSeat getRight() {return this.right;}
}
