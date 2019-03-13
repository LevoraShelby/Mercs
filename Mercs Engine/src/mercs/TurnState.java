package mercs;

import java.util.Objects;



/**
 * TODO: Document and add exceptions.
 * @author trevor
 */
public class TurnState {
	private final Integer currentPlayer;
	private final PlayState playState;


	public TurnState(
		Integer currentPlayer, 
		PlayState playState
	) {
		

		this.currentPlayer = currentPlayer;
		this.playState = playState;
	}


	public Integer currentPlayer() {
		return currentPlayer;
	}


	public PlayState playState() {
		return playState;
	}


	public String toString() {
		String toString = "{currentPlayer: " + currentPlayer;
		toString += ", playState: " + playState + "}";
		return toString;
	}


	public boolean equals(Object o) {
		if(o == null) {
			return false;
		}
		TurnState ts = (TurnState) o;
		return currentPlayer == ts.currentPlayer() 
			&& playState == ts.playState();
	}


	public int hashCode() {
		return Objects.hash(currentPlayer, playState);
	}
}