package display;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.Tile;
import mercs.PieceType;
import mercs.info.GameInfo;



/**
 * Read https://en.wikipedia.org/wiki/ANSI_escape_code#SGR_(Select_Graphic_Rendition)_parameters
 * for information on select graphic rendition codes.
 */
public class Display {
	/**
	 * @param info A game of Mercs.
	 * @return A mapping of tiles on info's board to how that tile can be
	 * displayed.
	 */
	public static Map<Tile, String> tileToDisplay(GameInfo info) {
		Map<Tile, String> tileToDisplay = new HashMap<>();

		for(Tile tile : info.board().tiles()) {
			String displayForTile;
			//Sets the background of the display to match the color of the tile
			if(isTileWhite(tile)) {
				displayForTile = "\u001b[48;2;140;145;155m";
			}
			else {
				displayForTile = "\u001b[48;2;80;90;105m";
			}

			//Adds the piece on the current tile (if one exists) to its display
			Integer pieceOnTile = info.board().pieceOnTile(tile);
			if(pieceOnTile != null) {
				displayForTile += displayForPiece(info, pieceOnTile);
			}
			else {
				//An empty space if no piece exists on tile
				displayForTile += ' '; 
			}

			tileToDisplay.put(tile, displayForTile);
		}

		return tileToDisplay;
	}


	/**
	 * @param tiles A collection of tiles that need to be ordered.
	 * @return A new list of tiles, sorted by their print order.
	 */
	public static List<Tile> sortedTiles(Collection<Tile> tiles) {
		TileByDisplayOrderComparator tileComparator = 
			new TileByDisplayOrderComparator();
		List<Tile> sortedTiles = new ArrayList<>(tiles);
		sortedTiles.sort(tileComparator);
		return sortedTiles;
	}


	/**
	 * @param tile
	 * @return Whether or not the tile is white, using the standard alternating
	 * pattern of color that chess uses, where the tile of rank 1, file 1 is
	 * white.
	 */
	public static boolean isTileWhite(Tile tile) {
		boolean isTileOnEvenRank = tile.rank() % 2 == 0;
		boolean isTileOnEvenFile = tile.file() % 2 == 0;
		return isTileOnEvenRank ^ isTileOnEvenFile;
	}


	/**
	 * @param info A game of Mercs.
	 * @param piece A piece owned by a player playing the game of Mercs that
	 * info describes.
	 * @return The display that represents piece.
	 */
	private static String displayForPiece(GameInfo info, Integer piece) {
		String displayForPiece;

		PieceType type = info.pieceToInfo().get(piece).type();
		displayForPiece = "" + displayForPieceType(type);

		Integer whitePlayer = info.order().firstPlayer();
		Integer blackPlayer = info.order().secondPlayer();
		//Makes the piece white if it belongs to a white player.
		if(
			info.playerToInfo().get(whitePlayer)
			.pieces().contains(piece)
		) {
			displayForPiece = "\u001b[38;2;255;255;255m" + displayForPiece;
		}

		//Makes the piece black if it belongs to a black player.
		else if(
			info.playerToInfo().get(blackPlayer)
			.pieces().contains(piece)
		) {
			displayForPiece = "\u001b[38;2;0;0;0m" + displayForPiece;
		}
		//Throws an error if nobody owns the piece.
		else {
			throw new IllegalArgumentException(
				"piece isn't owned by a player."
			);
		}

		return displayForPiece;
	}


	/**
	 * @param type
	 * @return The unicode character that represents this type of piece.
	 */
	private static char displayForPieceType(PieceType type) {
		if(type == PieceType.PAWN) return '\u265F';          //unicode pawn
		else if(type == PieceType.FERZ) return '\u265D';     //unicode bishop
		else if(type == PieceType.WAZIR) return '\u265C';    //unicode rook
		else if(type == PieceType.KNIGHT) return '\u265E';   //unicode knight
		else if(type == PieceType.COMMANDO) return '\u265A'; //unicode king
		else {
			throw new IllegalArgumentException(
				"piece is of an unsupported PieceType."
			);
		}
	}
}
