import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Font; 

/**
 * Loads a title screen and uses button to wait for a click to switch worlds
 * 
 * @author cwfloyd@email.uscb.edu
 * @version 1.0
 */
public class TitleScreen extends World
{
    /**
     * Constructor for the title screen background
     */
    public TitleScreen()
    {    
        super(1000, 750, 1, false); 
        GreenfootImage titleScreenImage = new GreenfootImage("TitleScreen.png");
        titleScreenImage.scale(1000,750);

        setBackground(titleScreenImage);        
        prepare();
    } // end TitleScreen constructor

    public void started() {
        Button.TITLESCREEN_MUSIC.playLoop();
    } // end method started

    /**
     * Initialize and instantiate buttons.
     */
    private void prepare()
    {
        Button playButton = new Button("titlePlay");
        addObject(playButton,313,516);
        Button rulesButton = new Button("titleRules");
        addObject(rulesButton,669,516);
    }// end method prepare
} // end class TitleScreen