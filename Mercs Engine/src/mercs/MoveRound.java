package mercs;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import main.Board;
import main.Move;
import main.RecordBasedMoveLogic;
import mercs.info.GameInfo;
import mercs.info.PieceInfo;
import mercs.info.PlayerInfo;
import mercs.info.OrderInfo;



/**
 * Represents the available options for a round of movement for the current
 * player in a game of Mercs. The current player is able to look at the plays
 * that each of their pieces that are currently on the board can make. They can
 * also choose one of these pieces to make a specified play.
 * @author trevor
 */
public final class MoveRound {
	private final GameInfo info;


	/**
	 * @param info A game of Mercs that is currently in its move round.
	 */
	public MoveRound(GameInfo info) {
		if(info.order().playState() != PlayState.MOVE) {
			throw new IllegalArgumentException(
				"info does not describe a game of Mercs in its move round."
			);
		}
		this.info = info;
	}


	/**
	 * @return A mapping of each piece of the current player's that could make
	 * a play to all the plays that the piece could make.
	 */
	public Map<Integer, Move[][]> pieceToPlays() {
		/**
		 * Gets the current player's pieces in order to get those pieces'
		 * plays.
		 */
		Set<Integer> piecesOfCurrentPlayer = info.currentPlayerInfo().pieces();

		/**
		 * Removes every piece of the current player's that isn't on the board,
		 * since those pieces cannot make plays.
		 */
		Iterator<Integer> piecesIter = piecesOfCurrentPlayer.iterator();
		while(piecesIter.hasNext()) {
			if(info.board().tileForPiece(piecesIter.next()) == null) {
				piecesIter.remove();
			}
		}

		//Gets the plays for each piece and maps them to that piece.
		Map<Integer, Move[][]> pieceToPlays = new HashMap<>();
		for(Integer piece : piecesOfCurrentPlayer) {
			Move[][] playsForPiece = info
				.pieceToInfo().get(piece)
				.logic().plays();
			pieceToPlays.put(piece, playsForPiece);
		}
		return pieceToPlays;
	}


	/**
	 * @param piece A piece of the current player's that is on the board.
	 * @param playIndex An index that points to a play in the array of plays
	 * that the given piece can make.
	 * @return The game of Mercs after the current player decides to make the
	 * given play.
	 */
	public GameInfo play(Integer piece, int playIndex) {
		Move[] play = pieceToPlays().get(piece)[playIndex];

		Board board = newBoard(play);
		Map<Integer, PieceInfo> pieceToInfo = newPieceToInfo(board, play);

		PlayerInfo firstPlayerInfo; PlayerInfo secondPlayerInfo;
		if(info.order().isFirstPlayersTurn()) {
			firstPlayerInfo = newCurrentPlayerInfo(play);
			secondPlayerInfo = info.secondPlayerInfo();
		}
		else {
			firstPlayerInfo = info.firstPlayerInfo();
			secondPlayerInfo = newCurrentPlayerInfo(play);
		}
		OrderInfo order = nextOrder();

		return new GameInfo(
			board, pieceToInfo, firstPlayerInfo, secondPlayerInfo, order
		);
	}


	/**
	 * @param play A play being made on the board.
	 * @return The board after the given play is made on it.
	 */
	private Board newBoard(Move[] play) {
		Board board = info.board();
		for(Move move : play) {
			board = board.makeMove(move);
		}
		return board;
	}


	/**
	 * @param board The board that the given play creates from the previous
	 * board when that play is made.
	 * @param play A play being made that could affect the pieces in the game.
	 * @return A mapping of each piece to it's information after play is made.
	 */
	private Map<Integer, PieceInfo> newPieceToInfo(Board board, Move[] play) {
		/**
		 * Maps each piece to its information, changing its logic to an updated
		 * version.
		 */
		Map<Integer, PieceInfo> pieceToInfo = new HashMap<>();
		for(Integer piece : info.pieceToInfo().keySet()) {
			//Reuses the type for each piece.
			PieceType type = info.pieceToInfo().get(piece).type();

			//Updates the logic for each piece.
			RecordBasedMoveLogic logic = info
				.pieceToInfo().get(piece)
				.logic().update(board, play);

			PieceInfo pieceInfo = new PieceInfo(type, logic);
			pieceToInfo.put(piece, pieceInfo);
		}

		return pieceToInfo;
	}


	/**
	 * @param play A play being made (by the current player) that could affect
	 * the total number of pieces that the current player has captured.
	 * @return The current player's information after play is made.
	 */
	private PlayerInfo newCurrentPlayerInfo(Move[] play) {
		/**
		 * Calculates the number of pieces that the current player captured
		 * during the play. For a piece to be "captured" by a player, it has to
		 * be taken off the board and the piece being taken off cannot belong
		 * to that player.
		 */
		int numPiecesCapturedDuringPlay = 0;
		for(Move move : play) {
			if(
				move.newPosition() == null
				&& !info.currentPlayerInfo().pieces().contains(move.piece())
			) {
				numPiecesCapturedDuringPlay++;
			}
		}

		//Adds the pieces that current player captured to their total.
		PlayerInfo currentPlayerInfo = new PlayerInfo(
			info.currentPlayerInfo().pieces(),
			info.currentPlayerInfo().numPiecesCaptured()
				+ numPiecesCapturedDuringPlay,
			info.currentPlayerInfo().cooldown()
		);
		return currentPlayerInfo;
	}


	/**
	 * @return The order for this game of Mercs after a play is made.
	 */
	private OrderInfo nextOrder() {
		//If the current player has no cooldown, it becomes their turn to buy.
		if(info.currentPlayerInfo().cooldown() == 0) {
			return new OrderInfo(
				info.order().isFirstPlayersTurn(), PlayState.BUY
			);
		}
		/**
		 * If the current player has cooldown, it becomes the next player's
		 * turn to move.
		 */
		else {
			return new OrderInfo(
				!info.order().isFirstPlayersTurn(), PlayState.MOVE
			);
		}
	}
}
