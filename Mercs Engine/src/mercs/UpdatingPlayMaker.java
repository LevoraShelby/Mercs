package mercs;

import main.RecordBasedMoveLogic;
import main.Board;
import main.Move;

import java.util.Map;



/**
 * TODO: Document
 * @author trevor
 */
public class UpdatingPlayMaker extends PlayMaker {
	private final RecordUpdater updater;


	public UpdatingPlayMaker(
		Board board, 
		Map<Integer, ? extends RecordBasedMoveLogic> pieceToLogic
	) {
		super(board, pieceToLogic);
		this.updater = new RecordUpdater(pieceToLogic);
	}


	public Map<Integer, RecordBasedMoveLogic> update(
		Board board, Move[] play
	) {
		return updater.update(board, play);
	}
}
