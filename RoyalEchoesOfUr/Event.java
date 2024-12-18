import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Event maintains data given by GameBoard which is formatted and returned as
 * a built-in text image using Greenfoot
 * 
 * @author cwfloyd@email.uscb.edu 
 * @version 1.0
 */
public class Event extends Actor{
    // true means positive event, false means negative event
    private boolean eventType;
    
    // event properties
    private String title;
    private String description;
    private GreenfootImage eventImage;
    
    /**
     * Handles the data for the events, allowing them to be displayed based on certain conditions in GameBoard
     * 
     * @param String title          title of the event
     * @param String description    description of the event
     * @param boolean isPositive    boolean for checking if the event is for positive or negative echo rolls
     */
    public Event(String title, String description, boolean isPositive) {
        this.title = title;
        this.description = description;
        this.eventImage = new GreenfootImage(title + "\n\n" + description + "\n\nCheck out Acknowledgements.txt for more information!", 16, Color.WHITE, Color.BLACK);
        this.eventType = isPositive;
        setImage(this.eventImage);
    } // end Event 3-arg constructor
} // end class Event
