package console;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import main.Tile;
import mercs.PieceType;
import mercs.info.GameInfo;



public class BoardDisplay {
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


	public static String[] addGrid(String[] boardDisplay, int numFiles) {
		String[] griddedDisplay = new String[boardDisplay.length + 4];
		String sgrBold = "\u001b[1m";
		String sgrReset = "\u001b[m";
		String files = "    ";
		for(int i = 0; i < numFiles; i++) {
			files += sgrBold + (char) (97 + i) + "  " + sgrReset;
		}
		griddedDisplay[0] = files;
		griddedDisplay[1] = "";
		griddedDisplay[griddedDisplay.length - 1] = files;
		griddedDisplay[griddedDisplay.length - 2] = "";
		
		for(int i = 0; i < boardDisplay.length; i++) {
			String rank = sgrBold + String.valueOf(boardDisplay.length - i) + 
				sgrReset;
			griddedDisplay[i + 2] = rank + "   " + boardDisplay[i] + "   " + 
				rank;
		}


		return griddedDisplay;
	}


	public static Map<Tile, String> tileToDisplay(GameInfo info) {
		Map<Tile, String> tileToDisplay = new HashMap<>();
		for(Tile tile : info.board().tiles()) {
			Integer piece = info.board().pieceOnTile(tile);
			if(piece == null) continue;
			String display = " ";
			PieceType type = info.pieceToInfo().get(piece).type();
			Integer blackPlayer = info.order().secondPlayer();
			if(info.playerToInfo().get(blackPlayer).pieces().contains(piece)) {
				if(type == PieceType.PAWN) display = "\u2659";
				else if(type == PieceType.FERZ) display = "\u2657";
				else if(type == PieceType.WAZIR) display = "\u2656";
				else if(type == PieceType.KNIGHT) display = "\u2658";
				else if(type == PieceType.COMMANDO) display = "\u2654";
			}
			else {
				if(type == PieceType.PAWN) display = "\u265F";
				else if(type == PieceType.FERZ) display = "\u265D";
				else if(type == PieceType.WAZIR) display = "\u265C";
				else if(type == PieceType.KNIGHT) display = "\u265E";
				else if(type == PieceType.COMMANDO) display = "\u265A";
			}
			tileToDisplay.put(tile, display);
		}
		return tileToDisplay;
	}
}
