package test;

import java.util.ArrayList;
import java.util.List;

import main.Board;
import main.Move;
import main.RecordBasedMoveLogic;
import main.Tile;



/**
 * A test piece that just goes forward, capturing anything it comes in contact
 * with.
 * @author trevor
 */
public class Forward implements RecordBasedMoveLogic {
	private final Integer piece;
	private final Board board;


	public Forward(Integer piece, Board board) {
		this.piece = piece;
		this.board = board;
	}


	public Move[][] plays() {
		if(board.tileForPiece(piece) != null) {
			Tile newPosition = board.tileForPiece(piece).add(new Tile(1, 0));
			Integer occupyingPiece = board.pieceOnTile(newPosition);

			List<Move> goingForward = new ArrayList<Move>();
			if(occupyingPiece != null) {
				goingForward.add(new Move(occupyingPiece, null));
			}
			goingForward.add(new Move(piece, newPosition));

			Move[][] plays = {
				goingForward.toArray(new Move[goingForward.size()])
			};
			return plays;
		}
		else {
			Move[][] plays = {};
			return plays;
		}
	}


	public RecordBasedMoveLogic update(Board board, Move[] play) {
		return new Forward(piece, board);
	}
}
