package mercs;

import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import main.Board;
import main.Move;
import main.Tile;



/**
 * Chooses a piece to place on a board based on its assigned PieceType.
 * @author trevor
 */
public class PiecePlacer {
	private final Board board;
	private final Map<Integer, PieceType> pieceToType;


	/**
	 * @param board The Board to place pieces on.
	 * @param pieceToType A mapping of pieces to their PieceTypes.
	 */
	public PiecePlacer(
		Board board, Map<Integer, PieceType> pieceToType
	) {
		this.board = board;
		this.pieceToType = new HashMap<Integer, PieceType>(pieceToType);
	}


	private Integer placeablePiece(PieceType type) {
		for(Integer piece : pieceToType.keySet()) {
			boolean isPieceOffBoard = board.tileForPiece(piece) == null;
			boolean isCorrectType = type.equals(pieceToType.get(piece));
			if(isPieceOffBoard && isCorrectType) {
				return piece;
			}
		}
		return null;
	}


	/**
	 * @param type The type that is being checked for availability.
	 * @return Whether or not there is a piece of PieceType type that can be
	 * placed from off the board, to somewhere on it.
	 */
	public boolean isTypeAvailable(PieceType type) {
		return placeablePiece(type) != null;
	}


	/**
	 * @param type the PieceType needed for the piece being placed.
	 * @param tile the tile to place the piece on.
	 * @returns a Board with a piece (that matches the PieceType) placed on 
	 * tile.
	 */
	public Board placePiece(PieceType type, Tile tile) {
		//Finds a piece that is described by group that is off the board.
		Integer pieceBeingPlaced = placeablePiece(type);
		if(pieceBeingPlaced == null) {
			throw new RuntimeException(
				"There was no piece of the PieceType " + type + " available" +
				"to place."
			);
		}

		//Places the piece on tile, constructing a new Board to be returned.
		return board.makeMove(new Move(pieceBeingPlaced, tile));
	}


	public static void main(String[] args) {
		List<Tile> pieceLocations = new ArrayList<Tile>();
		pieceLocations.add(new Tile(1, 1));
		pieceLocations.add(new Tile(5, 5));
		pieceLocations.add(null);
		pieceLocations.add(null);

		Board board = Board.squareBoard(5, pieceLocations);

		Map<Integer, PieceType> pieceToType = new HashMap<Integer, PieceType>();
		pieceToType.put(0, PieceType.WAZIR);
		pieceToType.put(1, PieceType.PAWN);
		pieceToType.put(2, PieceType.PAWN);
		pieceToType.put(3, PieceType.KNIGHT);

		PiecePlacer placer = new PiecePlacer(board, pieceToType);

		System.out.println(board);

		board = placer.placePiece(PieceType.KNIGHT, new Tile(2, 2));
		placer = new PiecePlacer(board, pieceToType);
		System.out.println(board);

		board = placer.placePiece(PieceType.PAWN, new Tile(1, 1));
		placer = new PiecePlacer(board, pieceToType);
		System.out.println(board);

		board = placer.placePiece(PieceType.WAZIR, new Tile(5, 2));
		placer = new PiecePlacer(board, pieceToType);
		System.out.println(board);
	}
}
