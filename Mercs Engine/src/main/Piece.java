package main;


/**
 * Represents something that communicates sets of Moves that it can make.
 * @contract All implementors of Piece should be immutable.
 * @author trevor
 */
public interface Piece {
	/**
	 * @return an array of turns that the Piece would be allowed to take.
	 */
	public Move[][] turns();

	/**
	 * @param turn a turn to affect the board that Piece is on.
	 * @return a new Piece that represents the last Piece, but now in the
	 * context of the change in board.
	 */
	public Piece updateBoard(Move[] turn);
}
