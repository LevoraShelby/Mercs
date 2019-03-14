package display;

import java.util.Map;

import main.Move;
import main.Tile;
import mercs.info.GameInfo;



public class MovementView {
	private final GameInfo info;
	private final Integer selectedPiece;


	public MovementView(GameInfo info, Integer selectedPiece) {
		this.info = info;
		this.selectedPiece = selectedPiece;
	}


	public String[] display() {
		Map<Tile, SgrString> tileToDisplay;
		if(selectedPiece != null) {
			tileToDisplay = tileToDisplayFromSelectedPiece();
		}
		else {
			tileToDisplay = Display.tileToDisplay(info);
		}

		/**
		 * Stops the color of the last tile from bleeding through the rest of
		 * the line.
		 */
		String[] display = Display.boardDisplay(tileToDisplay);
		for(int i = 0; i < display.length; i++) {
			display[i] = display[i] + "\u001b[m";
		}
		return display;
	}


	private Map<Tile, SgrString> tileToDisplayFromSelectedPiece() {
		Map<Tile, SgrString> tileToDisplay = Display.tileToDisplay(info);

		Move[][] plays = info.pieceToInfo().get(selectedPiece).logic().plays();
		for(Move[] play : plays) {
			/**
			 * Gets the display for the tile that the current play has the
			 * selected piece moving to.
			 */
			Tile tile = play[play.length - 1].newPosition();
			SgrString display = tileToDisplay.get(tile);
			
			display = display.changeBackground(display.background().darker());
			tileToDisplay.put(tile, display);
		}

		Tile tileOfSelectedPiece = info.board().tileForPiece(selectedPiece);
		SgrString display = tileToDisplay.get(tileOfSelectedPiece);
		/**
		 * Makes the selected piece blink so the user knows which piece is
		 * selected.
		 */
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
