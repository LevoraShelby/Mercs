package mercs;

import java.util.HashMap;
import java.util.Map;

import main.Board;
import main.Move;
import main.RecordBasedMoveLogic;



public class RecordUpdater {
	private Map<Integer, RecordBasedMoveLogic> pieceToLogic;


	public RecordUpdater(
		Map<Integer, ? extends RecordBasedMoveLogic> pieceToLogic
	) {
		this.pieceToLogic = new HashMap<>(pieceToLogic);
	}


	public Map<Integer, RecordBasedMoveLogic> update(
		Board board, Move[] play
	) {
		Map<Integer, RecordBasedMoveLogic> updatedPieceToLogic =
			new HashMap<>(pieceToLogic.size());

		for(Integer piece : pieceToLogic.keySet()) {
			RecordBasedMoveLogic updatedLogic =
				pieceToLogic.get(piece).update(board, play);
			updatedPieceToLogic.put(piece, updatedLogic);
		}

		return updatedPieceToLogic;
	}
}
