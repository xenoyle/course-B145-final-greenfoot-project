import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class RuleScreen here.
 * 
 * @author cwfloyd@email.uscb.edu
 * @version 1.0
 */
public class RuleScreen extends World
{
    /**
     * Constructor for RuleScreen and it's background
     */
    public RuleScreen()
    {    
        super(1000, 750, 1, false); 
        GreenfootImage ruleScreenImage = new GreenfootImage("RuleScreen.png");
        ruleScreenImage.scale(1000,750);

        setBackground(ruleScreenImage);
        prepare();
    } // end RuleScreen constructor
    /**
     * Initialize and instantiate buttons on rule screen.
     */
    private void prepare()
    {
        Button menuButton = new Button("rulesMenu");
        addObject(menuButton,340,681);
        Button playButton = new Button("rulesPlay");
        addObject(playButton,657,681);
    } // end method prepare
} // end RuleScreen class
