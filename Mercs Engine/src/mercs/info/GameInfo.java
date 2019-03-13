package mercs.info;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import main.Board;



/**
 * Describes the status of a game of Mercs. It's everything you need to know
 * about what is going on.
 * 
 * TODO: Add documentation here that describes the game of Mercs.
 * @author trevor
 */
public class GameInfo {
	private final Board board;
	private final Map<Integer, PieceInfo> pieceToInfo;
	private final Map<Integer, PlayerInfo> playerToInfo;
	private final OrderInfo order;


	/**
	 * @param board the board that this game is being played on. (non-null)
	 * @param pieceToInfo a mapping for each piece to the information that
	 * describes it in this game. pieceToInfo should not have a key that is 
	 * null, nor a value that is null. (non-null)
	 * @param playerToInfo a mapping for each player to the information that
	 * describes it in this game. playerToInfo should not have a key that is 
	 * null, nor a value that is null. playerToInfo should not contain any 
	 * PlayerInfo where pieces() would return with a key set that contains a
	 * piece not described by pieceToInfo (more simply, none of the players
	 * should own pieces that aren't in this game). (non-null)
	 * @param order the order of this game. (non-null)
	 */
	public GameInfo(
		Board board,
		Map<Integer, PieceInfo> pieceToInfo,
		Map<Integer, PlayerInfo> playerToInfo,
		OrderInfo order
	) {
		handleExceptions(board, pieceToInfo, playerToInfo, order);

		this.board = board;
		this.pieceToInfo = new HashMap<Integer, PieceInfo>(pieceToInfo);
		this.playerToInfo = new HashMap<Integer, PlayerInfo>(playerToInfo);
		this.order = order;
	}


	/**
	 * @return the board that this game is being played on.
	 */
	public Board board() {
		return board;
	}


	/**
	 * @return A mapping for each piece to the information that describes it
	 * in this game.
	 */
	public Map<Integer, PieceInfo> pieceToInfo() {
		return new HashMap<Integer, PieceInfo>(pieceToInfo);
	}


	/**
	 * @return A mapping for each player to the information that describes it
	 * in this game.
	 */
	public Map<Integer, PlayerInfo> playerToInfo() {
		return new HashMap<Integer, PlayerInfo>(playerToInfo);
	}


	/**
	 * @return 
	 */
	public OrderInfo order() {
		return order;
	}


	/**
	 * Throws exceptions if any of the arguments aren't allowed for the
	 * constructor.
	 */
	private static void handleExceptions(
		Board board,
		Map<Integer, PieceInfo> pieceToInfo,
		Map<Integer, PlayerInfo> playerToInfo,
		OrderInfo order
	) {
		if(board == null) {
			throw new NullPointerException(
				"board is null: There isn't a board to play on."
			);
		}
		if(pieceToInfo == null) {
			throw new NullPointerException(
				"pieceToInfo is null: There isn't a set of pieces to play "
				+ "with."
			);
		}
		if(pieceToInfo.containsKey(null)) {
			throw new NullPointerException(
				"pieceToInfo contains a null key: This game has a piece that "
				+ "doesn't exist."
			);
		}
		if(pieceToInfo.containsValue(null)) {
			throw new NullPointerException(
				"pieceToInfo contains a null value: There exists a piece that "
				+ "can't be described."
			);
		}
		if(!pieceToInfo.keySet().containsAll(board.pieces())) {
			throw new IllegalArgumentException(
				"board has pieces that pieceToInfo does not: The board holds "
				+ "pieces that this game cannot describe."
			);
		}
		/**
		 * Note: pieceToInfo can describe pieces that aren't on or off the
		 * board. They're still in the game, it's just that the board doesn't
		 * hold them. The board having pieces that pieceToInfo does not have is
		 * a problem, because then you have pieces that *are* in the game (they
		 * have to be -- they're on the board), but can't be described, which
		 * is something that GameInfo should be able to do for every piece it
		 * has.
		 */
		if(playerToInfo == null) {
			throw new NullPointerException(
				"playerToInfo is null: There isn't a group of players to play "
				+ "this game."
			);
		}
		if(playerToInfo.containsKey(null)) {
			throw new NullPointerException(
				"playerToInfo contains a null key: This game has a player "
				+ "that doesn't exist."
			);
		}
		if(playerToInfo.containsValue(null)) {
			throw new NullPointerException(
				"playerToInfo contains a null value: There exists a player "
				+ "that can't be described."
			);
		}
		Integer pieceOwnedOutOfGame = pieceOwnedOutOfGame(
			pieceToInfo.keySet(), playerToInfo.values()
		);
		if(pieceOwnedOutOfGame != null) {
			throw new IllegalArgumentException(
				"playerToInfo describes a player who owns a piece that "
				+ "pieceToInfo does not contain: There exists a player that "
				+ "that owns a piece that does not exist in this game."
			);
		}
		if(order == null) {
			throw new NullPointerException(
				"order is null: This game has no order to play by."
			);
		}
		if(!playerToInfo.keySet().containsAll(order.turnOrder())) {
			throw new IllegalArgumentException(
				"playerToInfo is missing players from order: Players that are "
				+ "playing this game can't be described by it."
			);
		}
	}


	/**
	 * Finds a piece that a player owns that isn't a piece in the game.
	 * @param piecesInGame all of the pieces in the game.
	 * @param infoOfPlayers information for all of the players in the game.
	 * @return a piece. If all the players' pieces exist in the game, this
	 * method returns null.
	 */
	private static Integer pieceOwnedOutOfGame(
		Collection<Integer> piecesInGame,
		Collection<PlayerInfo> infoOfPlayers
	) {
		/**
		 * Goes through each piece of each player, checking that each piece is
		 * being held in piecesInGame as well. If a piece isn't being held in
		 * piecesInGame, that piece is returned.
		 */
		for(PlayerInfo playerInfo : infoOfPlayers) {
			for(Integer ownedPiece : playerInfo.pieces()) {
				if(!piecesInGame.contains(ownedPiece)) {
					return ownedPiece;
				}
			}
		}

		return null;
	}


	public String toString() {
		String toString = "{board: " + board + ", pieceToInfo: " + pieceToInfo;
		toString += ", playerToInfo" + playerToInfo + ", order: " + order;
		toString += "}";
		return toString;
	}
}
