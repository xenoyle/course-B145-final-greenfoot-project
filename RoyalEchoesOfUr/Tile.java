import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Tile object that makes up the background tiles and supplementary tiles
 * on the GameBoard, taking a number in initialization to decide which
 * image will be used.
 * 
 * @author cwfloyd@email.uscb.edu
 * @version 2.0
 */
public class Tile extends Actor
{
    /**
     * Constructs image and scale of all Tiles
     * 
     * @param int type  tells the Tile constructor what image it wants to have
     */
    public Tile(int type)
    {
        GreenfootImage imageTL = new GreenfootImage("TL.png");
        imageTL.scale(50,50);
        GreenfootImage imageTM = new GreenfootImage("TM.png");
        imageTM.scale(50,50);
        GreenfootImage imageTR = new GreenfootImage("TR.png");
        imageTR.scale(50,50);
        GreenfootImage imageML = new GreenfootImage("ML.png");
        imageML.scale(50,50);
        GreenfootImage imageMR = new GreenfootImage("MR.png");
        imageMR.scale(50,50);
        GreenfootImage imageBL = new GreenfootImage("BL.png");
        imageBL.scale(50,50);
        GreenfootImage imageBM = new GreenfootImage("BM.png");
        imageBM.scale(50,50);
        GreenfootImage imageBR = new GreenfootImage("BR.png");
        imageBR.scale(50,50);
        GreenfootImage imageM = new GreenfootImage("M.png");
        imageM.scale(50,50);
        
        GreenfootImage imageBoardSupport = new GreenfootImage("boardSupportTile.png");
        imageBoardSupport.scale(75,75);
        
        switch (type) {
            case 1:
                setImage(new GreenfootImage(imageTL));
                break;
            case 2:
                setImage(new GreenfootImage(imageTM));
                break;
            case 3:
                setImage(new GreenfootImage(imageTR));
                break;
            case 4:
                setImage(new GreenfootImage(imageML));
                break;
            case 5:
                setImage(new GreenfootImage(imageMR));
                break;
            case 6:
                setImage(new GreenfootImage(imageBL));
                break;
            case 7:
                setImage(new GreenfootImage(imageBM));
                break;
            case 8:
                setImage(new GreenfootImage(imageBR));
                break;
            case 9:
                setImage(new GreenfootImage(imageBoardSupport));
                break;
            default:
                setImage(new GreenfootImage(imageM));
        } // end switch
    } // end Tile constructor
} // end class Tile
