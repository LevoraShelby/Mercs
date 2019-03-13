package test;

import main.Board;
import main.Move;
import main.RecordBasedMoveLogic;



public class BlankLogic implements RecordBasedMoveLogic {
	public Move[][] plays() {
		return new Move[0][];
	}


	public RecordBasedMoveLogic update(Board board, Move[] play) {
		return this;
	}
}
