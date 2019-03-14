package display;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.Tile;
import mercs.PieceType;
import mercs.info.GameInfo;



/**
 * A utility class for the display package.
 * 
 * Read https://en.wikipedia.org/wiki/ANSI_escape_code#SGR_(Select_Graphic_Rendition)_parameters
 * for information on select graphic rendition codes.
 */
public class Display {
	/**
	 * @param info A game of Mercs.
	 * @return A mapping of tiles on info's board to how that tile can be
	 * displayed.
	 */
	public static Map<Tile, SgrString> tileToDisplay(GameInfo info) {
		Map<Tile, SgrString> tileToDisplay = new HashMap<>();

		for(Tile tile : info.board().tiles()) {
			SgrString displayForTile;

			Color tileColor;
			//Sets the background of the display to match the color of the tile
			if(isTileWhite(tile)) {
				tileColor = new Color(140,145,155);
			}
			else {
				tileColor = new Color(80,90,105);
			}

			//Adds the piece on the current tile (if one exists) to its display
			Integer pieceOnTile = info.board().pieceOnTile(tile);
			if(pieceOnTile != null) {
				/**
				 * Gets the unicode character to represent the piece on the
				 * current tile.
				 */
				String pieceDisplay = displayForPieceType(
					info.pieceToInfo().get(pieceOnTile).type()
				);
				Color pieceColor = pieceColor(info, pieceOnTile);
				/**
				 * Creates an SGR string with a unicode character that matches
				 * the piece on the current tile for the base, the color of
				 * that piece for the foreground, and color of the current tile
				 * for the background, creating the effect of having a piece
				 * on a Tile.
				 */
				displayForTile = new SgrString(
					pieceDisplay + " ", pieceColor, tileColor
				);
			}
			else {
				//An empty space if no piece exists on tile
				displayForTile = new SgrString("  ", null, tileColor); 
			}

			tileToDisplay.put(tile, displayForTile);
		}

		return tileToDisplay;
	}


	/**
	 * @param tiles A collection of tiles that need to be ordered.
	 * @return A new list of tiles, sorted by their print order.
	 */
	public static Tile[] sortedTiles(Collection<Tile> tiles) {
		TileByDisplayOrderComparator tileComparator = 
			new TileByDisplayOrderComparator();
		List<Tile> sortedTiles = new ArrayList<>(tiles);
		sortedTiles.sort(tileComparator);
		return sortedTiles.toArray(new Tile[sortedTiles.size()]);
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
	private static Color pieceColor(GameInfo info, Integer piece) {
		Integer whitePlayer = info.order().firstPlayer();
		Integer blackPlayer = info.order().secondPlayer();
		//Makes the piece white if it belongs to a white player.
		if(
			info.playerToInfo().get(whitePlayer)
			.pieces().contains(piece)
		) {
			return Color.WHITE;
		}

		//Makes the piece black if it belongs to a black player.
		else if(
			info.playerToInfo().get(blackPlayer)
			.pieces().contains(piece)
		) {
			return Color.BLACK;
		}
		//Throws an error if nobody owns the piece.
		else {
			throw new IllegalArgumentException(
				"piece isn't owned by a player."
			);
		}
	}


	/**
	 * @param type
	 * @return The unicode character that represents this type of piece.
	 */
	private static String displayForPieceType(PieceType type) {
		if(type == PieceType.PAWN) return "\u265F";          //unicode pawn
		else if(type == PieceType.FERZ) return "\u265D";     //unicode bishop
		else if(type == PieceType.WAZIR) return "\u265C";    //unicode rook
		else if(type == PieceType.KNIGHT) return "\u265E";   //unicode knight
		else if(type == PieceType.COMMANDO) return "\u265A"; //unicode king
		else {
			throw new IllegalArgumentException(
				"piece is of an unsupported PieceType."
			);
		}
	}
}
