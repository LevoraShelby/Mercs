package console;

import java.util.Map;

import main.Move;
import main.Tile;
import mercs.info.GameInfo;



public class MovementView {
	private static final String SGR_SELECTED = "\u001b[5m";
	private static final String SGR_VULNERABLE = "\u001b[7m";
	private static final String SGR_RESET = "\u001b[m";

	private final GameInfo info;
	private final Integer selectedPiece;


	public MovementView(GameInfo info, Integer selectedPiece) {
		this.info = info;
		this.selectedPiece = selectedPiece;
	}


	public String[] display() {
		Map<Tile, String> tileToDisplay;
		if(selectedPiece != null) {
			tileToDisplay = tileToDisplay();
		}
		else {
			tileToDisplay = BoardDisplay.tileToDisplay(info);
		}

		return GamePrinter.gameToString(
			info.board().tiles(), tileToDisplay, "X"
		).split("\n");
	}


	private Map<Tile, String> tileToDisplay() {
		Map<Tile, String> tileToDisplay = BoardDisplay.tileToDisplay(info);

		Move[][] plays = info.pieceToInfo().get(selectedPiece).logic().plays();
		for(Move[] play : plays) {
			Tile tile = play[play.length - 1].newPosition();

			String display = tileToDisplay.get(tile);
			if(display == null) {
				display = "X";
			}
			display = SGR_VULNERABLE + display + SGR_RESET;

			tileToDisplay.put(tile, display);
		}

		Tile tileOfSelectedPiece = info.board().tileForPiece(selectedPiece);
		String display = tileToDisplay.get(tileOfSelectedPiece);
		display = SGR_SELECTED + display + SGR_RESET;
		tileToDisplay.put(tileOfSelectedPiece, display);

		return tileToDisplay;
	}


	public MovementView select(Integer selectedPiece) {
		if(this.selectedPiece == selectedPiece) {
			return this;
		}
		return new MovementView(info, selectedPiece);
	}
}
