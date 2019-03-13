package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * 
 * @author trevor
 */
public final class Board {
	private final Set<Tile> tiles;
	private final Map<Integer, Tile> pieceToTile;


	public Board(Set<Tile> tiles, Map<Integer, Tile> pieceToTile) {
		handleExceptions(tiles, pieceToTile);

		this.tiles = new HashSet<>(tiles);
		this.pieceToTile = new HashMap<>(pieceToTile);
	}


	/**
	 * @return A set of all of the Tiles in this Board.
	 */
	public Set<Tile> tiles() {
		return new HashSet<>(tiles);
	}


	/**
	 * @return A set of all of the pieces in this Board.
	 */
	public Set<Integer> pieces() {
		return new HashSet<>(pieceToTile.keySet());
	}


	/**
	 * @return A mapping of each piece in this Board to the Tile that the piece
	 * occupies. If a piece in this Board doesn't occupy a Tile, it will be
	 * mapped to a null value.
	 */
	public Map<Integer, Tile> pieceToTile() {
		return new HashMap<>(pieceToTile);
	}


	/**
	 * @param tile the Tile that the piece is on.
	 * @return the piece on tile. Returns null if there is no piece on tile.
	 */
	public Integer pieceOnTile(Tile tile) {
		for(Integer piece : pieceToTile.keySet()) {
			Tile tileForPiece = pieceToTile.get(piece);
			if(tileForPiece != null && tileForPiece.equals(tile)) {
				return piece;
			}
		}
		return null;
	}


	/**
	 * @param piece The piece to locate a Tile for.
	 * @return The Tile that piece is on. Returns null if the piece isn't on
	 * the board. (Note: the board might not have the piece. This is okay. If
	 * this is the case the board simply returns null, as the piece isn't on 
	 * the board -- or off it, for that matter.)
	 */
	public Tile tileForPiece(Integer piece) {
		return pieceToTile.get(piece);
	}


	public Board makeMove(Move move) {
		//Checks that the moving piece exists in Board.
		if(!pieceToTile.containsKey(move.piece())) {
			throw new RuntimeException(
				"Cannot move piece " + move.piece() + ", because it does not" +
				" exist on this Board."
			);
		}

		Map<Integer, Tile> newPieceToTile = 
			new HashMap<Integer, Tile>(pieceToTile);
		newPieceToTile.put(move.piece(), move.newPosition());
		return new Board(tiles, newPieceToTile);
	}


	public String toString() {
		List<Tile> sortedTiles = new ArrayList<>(tiles);
		Collections.sort(sortedTiles);
		String toString = "{Tiles: {" + sortedTiles + "}, ";
		Map<Integer, Tile> pieceToLocation = new HashMap<Integer, Tile>();
		for(Integer piece : pieceToTile.keySet()) {
			pieceToLocation.put(piece, tileForPiece(piece));
		}
		toString += "pieces: {" + pieceToLocation + "}}";

		return toString;
	}


	/**
	 * Throws exceptions if any of the arguments aren't allowed for the
	 * constructor.
	 */
	private static void handleExceptions(
		Set<Tile> tiles, Map<Integer, Tile> pieceToTile
	) {
		if(tiles == null) {
			throw new NullPointerException(
				"tiles is null: There isn't a set of Tiles that exists for "
				+ "this Board to describe."
			);
		}
		if(pieceToTile == null) {
			throw new NullPointerException(
				"pieceToTile is null: There isn't a set of piece for this "
				+ "Board to describe."
			);
		}
		//Checks that none of the pieces are on a non-existent Tile.
		for(Integer piece : pieceToTile.keySet()) {
			Tile tileForPiece = pieceToTile.get(piece);
			if(tileForPiece != null && !tiles.contains(tileForPiece)) {
				throw new RuntimeException(
					"pieceToTile has piece on a Tile that does not exist: "
					+ "Cannot have piece " + piece + " on " + tileForPiece +
					", as it does not exist in tiles."
				);
			}
		}
		//Checks that none of the piece are on the same Tile.
		List<Tile> tilesOfPieces = new ArrayList<>(pieceToTile.values());
		for(int i = 0; i < tilesOfPieces.size() - 1; i++) {
			for(int j = i + 1; j < tilesOfPieces.size(); j++) {
				if(tilesOfPieces.get(i) != null && tilesOfPieces.get(j) != null
					&& tilesOfPieces.get(i).equals(tilesOfPieces.get(j)) 
				) {
					throw new RuntimeException(
						"pieceToTile has repeating values: There are at least "
						+ "two pieces on the same Tile."
					);
				}
			}
		}
	}


	public static Board squareBoard(int length, List<Tile> pieceLocations) {
		Set<Tile> tiles = new HashSet<Tile>(length * length);
		for(int rank = 1; rank <= length; rank++) {
			for(int file = 1; file <= length; file++) {
				tiles.add(new Tile(rank, file));
			}
		}

		Map<Integer, Tile> pieceToTile = new HashMap<Integer, Tile>();
		for(Integer piece = 0; piece < pieceLocations.size(); piece++) {
			pieceToTile.put(piece, pieceLocations.get(piece));
		}

		return new Board(tiles, pieceToTile);
	}


	public static Board startingMercsBoard() { 
		//Creates 6x6 tile set.
		Set<Tile> tiles = new HashSet<Tile>(6*6);
		for(int rank = 1; rank <= 6; rank++) {
			for(int file = 1; file <= 6; file++) {
				tiles.add(new Tile(rank, file));
			}
		}

		//Builds the pieceToTileIndex.
		Map<Integer, Tile> pieceToTile = 
			new HashMap<Integer, Tile>(36);
		/**
		 * 0-17: white pieces, 17-35: black pieces
		 * (0-5,18-23): pawns, (6-9,24-27): ferzes, (10-13,28-31): wazirs,
		 * (14-15,32-33): knights, (16-17,34-35): commandos
		 */
		List<Tile> pieceLocations = Arrays.asList(
/**0-5*/	new Tile(2,2),new Tile(2,3),new Tile(2,4),new Tile(2,5),null,null,
/**6-9*/	null,null,null,null,
/**10-13*/	new Tile(1,2),new Tile(1,5),null,null,
/**14-15*/	new Tile(1,3),null,
/**16-17*/	new Tile(1,4),null,
/**18-23*/	new Tile(5,2),new Tile(5,3),new Tile(5,4),new Tile(5, 5),null,null,
/**24-27*/	null,null,null,null,
/**28-31*/	new Tile(6,2),new Tile(6,5),null,null,
/**32-33*/	new Tile(6,4),null,
/**34-35*/	new Tile(6,3),null
		);		
		for(Integer piece = 0; piece < 36; piece++) {
			pieceToTile.put(piece, pieceLocations.get(piece));
		};

		return new Board(tiles, pieceToTile);
	}
}