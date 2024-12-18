import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Represents a player's game piece -- player 1's pieces (for player index 0) 
 * are straight-faced, player 2's pieces (for player index 1) are smiling
 * 
 * @author cwfloyd@email.uscb.edu
 * @version 2.0
 */
public class PlayerPiece extends Actor 
{
    /* FIELDS/INSTANCE VARIABLES */
    private boolean moveable;   
    private int originalXcoord;
    private int originalYcoord;
    private int gameBoardLocationIndex; 
    private int targetGameBoardLocationIndex; 
    private int playerIndex; 
    
    /* CONSTRUCTORS */
    /**
     * Initializes a newly-instantiated PlayerPiece object
     * 
     * @param playerIndex       the index of the player that "owns" this PlayerPiece (0 = player one pieces, 1 = player two pieces)
     * @param originalXcoord    the original X-coordinate of where this playerPiece was located for this player's "starting zone"
     * @param originalYcoord    the original Y-coordinate of where this playerPiece was located for this player's "starting zone"
     */
    public PlayerPiece(int playerIndex, int originalXcoord, int originalYcoord)
    {
        this.playerIndex = playerIndex;
        this.originalXcoord = originalXcoord;
        this.originalYcoord = originalYcoord;
        
        moveable = true;
        gameBoardLocationIndex = -1; // starting position
        targetGameBoardLocationIndex = -1;
        GreenfootImage goodPiece = new GreenfootImage("goodPiece.png");
        GreenfootImage badPiece = new GreenfootImage("badPiece.png");
        goodPiece.scale(50,50);
        badPiece.scale(50,50);
    
        if (this.playerIndex == 0) {
            setImage(new GreenfootImage(goodPiece));
        } else {
            setImage(new GreenfootImage(badPiece));
        } // end if/else
    } // end PlayerPiece 3-arg constructor
    
    /**
     * The `act` method for the PlayerPiece is fairly simple
     * since most of the game logic is handled by the GameBoard class;
     * but we will use the `act` method to set the opacity (transparency)
     * of the object depending on whether or not the piece is moveable
     */
    public void act()
    {
        if (moveable) {
            getImage().setTransparency(255); // if moveable, piece is fully opaque (0 = transparent, 255 = opaque)
        } else {
            getImage().setTransparency(128); // if moveable, piece is 50% transparent
        } // end if/else
    } // end method act
    
    /**
     * Getter (accessor) method for retrieving this player piece's `moveable` state value (true or false)
     * 
     * NOTE: While it is a common Java convention to name "getter" methods by concatenating
     *       the prefix `get` with the name of the corresponding instance variable (while obeying camelCase rules), 
     *       it is usually preferable (more readable) to use the prefix `is` when the 
     *       corresponding instance variable is of type `boolean`
     */
    public boolean isMoveable() {
        return moveable;
    } // end method isMoveable;
    
    /**
     * Updates the moveable state of this PlayerPiece object
     * 
     * @param moveable whether this object should be moveable or not
     */
    public void setMoveable(boolean moveable) {
        this.moveable = moveable; 
    } // end method setMoveable
    
    /**
     * Getter (accessor) method for retrieving the original 
     * X-coordinate (horizontal coordinate) of this PlayerPiece
     */
    public int getOriginalXcoord()
    {
        return originalXcoord;
    } // end method getOriginalXcoord
    
    /**
     * Getter (accessor) method for retrieving the original 
     * Y-coordinate (vertical coordinate) of this PlayerPiece
     */
    public int getOriginalYcoord()
    {
        return originalYcoord;
    } // end method getOriginalYcoord
    
    /**
     * Getter (accessor) method for retrieving the current 
     * gameBoardLocationIndex for this PlayerPiece
     */
    public int getGameBoardLocationIndex() {
        return gameBoardLocationIndex;
    } // end method getGameBoardLocationIndex
    
    /**
     * Setter (mutator) method for UPDATING the gameBoardLocationIndex for this PlayerPiece
     * 
     * @param gameBoardLocationIndex    the current gameBoardLocationIndex 
     */
    public void setGameBoardLocationIndex(int gameBoardLocationIndex) 
    {
        this.gameBoardLocationIndex = gameBoardLocationIndex;
    } // end method getGameBoardLocationIndex
    
    /**
     * Getter (accessor) method for retrieving the possible
     * game board location index based on the current die roll
     */
    public int getTargetGameBoardLocationIndex() {
        return targetGameBoardLocationIndex;
    } // end method gettargetGameBoardLocationIndex
    
    /**
     * Setter (mutator) method for updating the possible TARGET
     * game board location index based on the current die roll
     * 
     * @param targetGameBoardLocationIndex     The possible TARGET game board location index for this piece, given the current die roll
     */
    public void setTargetGameBoardLocationIndex(int targetGameBoardLocationIndex) 
    {
        this.targetGameBoardLocationIndex = targetGameBoardLocationIndex;
    } // end method settargetGameBoardLocationIndex
    
} // end class PlayerPiece
