package test;

import java.util.HashMap;
import java.util.Map;

import mercs.PlayState;
import mercs.TurnSequencer;
import mercs.TurnState;
import mercs.info.OrderInfo;

public class TurnTesting {
	public static void main(String[] args) {
		Integer player1 = 1;
		Integer player2 = 2;
		TurnState turn = new TurnState(player1, PlayState.MOVE);
		OrderInfo order = new OrderInfo(
			turn,
			player1, player2
		);

		Map<Integer, Integer> playerToCooldown = new HashMap<>(2);
		playerToCooldown.put(order.firstPlayer(), 0);
		playerToCooldown.put(order.secondPlayer(), 0);

		TurnSequencer seq = new TurnSequencer(
			order, playerToCooldown
		);

		System.out.println(order.turn());
		System.out.println(playerToCooldown);
		System.out.println();
		for(int i = 0; i < 10; i++) {
			order = new OrderInfo(
				seq.next(),
				order.firstPlayer(), order.secondPlayer()
			);
			if(i == 1) {
				playerToCooldown.put(order.firstPlayer(), 3);
				playerToCooldown.put(order.secondPlayer(), 3);
			}
			seq = new TurnSequencer(order, playerToCooldown);
			System.out.println(order.turn());
			System.out.println(playerToCooldown);
			System.out.println();
		}
	}
}
