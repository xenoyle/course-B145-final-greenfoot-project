import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Represents a GameBoard space and its associated attributes (i.e., is it a safe space or not, is it occupied 
 * by a given player's piece or not, etc.)
 * 
 * @author cwfloyd@email.uscb.edu
 * @version 2.0
 */
public class Space extends Actor
{
    /* FIELDS */
    private boolean[] occupiedByPieceForPlayerIndex;
    private boolean safeSpace;
    private boolean echoSpace;
    private int imageID;
    private int randomNumber;
    
    /* CONSTRUCTORS */
    /**
     * Initializes the attributes of this Space object
     * 
     * @param safeSpace     true if this is a safe space, false otherwise
     */
    public Space(boolean safeSpace) 
    {
        occupiedByPieceForPlayerIndex = new boolean[]{false, false};
        this.safeSpace = safeSpace;
        this.echoSpace = echoSpace;
        randomNumber = Greenfoot.getRandomNumber(100);
        GreenfootImage normalSpaceImage = new GreenfootImage((randomNumber<80) ? "boardTile1.png" : "boardTile2.png");
        this.imageID = (randomNumber<80) ? 1 : 2;
        normalSpaceImage.scale(75, 75);                                                            
        GreenfootImage rerollSpaceImage = new GreenfootImage("boardRerollTile.png");
        rerollSpaceImage.scale(75, 75);
        if (this.safeSpace) {
            setImage(rerollSpaceImage);
            this.imageID = 3;
        } else {
            setImage(normalSpaceImage);
        }
    } // end 1-arg Space constructor
    
    /**
     * This getter (accessor) method returns true if this space is occupied by a piece
     * belonging to the player corresponding to playerIndex
     * 
     * @param playerIndex   the index of the player whose piece may be occupying this Space
     */
    public boolean isOccupiedByPieceForPlayerIndex(int playerIndex) {
        return occupiedByPieceForPlayerIndex[playerIndex];
    } // end method isOccupiedByPieceForPlayerIndex
    
    /**
     * This setter (mutator) method updates the value of the current ("this") object's 
     * instance variable value of occupiedByPieceForPlayerIndex using the boolean value assigned to the 
     * local variable of the same name
     * 
     * @param playerIndex   the index of the player whose piece may be occupying this Space
     * @param occupied      true if this Space is now occupied by a piece belonging to the given player
     */
    public void setOccupiedByPieceForPlayerIndex(int playerIndex, boolean occupied) {
        this.occupiedByPieceForPlayerIndex[playerIndex] = occupied;
    } // end method setOccupiedByPieceForPlayerIndex
 
    /**
     * This "getter" method returns `true` if this space is a safe space
     */
    public boolean isSafeSpace() {
        return safeSpace;
    } // end method isSafeSpace
    
    /**
     * This "getter" method returns `true` if this space is an echo space
     */
    public boolean isEchoSpace() {
        return echoSpace;
    } // end method isEchoSpace
    
    /**
     * This "setter" method allows the caller to set the instance variable echoSpace to it's parameter
     * 
     * @param boolean echoSpace     boolean stating if the space contains an echo or not
     */
    public void setEchoSpace(boolean echoSpace) {
        this.echoSpace = echoSpace;
    } // end method isEchoSpace
    
    /**
     * Allows for a toggle of the Space's images based on its current image, so that
     * normal image swaps with echo image and echo image swaps with normal image.
     * 
     * @param imageTypeID   ID for whether assets use echo images (1) or normal images (2)
     */
    public void toggleImageType(int imageTypeID) {
        if (imageTypeID == 1) {
            GreenfootImage echoTile1Image = new GreenfootImage("boardEchoTile1.png");
            GreenfootImage echoTile2Image = new GreenfootImage("boardEchoTile2.png");
            GreenfootImage echoRerollTileImage = new GreenfootImage("boardEchoRerollTile.png");
            if (imageID == 1) {
                setImage(echoTile1Image);
                echoTile1Image.scale(75,75);
            } else if (imageID == 2) {
                setImage(echoTile2Image);
                echoTile2Image.scale(75,75);
            } else {
                setImage(echoRerollTileImage);
                echoRerollTileImage.scale(75,75);
            } // end if/else
        } else {
            GreenfootImage tile1Image = new GreenfootImage("boardTile1.png");
            GreenfootImage tile2Image = new GreenfootImage("boardTile2.png");
            GreenfootImage rerollTileImage = new GreenfootImage("boardRerollTile.png");
            if (imageID == 1) {
                setImage(tile1Image);
                tile1Image.scale(75,75);
            } else if (imageID == 2) {
                setImage(tile2Image);
                tile2Image.scale(75,75);
            } else {
                setImage(rerollTileImage);
                rerollTileImage.scale(75,75);
            } // end if/else
        } // end if/else
    }   // end method toggleImageType
} // end class Space
