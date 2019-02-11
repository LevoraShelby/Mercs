package main;



/**
 * Represents a change in board layout for a chesslike game. It relates a piece
 * to the position it is being moved to (newPosition). If the piece is being
 * moved off the board, newPosition should be null. To add a piece to the board,
 * just have newPosition be the Tile it the piece gets placed on.
 * @author trevor
 */
public final class Move {
	private Integer piece;
	private final Tile newPosition;


	public Move(Integer piece, Tile newPosition) {	
		this.piece = piece;
		this.newPosition = newPosition;
	}


	public Tile newPosition() {
		return newPosition;
	}


	public Integer piece() {
		return piece;
	}


	/**
	 * Returns a String for the Move formatted as,
	 * "{piece: [[piece]], newPosition: [[newPosition]]}",
	 * where double brackets act as placeholders. newPosition will be added as
	 * "{<off board>}" if newPosition is a null value.
	 * @return a toString for the Move.
	 */
	public String toString() {
		String newPositionString;
		if(newPosition != null) {
			newPositionString = newPosition.toString();
		}
		else {
			newPositionString = "{<off board>}";
		}

		String toString = "{piece: " + piece.toString();
		toString += ", newPosition: " + newPositionString + "}";
		return toString;
	}
}
