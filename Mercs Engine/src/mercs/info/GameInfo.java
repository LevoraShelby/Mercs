package mercs.info;

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
	private final PlayerInfo firstPlayerInfo;
	private final PlayerInfo secondPlayerInfo;
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
		PlayerInfo firstPlayerInfo, PlayerInfo secondPlayerInfo,
		OrderInfo order
	) {
		handleExceptions(
			board, pieceToInfo, firstPlayerInfo, secondPlayerInfo, order
		);

		this.board = board;
		this.pieceToInfo = new HashMap<Integer, PieceInfo>(pieceToInfo);
		this.firstPlayerInfo = firstPlayerInfo;
		this.secondPlayerInfo = secondPlayerInfo;
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
	 * @return A description of the first player.
	 */
	public PlayerInfo firstPlayerInfo() {
		return firstPlayerInfo;
	}


	/**
	 * @return A description of the second player.
	 */
	public PlayerInfo secondPlayerInfo() {
		return secondPlayerInfo;
	}


	/**
	 * @return A description of the current player.
	 */
	public PlayerInfo currentPlayerInfo() {
		if(order.isFirstPlayersTurn()) {
			return firstPlayerInfo;
		}
		else {
			return secondPlayerInfo;
		}
	}


	/**
	 * @param isFirstPlayer 
	 * @return If isFirstPlayer is true, this returns firstPlayerInfo; if it's
	 * false if returns secondPlayerInfo;
	 */
	public PlayerInfo playerInfo(boolean isFirstPlayer) {
		if(isFirstPlayer) {
			return firstPlayerInfo;
		}
		else {
			return secondPlayerInfo;
		}
	}


	/**
	 * @return The order of this game.
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
		PlayerInfo firstPlayerInfo, PlayerInfo secondPlayerInfo,
		OrderInfo order
	) {
		if(board == null) {
			throw new NullPointerException(
				"board is null."
			);
		}
		if(pieceToInfo == null) {
			throw new NullPointerException(
				"pieceToInfo is null."
			);
		}
		if(pieceToInfo.containsKey(null)) {
			throw new NullPointerException(
				"pieceToInfo contains a null key."
			);
		}
		if(pieceToInfo.containsValue(null)) {
			throw new NullPointerException(
				"pieceToInfo contains a null value."
			);
		}
		//Checks that the board has no undescribed pieces.
		for(Integer piece : board.pieces()) {
			if(!pieceToInfo.containsKey(piece)) {
				throw new IllegalArgumentException(
					"board contains the piece " + piece + ", but " +
					"pieceToInfo does not."
				);
			}
		}
		//Checks that non-existent pieces are not being described.
		for(Integer piece : pieceToInfo.keySet()) {
			if(!board.pieces().contains(piece)) {
				throw new IllegalArgumentException(
					"pieceToInfo contains the piece " + piece + ", but " +
					"board does not."
				);
			}
		}
		if(firstPlayerInfo == null) {
			throw new NullPointerException(
				"firstPlayerInfo is null."
			);
		}
		if(secondPlayerInfo == null) {
			throw new NullPointerException(
				"secondPlayerInfo is null."
			);
		}
		for(Integer piece : board.pieces()) {
			if(
				!firstPlayerInfo.pieces().contains(piece)
				&& !secondPlayerInfo.pieces().contains(piece)
			) {
				throw new IllegalArgumentException(
					"board contains the piece " + piece + ", but neither " +
					"player has it."
				);
			}
		}
		for(Integer piece : firstPlayerInfo.pieces()) {
			if(secondPlayerInfo.pieces().contains(piece)) {
				throw new IllegalArgumentException(
					"Both players have the piece " + piece + "."
				);
			}
		}
		for(Integer piece : firstPlayerInfo.pieces()) {
			if(!board.pieces().contains(piece)) {
				throw new IllegalArgumentException(
					"firstPlayerInfo contains the piece " + ", but the " +
					"board does not."
				);
			}
		}
		for(Integer piece : secondPlayerInfo.pieces()) {
			if(!board.pieces().contains(piece)) {
				throw new IllegalArgumentException(
					"secondPlayerInfo contains the piece " + ", but the " + 
					"board does not."
				);
			}
		}
		if(order == null) {
			throw new NullPointerException(
				"order is null."
			);
		}
	}


	public String toString() {
		String toString = "{board: " + board + ", pieceToInfo: " + pieceToInfo;
		toString += ", firstPlayerInfo:" + firstPlayerInfo;
		toString += ", secondPlayerInfo: " + secondPlayerInfo +  ", order: ";
		toString += order + "}";
		return toString;
	}
}
