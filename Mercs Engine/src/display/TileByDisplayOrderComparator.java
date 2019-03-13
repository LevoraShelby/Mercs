package display;

import java.util.Comparator;

import main.Tile;



/**
 * A comparator for `Tile` that orders each tiles based on when they are
 * printed in the process of printing out a board (oriented towards the white
 * player), where tiles that would be printed out before are ordered before
 * tiles that would be printed out after.
 * 
 * 
 * Example:
 * 
 * 1 2
 * 3 4
 * 
 * Tile 1 comes before tile 2, tile 2 before tile 3, and so on.
 * @author trevor
 */
public class TileByDisplayOrderComparator implements Comparator<Tile> {
	public int compare(Tile tile1, Tile tile2) {
		if(tile1.rank() < tile2.rank()) {
			return 1;
		}
		else if(tile1.rank() > tile2.rank()) {
			return -1;
		}
		else {
			if(tile1.file() < tile2.file()) {
				return -1;
			}
			else if(tile1.file() > tile2.file()) {
				return 1;
			}
			else {
				return 0;
			}
		}
	}
}
