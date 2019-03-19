package mercs.info;

import mercs.PlayState;



/**
 * Describes the turn-based aspects of a game of Mercs. It keeps track of
 * whether it is the first player's turn of the second player's turn, and
 * whether the current player is moving a piece, buying a piece, promoting a
 * piece, or winning the game.
 * @author trevor
 */
public final class OrderInfo2 {
	private final boolean isFirstPlayersTurn;
	private final PlayState playState;


	/**
	 * @param isFirstPlayersTurn True if is the first player's turn; false if
	 * it is the second player's turn.
	 * @param playState Describes the current context of the current player's
	 * turn (moving, buying, promoting, having won). (non-null)
	 */
	public OrderInfo2(boolean isFirstPlayersTurn, PlayState playState) {
		if(playState == null) {
			throw new NullPointerException(
				"playState cannot be null."
			);
		}

		this.isFirstPlayersTurn = isFirstPlayersTurn;
		this.playState = playState;
	}


	/**
	 * @return True if it is the first player's turn; false if it is the second
	 * player's turn.
	 */
	public boolean isFirstPlayersTurn() {
		return isFirstPlayersTurn;
	}


	/**
	 * @return The context of the current player's turn.
	 */
	public PlayState playState() {
		return playState;
	}
}
