package mercs.pieceLogics;

import java.util.ArrayList;

import java.util.List;

import main.Board;
import main.Move;
import main.RecordBasedMoveLogic;
import main.Tile;



/**
 * Models the behavior of a pawn in the game of Mercs. The pawn can move
 * one Tile forward for a play (which might mean towards or away from higher
 * ranks, based on whichever color this pawn is). It cannot take other pieces
 * this way however. If it wants to take a piece for a play, there must be one
 * that is on a Tile that is directly and diagonally in front of it. If it is
 * to take a piece like this, it will move to the space of the piece it is
 * taking.
 * @author trevor
 */
public class PawnLogic implements RecordBasedMoveLogic {
	private final boolean isWhite;
	private final Integer piece;
	private final Board board;


	/**
	 * @param isWhite Is true if this piece is white (and so moves towards
	 * higher ranks), and is false if this piece is black (and so moves towards
	 * lower ranks).
	 * @param piece The piece that this PawnLogic informs. (non-null)
	 * @param board The board that this PawnLogic's piece plays on. (non-null)
	 */
	public PawnLogic(boolean isWhite, Integer piece, Board board) {
		handleExceptions(piece, board);

		this.isWhite = isWhite;
		this.piece = piece;
		this.board = board;
	}


	public Move[][] plays() {
		List<Move[]> plays = new ArrayList<Move[]>(0);

		/**
		 * Checks that PawnLogic's piece is on the board. If it isn't this will
		 * return plays as empty, since there is nothing a pawn can do if it is
		 * doesn't exist on the board.
		 */
		Tile location = board.tileForPiece(piece);
		if(location == null) {
			return plays.toArray(new Move[plays.size()][]);
		}

		/**
		 * Checks that there is a Tile in front of PawnLogic's piece, and that
		 * it is unoccupied. If this is true, adds the play to move to this
		 * Tile.
		 */
		Tile inFrontOf = location.add(forwardOffset());
		if(
			board.tiles().contains(inFrontOf) &&
			board.pieceOnTile(inFrontOf) == null
		) {
			Move[] moveForward = {new Move(piece, inFrontOf)};
			plays.add(moveForward);
		}

		//Adds the play to capture the forward left piece if PawnLogic can.
		Move[] captureForwardLeft = captureTile(inFrontOf.left());
		if(captureForwardLeft != null) {
			plays.add(captureForwardLeft);
		}

		//Adds the play to capture the forward right piece if PawnLogic can.
		Move[] captureForwardRight = captureTile(inFrontOf.right());
		if(captureForwardRight != null) {
			plays.add(captureForwardRight);
		}

		return plays.toArray(new Move[plays.size()][]);
	}


	/**
	 * @return The direction that this PawnLogic considers "forward".
	 */
	private Tile forwardOffset() {
		if(isWhite) {
			return new Tile(1, 0);
		}
		else {
			return new Tile(-1, 0);
		}
	}


	/**
	 * @return A play to take the piece on tile and move to it. If there is no
	 * piece on tile, returns null.
	 */
	private Move[] captureTile(Tile tile) {
		if(board.pieceOnTile(tile) != null) {
			Move[] captureTile = {
				new Move(board.pieceOnTile(tile), null),
				new Move(piece, tile)
			};
			return captureTile;
		}
		return null;
	}


	/**
	 * Throws exceptions if any of the arguments aren't allowed for the
	 * constructor.
	 */
	private static void handleExceptions(Integer piece, Board board) {
		if(piece == null) {
			throw new NullPointerException(
				"piece is null: There isn't a piece for this PawnLogic to " +
				"control."
			);
		}
		if(board == null) {
			throw new NullPointerException(
				"board is null: There isn't a board that this PawnLogic can " +
				"act on."
			);
		}
	}


	public RecordBasedMoveLogic update(Board board, Move[] play) {
		return new PawnLogic(isWhite, piece, board);
	}
}
