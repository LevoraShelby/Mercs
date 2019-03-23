package mercs;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import main.Board;
import main.Move;
import main.RecordBasedMoveLogic;
import main.Tile;
import mercs.info.GameInfo;
import mercs.info.PieceInfo;
import mercs.info.PlayerInfo;
import mercs.pieceLogics.AlliedLogic;
import mercs.pieceLogics.Mann;
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
		if(info.order().turn().playState() != PlayState.MOVE) {
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
		Set<Integer> piecesOfCurrentPlayer = info
			.playerToInfo()
			.get(info.order().turn().currentPlayer())
			.pieces();

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
	 * @param playingPiece The piece that is making the play.
	 * @param playIndex An index that points to a play in the array of plays
	 * that the given piece can make.
	 * @return The game of Mercs after the current player decides to make the
	 * given play.
	 */
	public GameInfo play(Integer playingPiece, int playIndex) {
		Move[] play = pieceToPlays().get(playingPiece)[playIndex];

		Board board = newBoard(play);
		Map<Integer, PieceInfo> pieceToInfo = newPieceToInfo(board, play);
		Map<Integer, PlayerInfo> playerToInfo = newPlayerToInfo(play);
		OrderInfo order = nextOrder();

		//Promotes pieces if necessary
		for(Integer pieceBeingPromoted : pieceToInfo.keySet()) {
			//Checks that pieceBeingPromoted can be promoted
			PieceType typeBeingPromoted = 
				pieceToInfo.get(pieceBeingPromoted).type();
			if(
				typeBeingPromoted != PieceType.PAWN 
				&& typeBeingPromoted != PieceType.COMMANDO
			) {
				continue;
			}

			//Checks that the piece is on the board
			if(board.tileForPiece(pieceBeingPromoted) == null) {
				continue;
			}

			/**
			 * Checks that pieceForPromotion should be promoted (i.e. the piece
			 * has reached the end of the board)
			 * NOTE: This is a quick-and-dirty check. It only checks if there
			 * is a tile ahead of the piece. It may need polishing for other
			 * implementations.
			 */
			Tile tileAheadOfPiece;
			boolean pieceIsWhite = 
					playerToInfo.get(order.firstPlayer())
					.pieces().contains(pieceBeingPromoted);
			if(pieceIsWhite) {
				tileAheadOfPiece = 
					board.tileForPiece(pieceBeingPromoted)
					.add(new Tile(1, 0));
			}
			else {
				tileAheadOfPiece =
					board.tileForPiece(pieceBeingPromoted)
					.add(new Tile(-1, 0));
			}
			if(board.tiles().contains(tileAheadOfPiece)) {
				continue;
			}

			//If all checks have been pass, the piece is promoted to a Mann.
			Set<Integer> allyPieces;
			if(pieceIsWhite) {
				allyPieces = playerToInfo.get(order.firstPlayer()).pieces();
			}
			else {
				allyPieces = playerToInfo.get(order.secondPlayer()).pieces();
			}
			pieceToInfo.put(
				pieceBeingPromoted,
				new PieceInfo(
					typeBeingPromoted,
					new AlliedLogic(
						new Mann(pieceBeingPromoted, board),
						allyPieces
					)
				)
			);
		}

		return new GameInfo(board, pieceToInfo, playerToInfo, order);
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
	 * @return A mapping of each player to their information after play is
	 * made.
	 */
	private Map<Integer, PlayerInfo> newPlayerToInfo(Move[] play) {
		Map<Integer, PlayerInfo> playerToInfo = info.playerToInfo();
		Integer currentPlayer = info.order().turn().currentPlayer();

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
				&& !info
					.playerToInfo().get(currentPlayer)
					.pieces().contains(move.piece())
			) {
				numPiecesCapturedDuringPlay++;
			}
		}

		//Adds the pieces that current player captured to their total.
		PlayerInfo currentPlayerInfo = info.playerToInfo().get(currentPlayer);
		currentPlayerInfo = new PlayerInfo(
			currentPlayerInfo.pieces(),
			currentPlayerInfo.numPiecesCaptured()
				+ numPiecesCapturedDuringPlay,
			currentPlayerInfo.cooldown()
		);

		playerToInfo.put(currentPlayer, currentPlayerInfo);
		return playerToInfo;
	}


	/**
	 * @return The order for this game of Mercs after a play is made.
	 */
	private OrderInfo nextOrder() {
		TurnState turn;

		Integer currentPlayer = info.order().turn().currentPlayer();
		//If the current player has no cooldown, it becomes their turn to buy.
		if(info.playerToInfo().get(currentPlayer).cooldown() == 0) {
			turn = new TurnState(currentPlayer, PlayState.BUY);
		}
		/**
		 * If the current player has cooldown, it becomes the next player's
		 * turn to move.
		 */
		else {
			turn = new TurnState(otherPlayer(currentPlayer), PlayState.MOVE);
		}

		OrderInfo order = new OrderInfo(
			turn,
			info.order().firstPlayer(), info.order().secondPlayer()
		);
		return order;
	}


	/**
	 * @param player
	 * @return The other player in the game.
	 */
	private Integer otherPlayer(Integer player) {
		//Returns the second player if the given player is the first player.
		if(info.order().firstPlayer() == player) {
			return info.order().secondPlayer();
		}
		//Returns the first player if the given player is the second player.
		else if(info.order().secondPlayer() == player) {
			return info.order().firstPlayer();
		}
		/**
		 * Throws an exception if the given player is neither the first or
		 * second player.
		 */
		else {
			throw new IllegalArgumentException(
				"player is not the first or second player."
			);
		}
	}
}
