package console;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import main.Tile;



public class GamePrinter {
	public static String gameToString(
		Set<Tile> tiles,
		Map<Tile, String> tileToDisplay,
		String emptyTileDisplay
	) {

		List<Tile> sortedTiles = new ArrayList<>(tiles);
		sortedTiles.sort((tile1, tile2) -> {
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
				else {
					return 1;
				}
			}
		});

		String toString = "";
		int currentRank = sortedTiles.get(0).rank();
		int currentFile = sortedTiles.get(0).file();
		for(Tile tile : sortedTiles) {

			//Spacing
			if(currentRank != tile.rank()) {
				toString += stringToNewRank(currentRank, tile.rank());
				currentRank = tile.rank();	
				currentFile = 1;
			}
			else if(currentFile != tile.file()){
				toString += stringToNewFile(currentFile, tile.file());
				currentFile = tile.file();
			}

			//Content
			if(tileToDisplay.containsKey(tile)) {
				toString += tileToDisplay.get(tile);
			}
			else {
				toString += emptyTileDisplay;
			}
		}

		return toString;
	}


	public static void printGame(
		Set<Tile> tiles,
		Map<Tile, String> tileToDisplay,
		String emptyTileDisplay
	) {
		System.out.println(gameToString(
			tiles, tileToDisplay, emptyTileDisplay
		));
	}


	//Prints new lines until the new rank is reached.
	private static String stringToNewRank(int oldRank, int newRank) {
		String toString = "";
		for(int i = oldRank; i > newRank; i--) {
			toString += "\n";
		}
		return toString;
	}


	//Prints double spaces until the new file is reached.
	private static String stringToNewFile(int oldFile, int newFile) {
		String toString = " ";
		for(int i = oldFile; i < newFile; i++) {
			toString += " ";
		}
		return toString;
	}
}
