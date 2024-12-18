import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Places an invisible button over background buttons to create interactivity with the screen.
 * Mainly used to switch worlds.
 * 
 * @author cwfloyd@email.uscb.edu
 * @version 1.0
 */
public class Button extends Actor
{
    String buttonType;
    
    public static final GreenfootSound TITLESCREEN_MUSIC = new GreenfootSound("TitleScreen.mp3");
    public static final GreenfootSound GAMEBOARD_MUSIC = new GreenfootSound("GameBoard.mp3");
    /**
     * A button that has no transparency and stores certain string types to determine
     * click function.
     * 
     * @param String buttonType     A string telling the Button which dimensions to use and which actions to perform
     */
    public Button(String buttonType) {
        GreenfootImage thisImage = this.getImage();
        switch (buttonType) {
                case "titlePlay":
                case "titleRules":
                    this.buttonType = buttonType;
                    thisImage.scale(282,141);
                    thisImage.setTransparency(0);
                    break;
                case "rulesMenu":
                case "rulesPlay":
                    this.buttonType = buttonType;
                    thisImage.scale(286,96);
                    thisImage.setTransparency(0);
                    break;
                default:
                    break;
            }
    }
    
    /**
     * Checks every frame for mouseClicked(this), used for title screen and rules screen buttons
     */
    public void act()
    {
        GAMEBOARD_MUSIC.setVolume(30);
        if (Greenfoot.mouseClicked(this)) {
            switch (buttonType) {
                case "titlePlay", "rulesPlay":
                    TITLESCREEN_MUSIC.stop();
                    Greenfoot.setWorld(new GameBoard());
                    GAMEBOARD_MUSIC.playLoop();
                    break;
                case "titleRules":
                    Greenfoot.setWorld(new RuleScreen());
                    break;
                case "rulesMenu":
                    Greenfoot.setWorld(new TitleScreen());
                    break;
                default:
                    break;
            }
        }
    }
}
