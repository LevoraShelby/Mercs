package mercs;

import java.util.HashMap;
import java.util.Map;



/**
 * TODO: Document
 * TODO: Extract interface
 * @author trevor
 */
public final class PieceTypeShop {
	private final Map<Integer, Integer> playerToCooldown;


	public PieceTypeShop(Map<Integer, Integer> playerToCooldown) {
		this.playerToCooldown = 
			new HashMap<Integer, Integer>(playerToCooldown);		
	}


	/**
	 * @param player
	 * @param pieceType
	 * @return new playerToCooldown mapping
	 */
	public Map<Integer, Integer> buy(Integer player, PieceType pieceType) {
		Map<Integer, Integer> newPlayerToCooldown =
			new HashMap<Integer, Integer>(playerToCooldown);

		if(canPlayerBuy(player)) {
			newPlayerToCooldown.put(player, cooldown(pieceType));
		}
		else {
			throw new RuntimeException(
				"Player " + player + " is still in cooldown."
			);
		}

		return newPlayerToCooldown;
	}


	public static int cooldown(PieceType pieceType) {
		switch(pieceType) {
			case PAWN:
				return 1;
			case FERZ:
			case WAZIR:
				return 3;
			case KNIGHT:
			case COMMANDO:
				return 5;
			default:
				throw new RuntimeException("Unsupported PieceType.");
		}
	}


	/**
	 * @param player The current player.
	 * @return Whether or not the current player can buy.
	 */
	public boolean canPlayerBuy(Integer player) {
		return playerToCooldown.get(player) == 0;
	}


	public static void main(String[] args) {
		Map<Integer, Integer> playerToCooldown = 
			new HashMap<Integer, Integer>();
		playerToCooldown.put(0, 0);
		playerToCooldown.put(1, 4);
		System.out.println(playerToCooldown);

		PieceTypeShop shop = new PieceTypeShop(playerToCooldown);
		playerToCooldown = shop.buy(0, PieceType.KNIGHT);
		System.out.println(playerToCooldown);
		playerToCooldown.put(1, 0);
	}
}
