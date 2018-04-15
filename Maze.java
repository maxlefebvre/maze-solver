/**
 * Maximillian Lefebvre
 * 101033745
 */
import javafx.geometry.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Maze {
    private static final byte  OPEN = 0;
    private static final byte  WALL = 1;
    private static final byte  VISITED = 2;

    private int         rows, columns;
    private byte[][]    grid;

    // A constructor that makes a maze of the given size
    public Maze(int r, int c) {
        rows = r;
        columns = c;
        grid = new byte[r][c];
        for(r=0; r<rows; r++) {
            for (c = 0; c<columns; c++) {
                grid[r][c] = WALL;
            }
        }
    }

    public int getRows() { return rows; }
    public int getColumns() { return columns; }

    // Return true if a wall is at the given location, otherwise false
    public boolean wallAt(int r, int c) {
        return grid[r][c] == WALL;
    }

    // Return true if this location has been visited, otherwise false
    public boolean visitedAt(int r, int c) {
        return grid[r][c] == VISITED;
    }

    // Put a visit marker at the given location
    public void placeVisitAt(int r, int c) {
        grid[r][c] = VISITED;
    }

    // Remove a visit marker from the given location
    public void removeVisitAt(int r, int c) {
        grid[r][c] = OPEN;
    }

    // Put a wall at the given location
    public void placeWallAt(int r, int c) {
        grid[r][c] = WALL;
    }

    // Remove a wall from the given location
    public void removeWallAt(int r, int c) {
        grid[r][c] = 0;
    }

    // Carve out a maze
    public void carve() {
        int startRow = (int)(Math.random()*(rows-2))+1;
        int startCol = (int)(Math.random()*(columns-2))+1;
        carve(startRow, startCol);
    }

    // Directly recursive method to carve out the maze
    public void carve(int r, int c) {
        ArrayList<Integer> rowOffsets = new ArrayList<Integer>(Arrays.asList(-1, 1, 0, 0));
        ArrayList<Integer> colOffsets = new ArrayList<Integer>(Arrays.asList(0, 0, -1, 1));
        Random random = new Random();

        // If location in on border
        if ( r == 0 || r == rows-1 || c == 0 || c == columns-1){
            // Don't remove wall
            return;
        }
        else if (!wallAt(r,c)){
            // Do nothing
            return;
        }
        else {
            // Remove the wall and carve maze in one of the 4 directions around location
            int adjWalls = 0;
            if (wallAt(r+1,c))
                adjWalls++;
            if (wallAt(r,c+1))
                adjWalls++;
            if (wallAt(r-1,c))
                adjWalls++;
            if (wallAt(r,c-1))
                adjWalls++;

            if (adjWalls>=3) {
                removeWallAt(r, c);
                for (int j=0; j<4;j++) {
                    int i = random.nextInt(rowOffsets.size());
                    carve(r + rowOffsets.get(i), c + colOffsets.get(i));
                    rowOffsets.remove(i);
                    colOffsets.remove(i);
                }
            }
        }
    }

    // Determine the longest path in the maze from the given start location
    public ArrayList<Point2D> longestPath() {
        // Find the longest path
        ArrayList<Point2D> longestPath = longestPathFrom(1,1);
        for(int x=0;x<columns;x++){
            for (int y=0;y<rows;y++){
                if(longestPathFrom(y,x).size() > longestPath.size())
                    longestPath = longestPathFrom(y,x);
            }
        }
        return longestPath;
    }

    // Determine the longest path in the maze from the given start location
    public ArrayList<Point2D> longestPathFrom(int r, int c) {
        ArrayList<Point2D> path = new ArrayList<Point2D>();

        // Create individual array lists for path that goes up,down,right and left
        ArrayList<Point2D> pathOne = new ArrayList<Point2D>();
        ArrayList<Point2D> pathTwo = new ArrayList<Point2D>();
        ArrayList<Point2D> pathThree = new ArrayList<Point2D>();
        ArrayList<Point2D> pathFour = new ArrayList<Point2D>();

        // Stop if path has a wall or is visited at current space
        if (wallAt(r,c) || visitedAt(r,c))
            return path;

        placeVisitAt(r,c);
        path.add(new Point2D(r,c));

        // Call function on spaces in all directions
        pathOne.addAll(longestPathFrom(r,c-1));
        pathTwo.addAll(longestPathFrom(r+1,c));
        pathThree.addAll(longestPathFrom(r,c+1));
        pathFour.addAll(longestPathFrom(r-1,c));

        // Add the longest path to the main path array list
        if (pathOne.size() > pathTwo.size() && pathOne.size() > pathThree.size() && pathOne.size() > pathFour.size())
            path.addAll(pathOne);
        if (pathTwo.size() > pathOne.size() && pathTwo.size() > pathThree.size() && pathTwo.size() > pathFour.size())
            path.addAll(pathTwo);
        if (pathThree.size() > pathOne.size() && pathThree.size() > pathTwo.size() && pathThree.size() > pathFour.size())
            path.addAll(pathThree);
        if (pathFour.size() > pathOne.size() && pathFour.size() > pathTwo.size() && pathFour.size() > pathThree.size())
            path.addAll(pathFour);

        removeVisitAt(r,c);
        return path;
    }
}

