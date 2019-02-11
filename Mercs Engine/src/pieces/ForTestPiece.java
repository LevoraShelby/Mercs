package pieces;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.Move;
import main.Piece;
import main.Tile;



/**
 * Can move a Tile up, left, right, or down as a turn. Cannot capture Pieces.
 * @author trevor
 */
@Deprecated
public final class ForTestPiece implements Piece {
	private static Tile[] offsets = {
		new Tile(0, 1),  //right
		new Tile(1, 0),  //up
		new Tile(0, -1), //left
		new Tile(-1, 0)  //down
	};
	private final Map<Tile, Boolean> board;
	private final Tile position;


	/**
	 * @param board values that are true represent Tile with Pieces on them,
	 * while false values represent Tiles that are empty.
	 * @param position Tile that this Piece is on.
	 */
	public ForTestPiece(Map<Tile, Boolean> board, Tile position) {
		this.board = new HashMap<Tile, Boolean>(board);
		this.position = position;
	}


	public Move[][] turns() {
		List<Tile> availableEndTiles = new ArrayList<Tile>();
		for(Tile offset : offsets) {
			Tile resultTile = offset.add(position);
			//Checks that the Tile being moved to exists and is empty.
			if(board.containsKey(resultTile) && !board.get(resultTile)) {
				availableEndTiles.add(resultTile);
			}
		}
		
		Move[][] turnList = new Move[availableEndTiles.size()][];
		for(int i = 0; i < turnList.length; i++) {
			Move move = null; // new Move(position, availableEndTiles.get(i), null);
			Move[] turn = {move};
			turnList[i] = turn;
		}
		
		return turnList;
	}


	public Piece updateBoard(Move[] turn) {
//		Tile newPosition = position;
//		Map<Tile, Boolean> newBoard = new HashMap<Tile, Boolean>(board);
//		for(Move move : turn) {
//			//If this Piece was captured, throw an exception.
//			if(move.capturedPosition() != null &&
//					move.capturedPosition().equals(newPosition))
//			{
//				throw new RuntimeException(
//					"No Piece to return; it was captured."
//				);
//			}
//			//Updates position if this is the Piece being moved.
//			if(move.startingPosition().equals(newPosition)) {
//				newPosition = move.endingPosition();
//			}
//
//			/**
//			 * Takes the piece to move off the board. If there is no piece to
//			 * move, throws an exception.
//			 */
//			if(!newBoard.get(move.startingPosition())) {
//				throw new RuntimeException("Piece to move does not exist.");
//			}
//			newBoard.remove(move.startingPosition());
//			newBoard.put(move.startingPosition(), false);
//
//			//Captures a Tile if there is something to capture.
//			if(move.capturedPosition() != null ||
//				newBoard.containsKey(move.capturedPosition()))
//			{
//				newBoard.remove(move.capturedPosition());
//				newBoard.put(move.capturedPosition(), false);
//			}
//
//			/**
//			 * Places the Piece down. Throws an exception is there's a Piece in
//			 * the way.
//			 */
//			if(newBoard.get(move.endingPosition())) {
//				throw new RuntimeException("endingPosition is occupied.");
//			}
//			newBoard.remove(move.endingPosition());
//			newBoard.put(move.endingPosition(), true);
//		}
		return null; //new ForTestPiece(newBoard, newPosition);
	}
}
