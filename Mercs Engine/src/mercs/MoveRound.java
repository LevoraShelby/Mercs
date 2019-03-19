package mercs;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import main.Board;
import main.Move;
import main.MoveLogic;
import main.RecordBasedMoveLogic;
import mercs.info.GameInfo;
import mercs.info.OrderInfo;
import mercs.info.PieceInfo;
import mercs.info.PlayerInfo;



/**
 * This represents the options available to the current player in a round of 
 * movement in a game of Mercs.
 * @author trevor
 */
public class MoveRound {
	private final GameInfo info;


	/**
	 * @param info The description for the game of this round. The order for
	 * info must have a PlayState of MOVE. (non-null)
	 */
	public MoveRound(GameInfo info) {
		handleExceptions(info);
		this.info = info;
	}


	/**
	 * @return The different plays that the current player can make, given
	 * the pieces they control.
	 */
	public Map<Integer, Move[][]> pieceToPlays() {
		//Gets the current player's pieces
		Integer currentPlayer = info.order().turn().currentPlayer();
		Set<Integer> currentPlayerPieces = 
			info.playerToInfo().get(currentPlayer).pieces();

		/**
		 * Using the current player's pieces, builds a pieceToPlays map using
		 * each piece's MoveLogic.
		 */
		Map<Integer, Move[][]> pieceToPlays = new HashMap<>();
		for(Integer piece : currentPlayerPieces) {
			MoveLogic pieceLogic = info.pieceToInfo().get(piece).logic();
			pieceToPlays.put(piece, pieceLogic.plays());
		}

		return pieceToPlays;
	}


	/**
	 * @param piece The piece that the current player is calling upon for this
	 * play.
	 * @param playIndex The index for the play from piece's available plays.
	 * @return The result of the game this round belongs to, after having
	 * performed the described play.
	 */
	public GameInfo play(Integer piece, int playIndex) {
		Move[] play = pieceToPlays().get(piece)[playIndex];
		Board newBoard = boardAfterPlay(play);

		PlayerInfo newCurrentPlayerInfo = currentPlayerInfoAfterPlay(play);
		Map<Integer, PlayerInfo> newPlayerToInfo = info.playerToInfo();
		newPlayerToInfo.put(
			info.order().turn().currentPlayer(),
			newCurrentPlayerInfo
		);

		Map<Integer, PieceInfo> newPieceToInfo = pieceToInfoAfterPlay(play);

		Map<Integer, Integer> playerToCooldown = new HashMap<>();
		for(Integer player : info.playerToInfo().keySet()) {
			int cooldown = info.playerToInfo().get(player).cooldown();
			playerToCooldown.put(player, cooldown);
		}
		TurnSequencer sequencer = new TurnSequencer(
			info.order(),
			playerToCooldown
		);
		OrderInfo newOrder = new OrderInfo(
			sequencer.next(),
			info.order().firstPlayer(),
			info.order().secondPlayer()
		);

		return new GameInfo(
			newBoard, newPieceToInfo, newPlayerToInfo, newOrder
		);
	}


	/**
	 * @param play The play being performed.
	 * @return The resultant Board if play were performed.
	 */
	private Board boardAfterPlay(Move[] play) {
		Board newBoard = info.board();
		for(Move move : play) {
			newBoard = newBoard.makeMove(move);
		}
		return newBoard;
	}


	/**
	 * @param play The play being performed.
	 * @return The number of pieces that do not belong to the current player.
	 * that would be moved from on the board to off the board if play were
	 * performed.
	 */
	private int numPiecesCapturedInPlay(Move[] play) {
		int numPiecesCapturedInPlay = 0;

		Board newBoard = info.board();

		Integer currentPlayer = info.order().turn().currentPlayer();
		PlayerInfo currentPlayerInfo = info.playerToInfo().get(currentPlayer);
		for(Move move : play) {
			/**
			 * Checks if piece that does not belong to the current player is 
			 * being moved from the board to off of it. This is what
			 * constitutes as a "capture".
			 */
			if(
				move.newPosition() == null 
				&& newBoard.tileForPiece(move.piece()) != null
				&& !currentPlayerInfo.pieces().contains(move.piece())
			) {
				numPiecesCapturedInPlay++;
			}
			newBoard = newBoard.makeMove(move);
		}

		return numPiecesCapturedInPlay;
	}


	/**
	 * @param play The play being performed.
	 * @return What the current player's information would be if play were
	 * performed.
	 */
	private PlayerInfo currentPlayerInfoAfterPlay(Move[] play) {
		int numPiecesCapturedInPlay = numPiecesCapturedInPlay(play);
		Integer currentPlayer = info.order().turn().currentPlayer();
		PlayerInfo currentPlayerInfoBeforePlay = 
			info.playerToInfo().get(currentPlayer);
		int cooldown = currentPlayerInfoBeforePlay.cooldown();
		if(cooldown > 0) cooldown--;

		return new PlayerInfo(
			currentPlayerInfoBeforePlay.pieces(),
			currentPlayerInfoBeforePlay.numPiecesCaptured() 
				+ numPiecesCapturedInPlay,
			cooldown
		);
	}


	/**
	 * @param play The play being performed.
	 * @return What the information for every piece would be if play were
	 * performed.
	 */
	private Map<Integer, PieceInfo> pieceToInfoAfterPlay(Move[] play) {
		//Gets the logic that needs to be updated for each piece.
		Map<Integer, RecordBasedMoveLogic> pieceToLogic = new HashMap<>();
		for(Integer piece : info.pieceToInfo().keySet()) {
			pieceToLogic.put(piece, info.pieceToInfo().get(piece).logic());
		}
		//Uses an UpdatingPlayMaker to handle the update.
		Map<Integer, RecordBasedMoveLogic> pieceToLogicAfterPlay = 
			new UpdatingPlayMaker(
					info.board(), 
					pieceToLogic
			).update(boardAfterPlay(play), play);

		//Creates an updated version of each piece, based on play.
		Map<Integer, PieceInfo> pieceToInfoAfterPlay = new HashMap<>();
		for(Integer piece : pieceToLogicAfterPlay.keySet()) {
			PieceType type = info.pieceToInfo().get(piece).type();
			//Creates the updated piece with it's same type and new logic.
			pieceToInfoAfterPlay.put(
				piece,
				new PieceInfo(
					type,
					pieceToLogicAfterPlay.get(piece)
				)
			);
		}

		return pieceToInfoAfterPlay;
	}


	/**
	 * Throws exceptions if any of the arguments aren't allowed for the
	 * constructor.
	 */
	private static void handleExceptions(GameInfo info) {
		if(info == null) {
			throw new NullPointerException(
				"info is null: There isn't a game for this round to belong to."
			);
		}
		if(info.order().turn().playState() != PlayState.MOVE) {
			throw new IllegalArgumentException(
				"playState of info isn't MOVE: MoveRound can't occur when its "
				+ "game isn't in a MOVE playState."
			);
		}
	}
}
