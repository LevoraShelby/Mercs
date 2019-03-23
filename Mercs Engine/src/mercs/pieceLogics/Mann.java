package mercs.pieceLogics;

import main.Board;
import main.Move;
import main.RecordBasedMoveLogic;



/**
 * A piece that moves like a king in chess does, but is not restricted by the
 * other team's movement options. A king without his crown.
 * @author trevor
 */
public class Mann implements RecordBasedMoveLogic {
	private final RecordBasedMoveLogic innerFerz;
	private final RecordBasedMoveLogic innerWazir;
	private final Integer piece;


	/**
	 * @param piece The piece assigned to this logic.
	 * @param board The board this logic navigates with.
	 */
	public Mann(Integer piece, Board board) {
		this.piece = piece;

		//Used for the diagonal movements.
		this.innerFerz = Leaper.ferz(piece, board);
		//Used for the horizontal and vertical movements.
		this.innerWazir = Leaper.wazir(piece, board);
	}


	/**
	 * Combines the diagonal plays with the horizontal and vertical ones.
	 */
	public Move[][] plays() {
		/**
		 * Gets the plays Mann has and sets up the array combinedPlays to put
		 * them in.
		 */
		Move[][] ferzPlays = innerFerz.plays();
		Move[][] wazirPlays = innerWazir.plays();
		Move[][] combinedPlays = 
			new Move[ferzPlays.length + wazirPlays.length][];
		
		//Adds the diagonal plays.
		for(int i = 0; i < ferzPlays.length; i++) {
			combinedPlays[i] = ferzPlays[i];
		}
		//Adds the horizontal and vertical plays.
		for(int i = 0; i < wazirPlays.length; i++) {
			combinedPlays[i + ferzPlays.length] = wazirPlays[i];
		}

		return combinedPlays;
	}


	public RecordBasedMoveLogic update(Board board, Move[] play) {
		return new Mann(piece, board);
	}
}
