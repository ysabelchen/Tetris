
/**
 * Creates new tetrads and keeps creating them until the game is over.
 */
public class Tetris implements ArrowListener
{
    private MyBoundedGrid<Block> grid;
    private BlockDisplay display;
    private Tetrad shape;
    private int clearedRows;
    private boolean ended;
    private boolean topRow;

    /**
     * Constructor for the Tetris class
     * Creates a new tetrad
     */
    public Tetris()
    {
        grid = new MyBoundedGrid<Block>(20,10);
        display = new BlockDisplay(grid);
        display.setTitle("Tetris");
        shape = new Tetrad(grid);
        display.showBlocks();
        display.setArrowListener(this);
        topRow = false;
    }

    /**
     * Rotates the tetrad 90 degrees
     */
    public void upPressed()
    {
        if (!ended)
        {
            shape.rotate();
            display.showBlocks();
        }
    }

    /**
     * Moves the tetrad down one
     */
    public void downPressed()
    {
        if (!ended)
        {
            shape.translate(1,0);
            display.showBlocks();
        }
    }

    /**
     * Moves the tetrad left one
     */
    public void leftPressed()
    {
        if (!ended)
        {
            shape.translate(0,-1);
            display.showBlocks();
        }
    }

    /**
     * Moves the tetrad right one
     */
    public void rightPressed()
    {
        if (!ended)
        {
            shape.translate(0,1);
            display.showBlocks();
        }
    }

    /**
     * Moves the tetrad all the way down
     */
    public void spacePressed()
    {
        if (!ended)
        {
            while (shape.translate(1,0))
            {
                downPressed();
                display.showBlocks();
            }
        }
    }

    /**
     * Keeps track of the score and pausing time. Checks if the tetrad can move down.
     * If it can't, then it clears the completed rows and increases the score. Then, if
     * the tetrad can't move down, the game ends.
     */
    public void play()
    {
        boolean status = true;
        int mils = 1000;
        int score = 0;
        while(status)
        {
            display.showBlocks();
            try
            {
                Thread.sleep(mils);
                if(!shape.translate(1,0))
                {
                    if (clearCompletedRows())
                    {
                        if (mils >= 500)
                        {
                            mils -= 50;
                        }
                        score += clearedRows;
                        display.setTitle("Score: " + score);
                    }
                    Location[] row1 = new Location[grid.getNumCols()];
                    for (int i = 0; i < row1.length; i++)
                    {
                        row1[i] = new Location(0, i);
                        if (grid.get(row1[i]) != null)
                        {
                            lose();
                            Thread.sleep(5000);
                            ended = true;
                            status = false;
                            System.exit(0);
                        }
                    }
                    Tetrad newTetrad = new Tetrad(grid);
                    shape = newTetrad;
                    display.showBlocks();
                }
            }
            catch(InterruptedException e)
            {
                //ignore
            }
        }
    }

    /**
     * Checks if the row is completed
     * 
     * @param row the row that is checked
     */
    private boolean isCompletedRow(int row)
    {
        for (int i = 0; i<grid.getNumCols(); i++)
        {
            Location loc = new Location(row, i);
            if (grid.get(loc) == null)
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Clears the completed row
     * 
     * @param row the row that is cleared
     */
    private void clearRow(int row)
    {
        for (int i = 0; i<grid.getNumCols(); i++)
        {
            Location loc = new Location(row, i);
            Block block = (Block)grid.get(loc);
            if (grid != null && block != null)
                block.removeSelfFromGrid();
        }
        for (int r = row-1; r >= 0; r--)
        {
            for (int c = 0; c<grid.getNumCols(); c++)
            {
                Location loc = new Location(r,c);
                Location nextLoc = new Location(r+1,c);
                Block b = grid.get(loc);
                if (b != null)
                {
                    b.moveTo(nextLoc);
                }
            }
        }
    }

    /**
     * Clears all completed rows
     */
    private boolean clearCompletedRows()
    {
        boolean status = false;
        int numRows = 0;
        for (int i = 0; i<grid.getNumRows(); i++)
        {
            if (isCompletedRow(i))
            {
                clearRow(i);
                numRows++;
                status = true;
            }
        }
        clearedRows = numRows;
        return status;
    }

    /**
     * Clears all rows and creates a sad face.
     */
    private void lose()
    {
        for (int r = 0; r<grid.getNumRows(); r++)
        {
            clearRow(r);
            //null pointer exception b/c clearRows but then pressed a key after clearing rows
        }
        Block block = new Block();
        Location[] locs = new Location[8];
        locs[0] = new Location(grid.getNumRows()/2-3, grid.getNumCols()/3);
        locs[1] = new Location(grid.getNumRows()/2-3, grid.getNumCols()/3*2-1);
        block.putSelfInGrid(grid,locs[0]);
        block.putSelfInGrid(grid,locs[1]);
        int i = 2;
        for (int c = grid.getNumCols()/3; c <= grid.getNumCols()/2; c++)
        {
            if (c == grid.getNumCols()/2)
            {
                i--;
            }
            locs[i] = new Location(grid.getNumRows()/2-i+3, c-1);
            locs[8-i] = new Location(grid.getNumRows()/2-i+3, grid.getNumCols()-c-1);
            block.putSelfInGrid(grid,locs[i]);
            block.putSelfInGrid(grid,locs[8-i]);
            i++;
        }
        display.showBlocks();
    }

    /**
     * Creates a game of tetris and plays
     * 
     * @param args array with information that may be passed at start of processing
     */
    public static void main(String[] args)
    {
        Tetris game = new Tetris();
        game.play();
    }
}