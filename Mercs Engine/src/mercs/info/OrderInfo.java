package mercs.info;

import java.util.Arrays;
import java.util.List;

import mercs.TurnState;



/**
 * Describes the state of turns for a game of Mercs. This is opposed to
 * describing the state of _a_ turn, which would be a TurnState. Instead of
 * just describing the current turn of a game, OrderInfo also describes the
 * order of turns within a game.
 * @author trevor
 */
public final class OrderInfo {
	private final TurnState turn;
	private final Integer firstPlayer;
	private final Integer secondPlayer;


	/**
	 * @param turn the state of the turn. The currentPlayer of turn must be
	 * either firstPlayer or secondPlayer. (non-null)
	 * @param firstPlayer the player who takes the first turn of the game and
	 * and takes the turn that comes after the second player's turn. (non-null)
	 * @param secondPlayer the player who takes the turn that comes after the
	 * first player's turn. They should not be the same player as the second
	 * player. (non-null)
	 */
	public OrderInfo(
		TurnState turn,
		Integer firstPlayer,
		Integer secondPlayer
	) {
		handleExceptions(turn, firstPlayer, secondPlayer);

		this.turn = turn;
		this.firstPlayer = firstPlayer;
		this.secondPlayer = secondPlayer;
	}


	/**
	 * @return the state of the turn.
	 */
	public TurnState turn() {
		return turn;
	}


	/**
	 * @return the order that each player takes their turn.
	 */
	public List<Integer> turnOrder() {
		return Arrays.asList(firstPlayer, secondPlayer);
	}


	/**
	 * @return the first player.
	 */
	public Integer firstPlayer() {
		return firstPlayer;
	}


	/**
	 * @return the second player.
	 */
	public Integer secondPlayer() {
		return secondPlayer;
	}


	/**
	 * Throws exceptions if any of the arguments aren't allowed for the
	 * constructor.
	 */
	private static void handleExceptions(
		TurnState turn,
		Integer firstPlayer,
		Integer secondPlayer
	) {
		if(turn == null) {
			throw new NullPointerException(
				"turn is null: It isn't anybody's turn."
			);
		}
		if(firstPlayer == null) {
			throw new NullPointerException(
				"firstPlayer is null: There isn't a player takes the first "
				+ "turn of the game and takes the turn that comes after the "
				+ "second player's turn."
			);
		}
		if(secondPlayer == null) {
			throw new NullPointerException(
				"secondPlayer is null: There isn't a player takes the turn "
				+ "that comes after the first player's turn."
			);
		}
		if(firstPlayer == secondPlayer) {
			throw new IllegalArgumentException(
				"firstPlayer and secondPlayer are the same: There is only one "
				+ "person playing, when there should be two."
			);
		}
		if(
			turn.currentPlayer() != firstPlayer 
			&& turn.currentPlayer() != secondPlayer
		) {
			throw new IllegalArgumentException(
				"currentPlayer of turn isn't firstPlayer or secondPlayer: The "
				+ "current turn isn't being taken by a recognized player."
			);
		}
	}


	public String toString() {
		String toString = "{turn: " + turn + ", firstPlayer: " + firstPlayer;
		toString += ", secondPlayer: " + secondPlayer + "}";
		return toString;
	}
}
