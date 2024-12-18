import greenfoot.*;  // imports Actor, World, Greenfoot, GreenfootImage

/**
 * The physical game board for Connor Floyd's "The Royal Echoes of Ur" game
 * 
 * @author cwfloyd@email.uscb.edu
 * @version 2.0
 */
public class GameBoard extends World
{   
    private boolean[] playerIsHumanForPlayerIndex;      // 1-D array that stores `true` (human) or `false` (CPU) for the player at the given array index

    private Space[] spaces;                             // 1-D array that stores Space objects (gameboard)
    private Tile[] tiles;                               // 1-D array that stores Tile objects (background)
    public static final int HEIGHT = 750;               // public height of GameBoard world
    public static final int WIDTH = 1000;                // public width of GameBoard world

    private PlayerPiece[][] playerPieces;               // 2-D array of PlayerPiece object references; row index 0 (player one) or 1 (player two)

    private final int NUMBER_OF_PIECES_PER_PLAYER = 4;  // set this value here, once, then use the constant to get this value elsewhere in the class
    private final int DELAY_LENGTH = 60;                 // set the duration used when Greenfoot.delay is called 

    private int[] playerOneStartYCoords;                // 1-D array of ints for storing the starting Y coordinates for player one pieces (initialized in constructor)
    private int[] playerTwoStartYCoords;                // 1-D array of ints for storing the starting Y coordinates for player two pieces (initialized in constructor)

    private Space[][] movementPathForPlayerIndex;       // 2-D array used to "map" each player's movement path to corresponding Spaces on the game board

    private int echoRollValue;                          // store the echo roll result

    public static final int ECHO_MAGNITUDE = 3;         // the distance an echo roll is from 0 on either side

    private Die die;                                    // reference to a Die object
    private int dieRollValue;                           // value of the most recent die roll
    private final int DIE_TEXT_VERTICAL_OFFSET = 50;    // determine where to display text beneath the Die's y-position

    private Event[] eventArray;
    private int state;                                  // an int value that can be referred to by the constants below

    /* public CONSTANTS for keeping track of the game's overall state */
    public static final int BOARD_SETUP = 0;           
    public static final int PLAYER1_ROLL_DIE = 1;      
    public static final int PLAYER1_MOVE = 2;    
    public static final int PLAYER2_ROLL_DIE = 3;      
    public static final int PLAYER2_MOVE = 4; 
    public static final int PLAYER1_WIN = 5;            
    public static final int PLAYER2_WIN = 6;            

    private boolean readyToExitState;                   // value for tracking whether or not we are ready to update the game's overall state

    private boolean onSafeSpaceSoRollAgain;             // value for tracking whether or not the current player just landed on a safe space

    private int[] goalCountForPlayerIndex;              // value for tracking the number of player one pieces (for player index 0) or player two pieces (for player index 1) 
    // that have moved into the corresponding player's goal

    /* CONSTRUCTOR(S) */
    /**
     * Initialize the GameBoard object and its variable fields
     */
    public GameBoard() 
    {
        super(WIDTH, HEIGHT, 1, false);                             // constructs word using WIDTH and HEIGHT, unbounded      

        /* initialize instance variables */
        playerIsHumanForPlayerIndex = new boolean[]{true, false};   // `true, false`  -> player one (player index 0) is human, player two (index 1) is CPU
        // `true, true`   -> player one is human, player two is also human

        spaces = new Space[20];     // empty array of 20 null references to Space; Space assignments are handled by the prepare method below
        tiles = new Tile[300];      // empty array of 100 null references to Tile; Tile assignments are handled by the prepare method below
        eventArray = new Event[8];

        playerPieces = new PlayerPiece[2][NUMBER_OF_PIECES_PER_PLAYER]; // always 2 players, with 4 (variable) pieces per player
        state = BOARD_SETUP;
        readyToExitState = false;
        onSafeSpaceSoRollAgain = false;

        goalCountForPlayerIndex = new int[2];   // set size of array to be 2 elements long 
        goalCountForPlayerIndex[0] = 0;         // at start of game, no player one pieces in the goal
        goalCountForPlayerIndex[1] = 0;         // at start of game, no player two pieces in the goal

        playerOneStartYCoords = new int[]{60, 120, 180, 240};
        playerTwoStartYCoords = new int[]{60, 120, 180, 240};

        prepare();
    } // end GameBoard no-arg constructor

    /* METHODS */
    /**
     * Prepare the world for the start of the program.
     * Create the initial objects and add them to the world.
     */
    private void prepare()
    {

        // positive events
        eventArray[0] = (new Event(
                "The Great Gardens of Lagash", 
                "Under the rule of Gudea, the city-state of Lagash developed irrigation systems that transformed barren lands into lush gardens. \nThese gardens are timeless examples of engineering and environmental management skills. \nWhat else has Mesopotamian engineering accomplished?", 
                true  // positive event
            ));
        eventArray[1] = (new Event(
                "The First Written Music",
                "Archaeologists discovered musical notation on a clay tablet from Nippur, representing the earliest known written musical composition in human history. \nExperts still don't know for sure how to play the notes recorded. \nWhat types of instruments would they play with?.",
                true
            ));
        eventArray[2] = (new Event(
                "The Code of Ur-Nammu",
                "King Ur-Nammu of Ur created the oldest known written legal code, predating the more famous Code of Hammurabi by several centuries. \nThis code introduced the concept of monetary compensation for injuries and established a systematic approach to justice. \nHow similar is their improved system to the systems of today?",
                true
            ));
        eventArray[3] = (new Event(
                "Advancement of Astronomical Knowledge",
                "Mesopotamian scholars made significant astronomical observations, developing mathematical models to track celestial movements. \nThey kept detailed records and could predict celestial events. \nWhat theories did they create for the cosmos?",
                true
            ));

        // negative Events
        eventArray[4] = (new Event(
                "The Great Flood of Shuruppak",
                "A massive flooding event devastated the city of Shuruppak, causing widespread destruction. \nThere is a story about the flood in which the king of Shuruppak, King Utnapishtim, built a boat to survive the flood. \nThis may have inspired Noah's Ark. Is this story true?",
                false  // negative event
            ));
        eventArray[5] = (new Event(
                "Slave Labor in Temple Construction",
                "Many large-scale temple and infrastructure projects in Mesopotamian cities were built using extensive slave labor, often involving prisoners of war \nor individuals in extreme economic debt. To what extent were slaves a part of Mesopotamian society?",
                false
            ));
        eventArray[6] = (new Event(
                "The Akkadian Empire's Collapse",
                "Around 2154 BCE, the Akkadian Empire underwent a catastrophic collapse, which was caused by a combination of climate change, resource depletion, and internal conflicts. \nWhat could cause the collapse of such a large empire?",
                false
            ));
        eventArray[7] = (new Event(
                "Devastating Drought Period",
                "Between 2200-2100 BCE, a severe drought struck Mesopotamia, causing agricultural failures, food shortages, and significant social upheaval. \nHow could a drought happen in an environment with freshwater rivers?",
                false
            ));

        /* set background image using Tile objects */
        for (int i = 0; i<300; i++) {
            /* background tiles */
            if ((i == 0)) {
                tiles[i] = new Tile(1);
            } else if (i == 19) {
                tiles[i] = new Tile(3);
            } else if (i == 280) {
                tiles[i] = new Tile(6);
            } else if (i == 299) {
                tiles[i] = new Tile(8);
            } else if (i < 19) {
                tiles[i] = new Tile(2);
            } else if (i%20 == 0) {
                tiles[i] = new Tile(4);
            } else if (i%20 == 19) {
                tiles[i] = new Tile(5);
            } else if (i > 280) {
                tiles[i] = new Tile(7);
            } else {
                tiles[i] = new Tile(0);
            } // end if
            addObject(tiles[i], (i%20)*50+25, (i/20)*50+25);
        } // end for

        /* place Space objects on screen */
        spaces[0]  = new Space(true);
        spaces[1]  = new Space(false);
        spaces[2]  = new Space(true);
        spaces[3]  = new Space(false);
        spaces[4]  = new Space(false);
        spaces[5]  = new Space(false);
        spaces[6]  = new Space(false);
        spaces[7]  = new Space(false);
        spaces[8]  = new Space(false);
        spaces[9]  = new Space(false);
        spaces[10] = new Space(true);
        spaces[11] = new Space(false);
        spaces[12] = new Space(false);
        spaces[13] = new Space(false);
        spaces[14] = new Space(true);
        spaces[15] = new Space(false);
        spaces[16] = new Space(true);
        spaces[17] = new Space(false);
        spaces[18] = new Space(false);
        spaces[19] = new Space(false);
        addObject(spaces[0],  (int)(0.425 * WIDTH)+50, 75); // left
        addObject(spaces[1],  (int)(0.5 * WIDTH)+50, 75); // middle
        addObject(spaces[2],  (int)(0.575 * WIDTH)+50, 75); // right
        addObject(spaces[3],  (int)(0.425 * WIDTH)+50, 150); // left
        addObject(spaces[4],  (int)(0.5 * WIDTH)+50, 150); // middle
        addObject(spaces[5],  (int)(0.575 * WIDTH)+50, 150); // right
        addObject(spaces[6],  (int)(0.425 * WIDTH)+50, 225); // left
        addObject(spaces[7],  (int)(0.5 * WIDTH)+50, 225); // middle
        addObject(spaces[8],  (int)(0.575 * WIDTH)+50, 225); // right
        addObject(spaces[9],  (int)(0.425 * WIDTH)+50, 300); // left
        addObject(spaces[10], (int)(0.5 * WIDTH)+50, 300); // middle
        addObject(spaces[11], (int)(0.575 * WIDTH)+50, 300); // right
        addObject(spaces[12], (int)(0.5 * WIDTH)+50, 375); // middle
        addObject(spaces[13], (int)(0.5 * WIDTH)+50, 450); // middle
        addObject(spaces[14], (int)(0.425 * WIDTH)+50, 525); // left
        addObject(spaces[15], (int)(0.5 * WIDTH)+50, 525); // middle
        addObject(spaces[16], (int)(0.575 * WIDTH)+50, 525); // right
        addObject(spaces[17], (int)(0.425 * WIDTH)+50, 600); // left
        addObject(spaces[18], (int)(0.5 * WIDTH)+50, 600); // middle
        addObject(spaces[19], (int)(0.575 * WIDTH)+50, 600); // right

        /* complementary board tiles */
        addObject(new Tile(9), (int)(0.425 * WIDTH)+50, 375); // left 
        addObject(new Tile(9), (int)(0.575 * WIDTH)+50, 375); // right 
        addObject(new Tile(9), (int)(0.425 * WIDTH)+50, 675); // bottom
        addObject(new Tile(9), (int)(0.5 * WIDTH)+50, 675); // bottom
        addObject(new Tile(9), (int)(0.575 * WIDTH)+50, 675); // bottom

        /* set up movement path for player 1 and player 2 */
        movementPathForPlayerIndex = new Space[2][14];  // movementPathForPlayerIndex array has 2 rows (one per player)
        // each player has a path of 14 board spaces to move through

        /* player one movement path */
        movementPathForPlayerIndex[0][0]  = spaces[9]; // spaces are ordered in left to right, top to bottom
        movementPathForPlayerIndex[0][1]  = spaces[6]; 
        movementPathForPlayerIndex[0][2]  = spaces[3]; 
        movementPathForPlayerIndex[0][3]  = spaces[0]; 
        movementPathForPlayerIndex[0][4]  = spaces[1]; 
        movementPathForPlayerIndex[0][5]  = spaces[4]; 
        movementPathForPlayerIndex[0][6]  = spaces[7];
        movementPathForPlayerIndex[0][7]  = spaces[10]; 
        movementPathForPlayerIndex[0][8]  = spaces[12]; 
        movementPathForPlayerIndex[0][9]  = spaces[13]; 
        movementPathForPlayerIndex[0][10] = spaces[15]; 
        movementPathForPlayerIndex[0][11] = spaces[18]; 
        movementPathForPlayerIndex[0][12] = spaces[17]; 
        movementPathForPlayerIndex[0][13] = spaces[14]; 

        /* player two movement path */
        movementPathForPlayerIndex[1][0]  = spaces[11]; 
        movementPathForPlayerIndex[1][1]  = spaces[8]; 
        movementPathForPlayerIndex[1][2]  = spaces[5]; 
        movementPathForPlayerIndex[1][3]  = spaces[2]; 
        movementPathForPlayerIndex[1][4]  = spaces[1]; 
        movementPathForPlayerIndex[1][5]  = spaces[4]; 
        movementPathForPlayerIndex[1][6]  = spaces[7];
        movementPathForPlayerIndex[1][7]  = spaces[10]; 
        movementPathForPlayerIndex[1][8]  = spaces[12]; 
        movementPathForPlayerIndex[1][9]  = spaces[13]; 
        movementPathForPlayerIndex[1][10] = spaces[15]; 
        movementPathForPlayerIndex[1][11] = spaces[18]; 
        movementPathForPlayerIndex[1][12] = spaces[19]; 
        movementPathForPlayerIndex[1][13] = spaces[16]; 

        /* instantiate player one and player two objects */
        for (int playerOneIndex = 0; playerOneIndex < playerPieces[0].length; playerOneIndex++)
        {
            int playerOneXCoords = (int)(0.3 * WIDTH)+36;
            int playerOneYCoords = playerOneStartYCoords[playerOneIndex];
            playerPieces[0][playerOneIndex] = new PlayerPiece(0, playerOneXCoords, playerOneYCoords);
            addObject(playerPieces[0][playerOneIndex], playerOneXCoords, playerOneYCoords);
        } // end for
        for (int playerTwoIndex = 0; playerTwoIndex < playerPieces[1].length; playerTwoIndex++)
        {
            int playerTwoXCoords = (int)(0.75 * WIDTH);
            int playerTwoYCoords = playerTwoStartYCoords[playerTwoIndex];
            playerPieces[1][playerTwoIndex] = new PlayerPiece(1, playerTwoXCoords, playerTwoYCoords);
            addObject(playerPieces[1][playerTwoIndex], playerTwoXCoords, playerTwoYCoords);
        } // end for

        // instantiate the die and place it in the mid-right portion of the board
        die = new Die();
        addObject(die, (int)(0.8 * WIDTH), (int)(0.5 * HEIGHT)); 

        // update GameBoard state to begin actual gameplay
        state = PLAYER1_ROLL_DIE;
    } // end method prepare

    /**
     * Depending on the game's state, determines what the GameBoard does during
     * each frame or cycle of the `act` method
     */
    public void act()
    {
        switch (state) {
            case PLAYER1_ROLL_DIE:
                determineDieRollValueForPlayerIndex(0);
                break;

            case PLAYER1_MOVE:
                determineWhichPiecesAreMoveableForPlayerIndex(0); 
                if (!readyToExitState) {
                    determineMoveForPlayerIndex(0);
                    return; // exits act method because player 1 hasn't selected a piece to move yet
                } // end if
                makeAllPiecesMoveableAgainForPlayerIndex(0);
                updateGameStateAfterTurnForPlayerIndex(0);  
                break;

            case PLAYER2_ROLL_DIE:
                determineDieRollValueForPlayerIndex(1); 
                break;

            case PLAYER2_MOVE:
                determineWhichPiecesAreMoveableForPlayerIndex(1);
                if (!readyToExitState) {
                    determineMoveForPlayerIndex(1);
                    return; // exits act method because player 2 hasn't selected a piece to move yet
                } // end if
                makeAllPiecesMoveableAgainForPlayerIndex(1);
                updateGameStateAfterTurnForPlayerIndex(1);
                break;

            case PLAYER1_WIN:
                showText("\nPlayer 1\nWINS!!", die.getX(), die.getY() + DIE_TEXT_VERTICAL_OFFSET);
                Greenfoot.stop();
                break;

            case PLAYER2_WIN:
                showText("\nPlayer 2\nWINS!!", die.getX(), die.getY() + DIE_TEXT_VERTICAL_OFFSET);
                Greenfoot.stop();
                break;

            default:
                break;
        } // end switch
    } // end method act

    /**
     * Determines the die roll for the current player. If the player is human,
     * then the human player clicks on the die object to roll the die; otherwise,
     * the die is automatically rolled by the CPU.
     * 
     * @param playerIndex  the index of the player rolling the die
     */
    public void determineDieRollValueForPlayerIndex(int playerIndex)
    {
        String playerNumberString = (playerIndex == 0 ? "1" : "2");

        if (playerIsHumanForPlayerIndex[playerIndex] && !Greenfoot.mouseClicked(die))
        {
            showText("\nPlayer " + playerNumberString + "\nclick to roll", die.getX(), die.getY() + DIE_TEXT_VERTICAL_OFFSET);
            return; // skip remaining statements and return to this method's caller
        } // end if

        dieRollValue = Greenfoot.getRandomNumber(4); // rolls a 0 to 3, inclusively

        showText("\nPlayer " + playerNumberString + "\nrolls a " + dieRollValue, die.getX(), die.getY() + DIE_TEXT_VERTICAL_OFFSET);

        Greenfoot.delay(DELAY_LENGTH); // allow time to view on-screen message

        if (dieRollValue == 0) 
        {
            showText("\nSkipping\nturn...", die.getX(), die.getY() + DIE_TEXT_VERTICAL_OFFSET);
            Greenfoot.delay(DELAY_LENGTH); // allow time to view on-screen message
            state = (playerIndex == 0 ? PLAYER2_ROLL_DIE : PLAYER1_ROLL_DIE);
        } 
        else {
            state = (playerIndex == 0 ? PLAYER1_MOVE : PLAYER2_MOVE);
        } // end if/else
    } // end method determineDieRollValueForPlayerIndex

    /**
     * Routine for determining which player one pieces are moveable
     * 
     * @param playerIndex   index of the player whose turn is currently active
     */
    public void determineWhichPiecesAreMoveableForPlayerIndex(int playerIndex) {
        /* check to see which of this player's pieces are moveable for the given die roll value */
        for (int playerPieceIndex = 0; playerPieceIndex < playerPieces[playerIndex].length; playerPieceIndex++) 
        {    
            // determine index of opposing player using simple arithmetic
            int opposingPlayerIndex = 1 - playerIndex; // if playerIndex = 1, opposingPlayerIndex = 1 - 1 = 0
            // if playerIndex = 0, opposingPlayerIndex = 1 - 0 = 1

            playerPieces[playerIndex][playerPieceIndex].setMoveable(false); // default state; update depending on conditions to be checked below

            // call a getter method to retrieve the player piece's current position on the game board 
            int currentPlayerPieceGameBoardLocationIndex = 
                playerPieces[playerIndex][playerPieceIndex].getGameBoardLocationIndex();

            // look ahead by die roll value to determine target array index for this piece
            int playerPieceTargetGameBoardLocationIndex = currentPlayerPieceGameBoardLocationIndex + dieRollValue;

            // if the player piece is already currently in the goal zone, then it shouldn't be moveable
            if (currentPlayerPieceGameBoardLocationIndex == 14)
            {
                playerPieces[playerIndex][playerPieceIndex].setMoveable(false);
                continue; // we know this piece is NOT moveable so we can skip
            } // end if

            // if the target space is the goal, then this piece is moveable
            // and we call a setter method to update the target space location index 
            if (playerPieceTargetGameBoardLocationIndex == 14) 
            {
                playerPieces[playerIndex][playerPieceIndex].setMoveable(true);
                playerPieces[playerIndex][playerPieceIndex].setTargetGameBoardLocationIndex(playerPieceTargetGameBoardLocationIndex);
                continue; // we know this piece is moveable and we know it's not in the start zone, so we can skip
            } // end if

            // if the target space for this piece is beyond the goal, then this piece is not moveable
            if (playerPieceTargetGameBoardLocationIndex > 14) 
            {
                playerPieces[playerIndex][playerPieceIndex].setMoveable(false);
                continue; // we know this piece is NOT moveable, so we can skip 
            } // end if

            // check to see if the target space is occupied by one of the current player's pieces
            if (movementPathForPlayerIndex[playerIndex][playerPieceTargetGameBoardLocationIndex].isOccupiedByPieceForPlayerIndex(playerIndex)) {
                playerPieces[playerIndex][playerPieceIndex].setMoveable(false);
                continue; // we know this piece is NOT moveable, so we can skip
            } // end if

            // Check to see if the target space meets both of these conditions:
            // 1) is occupied by one of the OPPOSING player's pieces
            // AND
            // 2) is a safe space
            if (movementPathForPlayerIndex[playerIndex][playerPieceTargetGameBoardLocationIndex].isOccupiedByPieceForPlayerIndex(opposingPlayerIndex)  
            && movementPathForPlayerIndex[playerIndex][playerPieceTargetGameBoardLocationIndex].isSafeSpace())
            {
                playerPieces[playerIndex][playerPieceIndex].setMoveable(false);
                continue; // we know this piece is NOT moveable, so we can skip
            } // end if

            // Check to see if the target space meets both of these conditions:
            // 1) is occupied by one of the CURRENT player's pieces
            // AND
            // 2) is a safe space
            if (movementPathForPlayerIndex[playerIndex][playerPieceTargetGameBoardLocationIndex].isOccupiedByPieceForPlayerIndex(playerIndex)  
            && movementPathForPlayerIndex[playerIndex][playerPieceTargetGameBoardLocationIndex].isSafeSpace())
            {
                playerPieces[playerIndex][playerPieceIndex].setMoveable(false);
                continue; // we know this piece is NOT moveable, so we can skip
            } // end if

            // Check to see if the target space meets both of these conditions:
            // 1) is occupied by one of the OPPOSING player's pieces
            // AND
            // 2) is NOT a safe space
            if (movementPathForPlayerIndex[playerIndex][playerPieceTargetGameBoardLocationIndex].isOccupiedByPieceForPlayerIndex(opposingPlayerIndex)  
            && !movementPathForPlayerIndex[playerIndex][playerPieceTargetGameBoardLocationIndex].isSafeSpace())
            {
                playerPieces[playerIndex][playerPieceIndex].setMoveable(true);
                playerPieces[playerIndex][playerPieceIndex].setTargetGameBoardLocationIndex(playerPieceTargetGameBoardLocationIndex);
            } // end OUTER if

            // if the target space for this piece is a safe space, then this piece is moveable
            if (movementPathForPlayerIndex[playerIndex][playerPieceTargetGameBoardLocationIndex].isSafeSpace()) 
            {
                playerPieces[playerIndex][playerPieceIndex].setMoveable(true);
                playerPieces[playerIndex][playerPieceIndex].setTargetGameBoardLocationIndex(playerPieceTargetGameBoardLocationIndex);
            } // end OUTER if

            // if we made it this far, we assume that nothing is preventing this piece from being moveable
            playerPieces[playerIndex][playerPieceIndex].setMoveable(true);
            playerPieces[playerIndex][playerPieceIndex].setTargetGameBoardLocationIndex(playerPieceTargetGameBoardLocationIndex);
        } // end for
    } // end method determineWhichPiecesAreMoveableForPlayerIndex

    /**
     * Determines which of the player's moveable pieces will actually be moved
     * 
     * @param playerIndex   the index of the player currently moving 
     */
    public void determineMoveForPlayerIndex(int playerIndex)
    {
        int countOfPlayerPiecesThatAreNotMoveable = 0; // local counter

        // check to see if there are actually any moves to make
        for (int playerPieceIndex = 0; playerPieceIndex < NUMBER_OF_PIECES_PER_PLAYER; playerPieceIndex++)
        {
            PlayerPiece currentPlayerPieceToCheck = playerPieces[playerIndex][playerPieceIndex];
            if (!currentPlayerPieceToCheck.isMoveable()) 
            {
                countOfPlayerPiecesThatAreNotMoveable++;   
                if (countOfPlayerPiecesThatAreNotMoveable == NUMBER_OF_PIECES_PER_PLAYER)
                {
                    showText("No moves!", die.getX(), die.getY() + DIE_TEXT_VERTICAL_OFFSET);
                    Greenfoot.delay(DELAY_LENGTH); 
                    readyToExitState = true;
                    return; // terminate method and return to method caller
                } // end INNER if
            } // end OUTER if 
        } // end for

        // loop again through the moveable pieces to see which will be moved
        for (int playerPieceIndex = 0; playerPieceIndex < NUMBER_OF_PIECES_PER_PLAYER; playerPieceIndex++)
        {
            PlayerPiece currentPlayerPieceToCheck = playerPieces[playerIndex][playerPieceIndex];

            if (currentPlayerPieceToCheck.isMoveable())
            {
                showText("\n\n\nSelect\n" + "piece" + "\nto move\n" + dieRollValue + (dieRollValue == 1 ? " space" : " spaces"), 
                    die.getX(), die.getY() + DIE_TEXT_VERTICAL_OFFSET);

                if (playerIsHumanForPlayerIndex[playerIndex] && Greenfoot.mouseClicked(currentPlayerPieceToCheck)) 
                {                                                                          
                    handleSelectedPieceForPlayerIndex(playerIndex, currentPlayerPieceToCheck);
                    readyToExitState = true; // now that a piece is selected by human, the game will update its state 
                    return; // move has been made, so we exit the method early and return to method caller
                } // end if 

                // if we've gotten to this point in the code, then we allow the CPU to determine which piece to move
                if (!playerIsHumanForPlayerIndex[playerIndex] && Greenfoot.getRandomNumber(100) < 30) 
                {
                    Greenfoot.delay(DELAY_LENGTH); // allows time to see which board pieces are moveable or not

                    showText("\nMoving\n" + "piece" + " " + (playerPieceIndex + 1), die.getX(), die.getY() + DIE_TEXT_VERTICAL_OFFSET);
                    Greenfoot.delay(DELAY_LENGTH); // allows time to view message

                    handleSelectedPieceForPlayerIndex(playerIndex, currentPlayerPieceToCheck);

                    readyToExitState = true; // now that a piece is selected by CPU, the game will update its state 
                    return; // move has been made, so we exit the method early and return to method caller

                } // end INNER if 
            } // end OUTER if
        } // end for

    } // end method determineMoveForPlayerIndex

    /**
     * updates the given player piece's location index along the movement path for the given playerIndex
     * 
     * @param playerIndex           the index of the player moving the selected piece
     * @param selectedPlayerPiece   a reference to the player's selected piece
     */
    public void handleSelectedPieceForPlayerIndex(int playerIndex, PlayerPiece selectedPlayerPiece)
    {
        if (selectedPlayerPiece.getTargetGameBoardLocationIndex() >= 0 
        && selectedPlayerPiece.getTargetGameBoardLocationIndex() < 14) {
            moveSelectedPieceOntoTargetSpaceForPlayerIndex(playerIndex, selectedPlayerPiece);
        } else if (selectedPlayerPiece.getTargetGameBoardLocationIndex() == 14) { 
            moveSelectedPieceIntoGoalZoneForPlayerIndex(playerIndex, selectedPlayerPiece);
        } // end if/else

        // update the status of the Space it is leaving so that it is no longer occupied by that player
        if (selectedPlayerPiece.getGameBoardLocationIndex() >= 0) {
            movementPathForPlayerIndex[playerIndex][selectedPlayerPiece.getGameBoardLocationIndex()].setOccupiedByPieceForPlayerIndex(playerIndex, false);
            /* if statement to restrict echoes to the middle of the board */
            if ((selectedPlayerPiece.getGameBoardLocationIndex() >= 4) && (selectedPlayerPiece.getGameBoardLocationIndex() <= 11)) {
                movementPathForPlayerIndex[playerIndex][selectedPlayerPiece.getGameBoardLocationIndex()].setEchoSpace(true);
                movementPathForPlayerIndex[playerIndex][selectedPlayerPiece.getGameBoardLocationIndex()].toggleImageType(1);
            } // end if
        } // end if

        // update the selected player piece's Space location to be whatever its target location is 
        selectedPlayerPiece.setGameBoardLocationIndex(selectedPlayerPiece.getTargetGameBoardLocationIndex());

        if (selectedPlayerPiece.getTargetGameBoardLocationIndex()<=13) {
            // check if the target space is an echo space
            if (movementPathForPlayerIndex[playerIndex][selectedPlayerPiece.getTargetGameBoardLocationIndex()].isEchoSpace()) {
                resolveEchoRoll(playerIndex, selectedPlayerPiece);
                movementPathForPlayerIndex[playerIndex][selectedPlayerPiece.getTargetGameBoardLocationIndex()].setEchoSpace(false); // clear the echo space
            } // end if
        } // end if
    } // end method handleSelectedPieceForPlayerIndex

    /**
     * helper method (called by handleSelectedPieceForPlayerIndex) for moving a
     * piece into a target space that is not the goal
     * 
     * @param playerIndex           the index of the player moving a piece into the piece's target space 
     * @param selectedPlayerPiece   a reference to the player's selected piece
     */
    public void moveSelectedPieceOntoTargetSpaceForPlayerIndex(int playerIndex, PlayerPiece selectedPlayerPiece)
    {
        // determine index of opposing player using simple arithmetic (no if-statement needed!)
        int opposingPlayerIndex = 1 - playerIndex; // if playerIndex = 1, opposingPlayerIndex = 1 - 1 = 0
        // if playerIndex = 0, opposingPlayerIndex = 1 - 0 = 1

        // move the playerPiece sprite to its new X and Y locations on the screen
        selectedPlayerPiece.setLocation(
            movementPathForPlayerIndex[playerIndex][selectedPlayerPiece.getTargetGameBoardLocationIndex()].getX(), 
            movementPathForPlayerIndex[playerIndex][selectedPlayerPiece.getTargetGameBoardLocationIndex()].getY());

        // update the occupied state for the target space where the selected player piece is being moved
        movementPathForPlayerIndex[playerIndex][selectedPlayerPiece.getTargetGameBoardLocationIndex()].setOccupiedByPieceForPlayerIndex(playerIndex, true);

        // if the target space is a safe space, mark for rolling again when the turn is over
        if (movementPathForPlayerIndex[playerIndex][selectedPlayerPiece.getTargetGameBoardLocationIndex()].isSafeSpace()) {
            /* reroll again is cancelled out if echo space is on the safe space */
            if (movementPathForPlayerIndex[playerIndex][selectedPlayerPiece.getTargetGameBoardLocationIndex()].isEchoSpace() == false) {
                onSafeSpaceSoRollAgain = true;
            } // end if
        } // end if

        // if this space is occupied by a piece belonging to the opposing player, 
        // move the opposing playerPiece at this space back to the beginning
        if (movementPathForPlayerIndex[playerIndex][selectedPlayerPiece.getTargetGameBoardLocationIndex()].isOccupiedByPieceForPlayerIndex(opposingPlayerIndex)) {
            resetCapturedPieceOnBoardAndReplaceWithSelectedPieceForPlayerIndex(playerIndex, selectedPlayerPiece);
        } // end if

    } // end method moveSelectedPieceOntoTargetSpaceForPlayerIndex

    /**
     * "Helper" method (here called by `moveSelectedPieceOntoTargetSpaceForPlayerIndex`) 
     * for handling the capture of an opposing player's piece 
     * 
     * @param playerIndex           the index of the player moving a piece into that piece's target space 
     * @param selectedPlayerPiece   a reference to the player's selected piece
     */
    public void resetCapturedPieceOnBoardAndReplaceWithSelectedPieceForPlayerIndex(int playerIndex, PlayerPiece selectedPlayerPiece) {
        // determine index of opposing player using simple arithmetic
        int opposingPlayerIndex = 1 - playerIndex;

        for (PlayerPiece currentOpponentPieceToCheck : playerPieces[opposingPlayerIndex]) {

            // if the captured opponent's piece is located along its board space 
            // movement path then move it back to location index -1 (starting zone)
            // and move its sprite back to its original X and Y coordinate locations in the world
            if (currentOpponentPieceToCheck.getGameBoardLocationIndex() == selectedPlayerPiece.getTargetGameBoardLocationIndex())  {
                currentOpponentPieceToCheck.setLocation(currentOpponentPieceToCheck.getOriginalXcoord(), currentOpponentPieceToCheck.getOriginalYcoord());

                currentOpponentPieceToCheck.setGameBoardLocationIndex(-1); // reset piece's location index to -1

                // at the current player's TARGET location index along the movement path, update the state of that Space
                // so that it is NO LONGER OCCUPIED by the (captured) opposing player's piece
                movementPathForPlayerIndex[playerIndex][selectedPlayerPiece.getTargetGameBoardLocationIndex()].setOccupiedByPieceForPlayerIndex(opposingPlayerIndex, false);
            } // end if
        } // end for    
    } // end method resetCapturedPieceOnBoardAndReplaceWithSelectedPieceForPlayerIndex

    /**
     * "Helper" method (here called by handleSelectedPieceForPlayerIndex) to move 
     * the selected playerPiece's SPRITE into the goal zone.
     * Note that this only moves the player piece's SPRITE; the player piece's 
     * board location index is updated elsewhere (can you figure out where?)
     * 
     * Actual X and Y coordinates of each player's piece in the goal zone are each  
     * computed as a linear function of how many of that player's pieces are already 
     * in the goal zone (i.e., goalCountForPlayer[playerIndex])
     * 
     * @param playerIndex           the index of the player moving a piece into the goal zone
     * @param selectedPlayerPiece   a reference to the player's selected piece
     */
    public void moveSelectedPieceIntoGoalZoneForPlayerIndex(int playerIndex, PlayerPiece selectedPlayerPiece)
    {
        if (playerIndex == 0) {
            selectedPlayerPiece.setLocation((int)((0.3 - 0.05*goalCountForPlayerIndex[0]+0.025) * WIDTH), HEIGHT - 80);
        } else { 
            selectedPlayerPiece.setLocation((int)((0.75 + 0.05*goalCountForPlayerIndex[1]-0.025) * WIDTH), HEIGHT - 80);
        } // end if/else        
    } // end method moveSelectedPieceIntoGoalZoneForPlayerIndex

    /**
     * "Turns on" (makes moveable) all of pieces for the given player (specified by `playerIndex`) 
     * at the conclusion of that player's turn
     * 
     * @param playerIndex   the index of the player that is completing their turn
     */
    public void makeAllPiecesMoveableAgainForPlayerIndex(int playerIndex)
    {
        for (PlayerPiece currentPlayerPieceToCheck : playerPieces[playerIndex]) {
            currentPlayerPieceToCheck.setMoveable(true);
        } // end for
    } // end method makeAllPiecesMoveableAgainForPlayerIndex

    /**
     * Updates the game state (and checks for a possible win condition) after the player
     * (specified by `playerIndex`) has just completed their turn
     * 
     * @param playerIndex   the index of the player that has just completed their turn
     */
    public void updateGameStateAfterTurnForPlayerIndex(int playerIndex)
    {
        switch (playerIndex) {
            case 0: // player 1
                updateGoalCountForPlayerIndex(0);

                if (goalCountForPlayerIndex[0] == NUMBER_OF_PIECES_PER_PLAYER) {
                    state = PLAYER1_WIN; // update state for next act method call
                } else if (onSafeSpaceSoRollAgain) {
                    showText("\nPlayer 1\nrolls again!", die.getX(), die.getY() + DIE_TEXT_VERTICAL_OFFSET);
                    onSafeSpaceSoRollAgain = false; // reset for next turn
                    Greenfoot.delay(DELAY_LENGTH);
                    readyToExitState = false; // reset for next turn
                    state = PLAYER1_ROLL_DIE; // update state for next act method call

                } else { 
                    // player 1 (index 0)'s turn is finished, so update game state for player 2's turn
                    showText("\nPlayer 2\nup next", die.getX(), die.getY() + DIE_TEXT_VERTICAL_OFFSET);
                    Greenfoot.delay(DELAY_LENGTH);
                    readyToExitState = false; // reset for next turn
                    state = PLAYER2_ROLL_DIE; // update state for next act method call

                } // end multi-way if/else
                break;

            case 1: // player 2
                updateGoalCountForPlayerIndex(1);

                if (goalCountForPlayerIndex[1] == NUMBER_OF_PIECES_PER_PLAYER) {
                    state = PLAYER2_WIN; // update state for next act method call
                } else if (onSafeSpaceSoRollAgain) {
                    showText("\nPlayer 2\nrolls again!", die.getX(), die.getY() + DIE_TEXT_VERTICAL_OFFSET);
                    onSafeSpaceSoRollAgain = false; // reset for next turn
                    Greenfoot.delay(DELAY_LENGTH);
                    readyToExitState = false; // reset for next turn
                    state = PLAYER2_ROLL_DIE; // update state for next act method call
                } else { 
                    // player 1 (index 0)'s turn is finished, so update game state for player 2's turn
                    showText("\nPlayer 1\nup next", die.getX(), die.getY() + DIE_TEXT_VERTICAL_OFFSET);
                    Greenfoot.delay(DELAY_LENGTH);
                    readyToExitState = false; // reset for next turn
                    state = PLAYER1_ROLL_DIE; // update state for next act method call
                } // end multi-way if/else
                break;

            default:
                break;

        } // end switch

    } // end method updateGameStateAfterTurnForPlayerIndex

    /**
     * For the given playerIndex, checks to see how many of that player's pieces have been 
     * moved into that goal zone at the end of that player's movement path
     * 
     * @param playerIndex   the index of the player whose pieces are being checked to see if they're in the goal zone
     */
    public void updateGoalCountForPlayerIndex(int playerIndex)
    {
        goalCountForPlayerIndex[playerIndex] = 0;

        for (PlayerPiece currentPlayerPieceToCheck : playerPieces[playerIndex])
        {
            if (currentPlayerPieceToCheck.getGameBoardLocationIndex() == 14) {
                goalCountForPlayerIndex[playerIndex]++;
            } // end if
        } // end enhanced for loop 
    } // end method updateGoalCountForPlayerIndex

    /**
     * Helper method for resolving echo roll. Adds event objects when echo rolls happen.
     * 
     * @param playerIndex   index of current player
     * @param piece         player piece
     */
    private void resolveEchoRoll(int playerIndex, PlayerPiece selectedPlayerPiece) {
        // determine index of opposing player using simple arithmetic (no if-statement needed!)
        int opposingPlayerIndex = 1 - playerIndex; // if playerIndex = 1, opposingPlayerIndex = 1 - 1 = 0
        // if playerIndex = 0, opposingPlayerIndex = 1 - 0 = 1

        Event positiveEvent = eventArray[(int)(Greenfoot.getRandomNumber(4))];
        Event negativeEvent = eventArray[(int)(Greenfoot.getRandomNumber(4)+4)];

        // generate an echo roll value from -3 to 3 (excluding 0)
        int echoRollValue = (int)(Greenfoot.getRandomNumber(ECHO_MAGNITUDE*2) - ECHO_MAGNITUDE);
        if (echoRollValue >= 0) {
            echoRollValue++; // Skip zero
        } // end if

        showText("\nEcho Roll: " + echoRollValue, die.getX(), die.getY() + DIE_TEXT_VERTICAL_OFFSET);
        Greenfoot.delay(DELAY_LENGTH);

        int currentLocation = selectedPlayerPiece.getGameBoardLocationIndex();
        int newLocation = currentLocation + echoRollValue;

        // check if the piece is landing on the echo space (middle space)
        if (currentLocation == 7) {
            if (echoRollValue > 0) {
                // move to the goal zone
                addObject(positiveEvent, 550, 375);
                Greenfoot.delay(DELAY_LENGTH*8);
                removeObject(positiveEvent);
                movementPathForPlayerIndex[playerIndex][currentLocation].toggleImageType(2);
                movementPathForPlayerIndex[playerIndex][currentLocation].setEchoSpace(false);
                moveSelectedPieceIntoGoalZoneForPlayerIndex(playerIndex, selectedPlayerPiece);
                showText("\nEcho sends piece to goal zone!", die.getX(), die.getY() + DIE_TEXT_VERTICAL_OFFSET);
            } else {    
                // move back to the starting zone
                addObject(negativeEvent, 550, 375);
                Greenfoot.delay(DELAY_LENGTH*8);
                removeObject(negativeEvent);
                movementPathForPlayerIndex[playerIndex][currentLocation].toggleImageType(2);
                movementPathForPlayerIndex[playerIndex][currentLocation].setEchoSpace(false);
                selectedPlayerPiece.setLocation(selectedPlayerPiece.getOriginalXcoord(), selectedPlayerPiece.getOriginalYcoord());
                selectedPlayerPiece.setGameBoardLocationIndex(-1); // Reset piece's location index to -1
                showText("\nEcho sends piece back to starting zone!", die.getX(), die.getY() + DIE_TEXT_VERTICAL_OFFSET);
            }
            return; // exit the method after handling the echo space
        }

        selectedPlayerPiece.setTargetGameBoardLocationIndex(newLocation);

        /* if echo roll target space (newLocation) is occupied by friendly piece, selectedPlayerPiece will not move */
        if (!(movementPathForPlayerIndex[playerIndex][newLocation].isOccupiedByPieceForPlayerIndex(playerIndex))
        && !(movementPathForPlayerIndex[playerIndex][newLocation].isSafeSpace()
            && movementPathForPlayerIndex[playerIndex][newLocation].isOccupiedByPieceForPlayerIndex(opposingPlayerIndex))) {
            if (newLocation >= 0 && newLocation < 14) {
                // move the piece to the new location
                movementPathForPlayerIndex[playerIndex][currentLocation].setOccupiedByPieceForPlayerIndex(playerIndex, false);
                selectedPlayerPiece.setLocation(
                    movementPathForPlayerIndex[playerIndex][newLocation].getX(),
                    movementPathForPlayerIndex[playerIndex][newLocation].getY());

                // turn echo space into normal space
                movementPathForPlayerIndex[playerIndex][currentLocation].toggleImageType(2);
                movementPathForPlayerIndex[playerIndex][currentLocation].setEchoSpace(false);

                // move the piece to the new index
                selectedPlayerPiece.setGameBoardLocationIndex(newLocation);
                selectedPlayerPiece.setTargetGameBoardLocationIndex(newLocation);
                movementPathForPlayerIndex[playerIndex][newLocation].setOccupiedByPieceForPlayerIndex(playerIndex, true);

                // take enemy piece if landed on
                if (movementPathForPlayerIndex[playerIndex][newLocation].isOccupiedByPieceForPlayerIndex(opposingPlayerIndex)) {
                    resetCapturedPieceOnBoardAndReplaceWithSelectedPieceForPlayerIndex(playerIndex, selectedPlayerPiece);
                } // end if

                if (echoRollValue > 0) {
                    addObject(positiveEvent, 550, 375);
                    Greenfoot.delay(DELAY_LENGTH*8);
                    removeObject(positiveEvent);
                } else {
                    addObject(negativeEvent, 550, 375);
                    Greenfoot.delay(DELAY_LENGTH*8);
                    removeObject(negativeEvent);
                }

                showText("\nEcho moves piece\n" + echoRollValue + " spaces", die.getX(), die.getY() + DIE_TEXT_VERTICAL_OFFSET);
            } else {
                // move the piece to the last space if the echo wants to send it to goal zone
                movementPathForPlayerIndex[playerIndex][currentLocation].setOccupiedByPieceForPlayerIndex(playerIndex, false);
                selectedPlayerPiece.setLocation(
                    movementPathForPlayerIndex[playerIndex][13].getX(),
                    movementPathForPlayerIndex[playerIndex][13].getY());

                // turn echo space into normal space
                movementPathForPlayerIndex[playerIndex][currentLocation].toggleImageType(2);
                movementPathForPlayerIndex[playerIndex][currentLocation].setEchoSpace(false);

                // move the piece to the last index
                selectedPlayerPiece.setGameBoardLocationIndex(13);
                movementPathForPlayerIndex[playerIndex][13].setOccupiedByPieceForPlayerIndex(playerIndex, true);

                addObject(positiveEvent, 550, 375);
                Greenfoot.delay(DELAY_LENGTH*8);
                removeObject(positiveEvent);

                showText("\nEcho moves piece\n" + echoRollValue + " spaces", die.getX(), die.getY() + DIE_TEXT_VERTICAL_OFFSET);
            }
        } else {
            /* handle newLocation being occupied by a friendly piece by not moving */
            movementPathForPlayerIndex[playerIndex][currentLocation].toggleImageType(2);
            movementPathForPlayerIndex[playerIndex][currentLocation].setEchoSpace(false);
            showText("\nEcho roll space \nobstructed!", die.getX(), die.getY() + DIE_TEXT_VERTICAL_OFFSET);
        }

        Greenfoot.delay(DELAY_LENGTH);
    } // end method resolveEchoRoll
} // end class GameBoard
 