package mercs;

import java.util.HashMap;
import java.util.Map;

import main.Board;
import main.Move;
import main.MoveLogic;



public class MoveTaker {
	private final Board board;
	private final Map<Integer, MoveLogic> pieceToMoveLogic;


	public MoveTaker(Board board, Map<Integer, MoveLogic> pieceToMoveLogic) {
		this.board = board;
		this.pieceToMoveLogic = new HashMap<Integer, MoveLogic>(
			pieceToMoveLogic
		);
	}


	public Board takeMove(Integer piece, int moveIndex) {
		return null;
	}


	public Map<Integer, Move[][]> pieceToPlays() {
		Map<Integer, Move[][]> pieceToPlayers = new HashMap<Integer, Move[][]>();
		return null;
	}
}
