import java.util.*;
import java.awt.*;
/**
 * Creates a four block shape.
 */
public class Tetrad
{
    private Block[] blocks;
    private MyBoundedGrid<Block> gr;
    private int rand;
    private final static Color[] BLOCK_COLORS = {   Color.RED, Color.GRAY, Color.CYAN,
                                                    Color.YELLOW, Color.MAGENTA,
                                                    Color.BLUE, Color.GREEN };

    /**
     * Constructor for Tetrad
     * Creates a four block shape
     * 
     * @param grid the grid where the tetrad is created
     */
    public Tetrad(MyBoundedGrid<Block> grid)
    {
        gr = grid;
        rand = (int)(Math.random()*7) + 1;
        blocks = new Block[4];
        for(int i = 0; i < 4; i++)
        {
            blocks[i] = new Block();
            blocks[i].setColor(BLOCK_COLORS[rand-1]);
        }
        Location[] locs = new Location[4];
        int col = grid.getNumCols();
        //I shape
        if (rand == 1)
        {
            locs[0] = new Location(1,col/2);
            //center block
            locs[1] = new Location(0,col/2);
            locs[2] = new Location(2,col/2);
            locs[3] = new Location(3,col/2);
        }
        //T shape
        else if (rand == 2)
        {
            locs[0] = new Location(0,col/2);
            //center block
            locs[1] = new Location(0,col/2-1);
            locs[2] = new Location(0,col/2+1);
            locs[3] = new Location(1,col/2);
        }
        //O shape
        else if (rand == 3)
        {
            locs[0] = new Location(0,col/2-1);
            locs[1] = new Location(1,col/2-1);
            locs[2] = new Location(0,col/2);
            locs[3] = new Location(1,col/2);
        }
        //L shape
        else if (rand == 4)
        {
            locs[0] = new Location(1,col/2);
            //center block
            locs[1] = new Location(0,col/2);
            locs[2] = new Location(2,col/2);
            locs[3] = new Location(2,col/2+1);
        }
        //J shape
        else if (rand == 5)
        {
            locs[0] = new Location(1,col/2);
            //center block
            locs[1] = new Location(0,col/2);
            locs[2] = new Location(2,col/2);
            locs[3] = new Location(2,col/2-1);
        }
        //S shape
        else if (rand == 6)
        {
            locs[0] = new Location(0,col/2);
            //center block
            locs[1] = new Location(1,col/2);
            locs[2] = new Location(1,col/2-1);
            locs[3] = new Location(0,col/2+1);
        }
        //Z shape
        else
        {
            locs[0] = new Location(0,col/2);
            //center block
            locs[1] = new Location(1,col/2);
            locs[2] = new Location(1,col/2+1);
            locs[3] = new Location(0,col/2-1);
        }
        addToLocations(grid,locs);
    }

    /**
     * Adds the tetrad to a location
     * 
     * @param grid the grid where the tetrad is placed
     * @param locs the locations where the blocks are placed
     */
    private void addToLocations(MyBoundedGrid<Block> grid, Location[] locs)
    {
        for (int i = 0; i < locs.length; i++)
        {
            blocks[i].putSelfInGrid(grid,locs[i]);
        }
    }

    /**
     * Removes the blocks
     * @return the locations of the removed blocks
     */
    private Location[] removeBlocks()
    {
        Location[] locs = new Location[4];
        for (int i = 0; i<4; i++)
        {
            locs[i] = blocks[i].getLocation();
            if (blocks[i] != null && gr != null)
                blocks[i].removeSelfFromGrid();
        }
        return locs;
    }

    /**
     * Checks whether the locations are empty
     * 
     * @param grid the grid where the blocks are placed
     * @param locs the locations where the blocks are placed
     * @return true if the location is placed; otherwise,
     *                                         false
     */
    private boolean areEmpty(MyBoundedGrid<Block> grid, Location[] locs)
    {
        for (int i = 0; i < locs.length; i++)
        {
            if (!grid.isValid(locs[i]) || grid.get(locs[i]) != null)
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Translates the blocks
     * 
     * @param deltaRow the number of spaces to move horizontally
     * @param deltaCol the number of spaces to move vertically
     * @return true if the blocks can translate; otherwise,
     *                                           false
     */
    public boolean translate(int deltaRow, int deltaCol)
    {
        if (gr == null)
            return false;
        Location[] oldLocs = removeBlocks();
        Location[] newLocs = new Location[4];
        for (int i = 0; i < 4; i++)
        {
            newLocs[i] = new Location(oldLocs[i].getRow() + deltaRow,
                                      oldLocs[i].getCol() + deltaCol);
        }
        if (areEmpty(gr, newLocs))
        {
            addToLocations(gr, newLocs);
            return true;
        }
        else
        {
            addToLocations(gr, oldLocs);
            return false;
        } 
    }

    /**
     * Rotates the block 90 degrees
     * 
     * @return true if the block was rotated; otherwise,
     *                                        false
     */
    public boolean rotate()
    {
        if (gr == null)
            return false;
        Location rotation = blocks[0].getLocation();
        int oldRow = rotation.getRow();
        int oldCol = rotation.getCol();
        Location[] oldLocs = removeBlocks();
        Location[] newLocs = new Location[4];
        newLocs[0] = rotation;
        if (rand == 3)
        {
            addToLocations(gr,oldLocs);
            return true;
        }
        else
        {
            for (int i = 1; i < 4; i++)
            {
                newLocs[i] = new Location(oldRow - oldCol + oldLocs[i].getCol(),
                    oldRow + oldCol - oldLocs[i].getRow());
            }
            if (areEmpty(gr, newLocs))
            {
                addToLocations(gr, newLocs);
                return true;
            }
            else
            {
                addToLocations(gr, oldLocs);
                return false;
            }
        }
    }
}
