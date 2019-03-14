package display;

import java.util.Map;

import main.Move;
import main.Tile;
import mercs.info.GameInfo;



public class MovementView {
	private static final String SGR_SELECTED = "\u001b[5m";
	private static final String SGR_RESET = "\u001b[m";

	private final GameInfo info;
	private final Integer selectedPiece;


	public MovementView(GameInfo info, Integer selectedPiece) {
		this.info = info;
		this.selectedPiece = selectedPiece;
	}


	public String[] display() {
		Map<Tile, SgrString> tileToDisplay;
		if(selectedPiece != null) {
			tileToDisplay = tileToDisplay();
		}
		else {
			tileToDisplay = Display.tileToDisplay(info);
		}

		List<String> display = new ArrayList<>();
	}


	private Map<Tile, SgrString> tileToDisplay() {
		Map<Tile, SgrString> tileToDisplay = Display.tileToDisplay(info);

		Move[][] plays = info.pieceToInfo().get(selectedPiece).logic().plays();
		for(Move[] play : plays) {
			/**
			 * Gets the display for the tile that the current play has the
			 * selected piece moving to.
			 */
			Tile tile = play[play.length - 1].newPosition();
			SgrString display = tileToDisplay.get(tile);
			//Makes the display blink in order to give it a 
			display = display.changeBackground(display.background().darker());
			tileToDisplay.put(tile, display);
		}

		Tile tileOfSelectedPiece = info.board().tileForPiece(selectedPiece);
		SgrString display = tileToDisplay.get(tileOfSelectedPiece);
		display = display.changeBlinking(true);
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
