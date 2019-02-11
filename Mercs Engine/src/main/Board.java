package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * The Board is consists of a set of Tiles, and pieces that exist with those
 * tiles. These tiles are stored as an array of Tiles while pieces are stored
 * as an array of indexes, which point to the Tile they belong to on the board.
 * If they are off the board at the time, the index is written as -1. No two
 * pieceIndexes should point to the same Tile (note: -1 does not point to a
 * Tile).
 * @author trevor
 */
public final class Board {
	private final List<Tile> tiles;
	private final List<Integer> tileIndexesOfPieces;


	/**
	 * Constructs a ChessSet. board and pieceIndexes will always remain in the
	 * same order they are constructed in.
	 * @param tiles a set of Tiles.
	 * @param tileIndexesOfPieces a set of indexes for the parameter tiles, with
	 * one tile in tiles for each piece. The piece itself is decided by its index
	 * in this list.
	 */
	public Board(List<Tile> tiles, List<Integer> tileIndexesOfPieces) {
		//Checks that tiles does not contain two of the same Tile.
		if(hasRepeatingTiles(tiles)) {
			throw new RuntimeException(
				"tiles should not have repeating Tiles."
			);
		}
		this.tiles = new ArrayList<Tile>(tiles);

		//Checks for an out of bounds pieceIndex
		for(int pieceIndex : tileIndexesOfPieces) {
			if(pieceIndex >= tiles.size() || pieceIndex < -1) {
				throw new IndexOutOfBoundsException(
					"pieceIndex is out of bounds."
				);
			}
		}
		//Checks that there is no Tile on the board with two pieces.
		if(hasRepeatingIndexes(tileIndexesOfPieces)) {
			throw new RuntimeException(
				"At least two pieceIndexes point to the same Tile."
			);
		}
		this.tileIndexesOfPieces = new ArrayList<Integer>(
			tileIndexesOfPieces
		);
	}


	public List<Tile> tiles() {
		return new ArrayList<Tile>(tiles);
	}


	public List<Integer> tileIndexesOfPieces() {
		return new ArrayList<Integer>(tileIndexesOfPieces);
	}


	public String toString() {
		String toString = "{Tiles: {" + tiles + "}, ";

		Tile[] pieces = new Tile[tileIndexesOfPieces.size()];
		for(int i = 0; i < pieces.length; i++) {
			if(tileIndexesOfPieces.get(i) != -1) {
				pieces[i] = tiles.get(tileIndexesOfPieces.get(i));
			}
			else {
				pieces[i] = null;
			}
		}
		toString += "pieces: {" + Arrays.deepToString(pieces) + "}}";

		return toString;
	}


	/**
	 * @param tile the Tile that the piece is on.
	 * @return the piece (an index for pieceIndexes) on tile. Returns -1 if
	 * there is no piece on tile.
	 */
	public Integer pieceOnTile(Tile tile) {
		Integer pieceOnTile = tileIndexesOfPieces.indexOf(tiles.indexOf(tile));
		if(pieceOnTile == -1) {
			return null;
		}
		return pieceOnTile;
	}


	/**
	 * @param piece the piece to locate a Tile for.
	 * @return the Tile that piece is on.
	 */
	public Tile tileForPiece(Integer piece) {
		Integer tileIndexForPiece = tileIndexesOfPieces.get(piece);
		if(tileIndexForPiece == -1) {
			return null;
		}
		return tiles.get(tileIndexForPiece);
	}


	/**
	 * @param tiles the Tile to check.
	 * @return whether or not board has any repeating Tiles.
	 */
	private static boolean hasRepeatingTiles(List<Tile> tiles) {
		for(int i = 0; i < tiles.size(); i++) {
			for(int j = i + 1; j < tiles.size(); j++) {
				if(tiles.get(i).equals(tiles.get(j))) {
					return true;
				}
			}
		}
		return false;
	}


	/**
	 * @param indexes the indexes to check.
	 * @return whether or not array has any repeating indexes (excluding -1).
	 */
	private static boolean hasRepeatingIndexes(List<Integer> indexes) {
		for(int i = 0; i < indexes.size(); i++) {
			if(indexes.get(i) != -1) {
				for(int j = i + 1; j < indexes.size(); j++) {
					if(indexes.get(i).equals(indexes.get(j))) {
						return true;
					}
				}
			}
		}
		return false;
	}
}