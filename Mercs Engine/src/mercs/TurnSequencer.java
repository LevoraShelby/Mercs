package mercs;

import java.util.HashMap;
import java.util.Map;

import mercs.info.OrderInfo;



/**
 * Decides a TurnState given a prior TurnState, the order of players, and the
 * cooldowns for those players.
 * @author trevor
 */
public class TurnSequencer {
	private final OrderInfo order;
	private final Map<Integer, Integer> playerToCooldown;


	/**
	 * Constructs a TurnSequencer.
	 * @param order
	 * @param playerToCooldown
	 */
	public TurnSequencer(
		OrderInfo order,
		Map<Integer, Integer> playerToCooldown
	) {
		this.order = order;
		this.playerToCooldown = new HashMap<>(playerToCooldown);
	}


	/**
	 * If the current player is in the move round of their turn, and they do 
	 * not have an active cooldown (a non-zero cooldown), the next TurnState
	 * will be for the buy round of the same player. If the current player is
	 * in the move round of their turn, but they do have an active cooldown,
	 * the next TurnState will be for the move round of the next player in the
	 * turn order. If the current player is in the buy round of their turn, the
	 * next TurnState will be for the move round of the next player in the turn
	 * order.
	 * @return The next TurnState.
	 */
	public TurnState next() {
		PlayState playState = order.turn().playState();
		int currentPlayerCooldown = playerToCooldown.get(
			order.turn().currentPlayer()
		);

		if(
			playState == PlayState.MOVE &&
			currentPlayerCooldown != 0
		) {
			return new TurnState(nextPlayer(), PlayState.MOVE);
		}

		else if(
			playState == PlayState.MOVE &&
			currentPlayerCooldown == 0
		) {
			return new TurnState(order.turn().currentPlayer(), PlayState.BUY);
		}

		else if(playState == PlayState.BUY) {
			return new TurnState(nextPlayer(), PlayState.MOVE);
		}

		else {
			throw new RuntimeException("Unsupported PlayState");
		}
	}


	/**
	 * @return The player who comes after the current player. If it's the first
	 * player, it will be the second player, and if it's the second player, it
	 * will be the first player.
	 */
	private Integer nextPlayer() {
		if( order.turn().currentPlayer().equals(order.firstPlayer()) ) {
			return order.secondPlayer();
		}
		else {
			return order.firstPlayer();
		}
	}
}