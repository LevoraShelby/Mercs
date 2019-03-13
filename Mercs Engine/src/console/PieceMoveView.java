package console;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;

import mercs.info.GameInfo;



/**
 * A view to show the different pieces that the current player in a game of
 * Mercs has on board. One of these pieces can be selected. This selected piece
 * will be seen as selected in the display and the plays that the selected
 * piece can take will also be visible.
 * @author trevor
 */
public class PieceMoveView {
	private final Integer[] pieces;
	private final SelectionView piecesView;
	private final MovementView movementView;


	/**
	 * @param info The game of Mercs.
	 * @param selectedPiece The piece being selected. If this piece isn't one
	 * that the current player has in play, no pieces are selected.
	 */
	public PieceMoveView(GameInfo info, Integer selectedPiece) {
		this.pieces = pieces(info);

		int selectedIndex;
		if(selectedPiece != null) {
			selectedIndex = Arrays.binarySearch(pieces, selectedPiece);
			if(selectedIndex < 0) {
				selectedPiece = null;
			}
		}
		else {
			selectedIndex = -1;
		}

		this.piecesView = new SelectionView(
			pieceSelections(this.pieces), selectedIndex, 5
		);

		this.movementView = new MovementView(info, selectedPiece);
	}


	protected PieceMoveView(
		Integer[] pieces, 
		SelectionView piecesView,
		MovementView movementView
	) {
		this.pieces = pieces.clone();
		this.piecesView = piecesView;
		this.movementView = movementView;
	}


	/**
	 * @param info The game of Mercs.
	 * @return An array of pieces that the current player has in play.
	 */
	private static Integer[] pieces(GameInfo info) {
		Integer currentPlayer = info.order().turn().currentPlayer();
		Set<Integer> pieceSet = info.playerToInfo().get(currentPlayer)
			.pieces();
		Iterator<Integer> iter = pieceSet.iterator();

		while(iter.hasNext()) {
			if(info.board().tileForPiece(iter.next()) == null) {
				iter.remove();
			}
		}

		Integer[] pieces = pieceSet.toArray(new Integer[pieceSet.size()]);
		Arrays.sort(pieces);

		return pieces;
	}


	/**
	 * @param pieces An array of pieces.
	 * @return An array of the pieces as Strings with a period behind them.
	 */
	private static String[] pieceSelections(Integer[] pieces) {
		String[] pieceSelections = Arrays.stream(pieces)
			.map(piece -> String.valueOf(piece) + ".")
			.toArray(String[]::new);

		return pieceSelections;
	}


	/**
	 * @param selectedPiece
	 * @return A PieceMoveView where selectedPiece is the piece that has become
	 * selected.
	 */
	public PieceMoveView select(Integer selectedPiece) {
		int selectedIndex;
		if(selectedPiece != null) {
			selectedIndex = Arrays.binarySearch(pieces, selectedPiece);
			if(selectedIndex < 0) {
				selectedPiece = null;
			}
		}
		else {
			selectedIndex = -1;
		}

		return new PieceMoveView(
			pieces,
			piecesView.select(selectedIndex),
			movementView.select(selectedPiece)
		);
	}


	/**
	 * @return The display for this view, showing the current pieces that can
	 * be selected, the current board, the selected piece, and the moves that
	 * the selected piece can take.
	 */
	public String[] display() {
		String[] display1 = piecesView.display();
		String[] display2 = BoardDisplay.addGrid(movementView.display(), 6);

		int length = display1.length > display2.length ? 
			display1.length : display2.length;
		String[] combinedDisplay = new String[length];

		for(int i = 0; i < combinedDisplay.length; i++) {
			combinedDisplay[i] = "";
			if(i < display1.length) {
				combinedDisplay[i] += display1[i];
			}
			else if(i >= display1.length) {
				combinedDisplay[i] += "     ";
			}
			combinedDisplay[i] += "     ";

			if(i < display2.length) {
				combinedDisplay[i] += display2[i];
			}
		}

		return combinedDisplay;
	}
}
