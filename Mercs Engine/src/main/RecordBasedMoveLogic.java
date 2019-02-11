package main;


/**
 * TODO: Fix documentation
 * Represents something that communicates sets of plays that could be taken.
 * @contract All implementors of RBML should be immutable.
 * @author trevor
 */
public interface RecordBasedMoveLogic extends MoveLogic {
	/**
	 * @param turn a turn to affect the board that RBML is on.
	 * @return a new RBML that represents the last RBML, but now in the context
	 * of the change in board.
	 */
	public RecordBasedMoveLogic updateBoard(Board board, Move[] play);
}
