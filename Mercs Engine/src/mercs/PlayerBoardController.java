package mercs;

import java.util.HashMap;
import java.util.Map;

import main.Board;
import main.Move;
import main.RecordBasedMoveLogic;



public class PlayerBoardController {
//	private final Map<Integer, Map<Integer, RecordBasedMoveLogic>>
//		playerToPieceToML;
	private final Map<Integer, UpdatingPlayMaker> playerToPlayMaker;


	public PlayerBoardController(
		Board board,
		Map<Integer, Map<Integer, RecordBasedMoveLogic>> playerToPieceToML
	) {
		//TODO: Add check for repeat pieces
		this.playerToPlayMaker = new HashMap<Integer, UpdatingPlayMaker>();
		for(Integer player : playerToPieceToML.keySet()) {
			playerToPlayMaker.put(
				player,
				new UpdatingPlayMaker(board, playerToPieceToML.get(player))
			);
		}
//		this.playerToPieceToML = 
//			new HashMap<Integer, Map<Integer, RecordBasedMoveLogic>>();
//
//		Map<Integer, RecordBasedMoveLogic> allPiecesToML = 
//				new HashMap<Integer, RecordBasedMoveLogic>();
//
//		for(Integer player : playerToPieceToML.keySet()) {
//			Map<Integer, RecordBasedMoveLogic> pieceToML =
//					new HashMap<Integer, RecordBasedMoveLogic>(
//						playerToPieceToML.get(player)
//					);
//			this.playerToPieceToML.put(player, pieceToML);
//			
//			allPiecesToML.putAll(playerToPieceToML.get(player));
//		}
//
//		this.playMaker = new UpdatingPlayMaker(board, allPiecesToML);
	}


	public Board makePlay(Integer piece, int playIndex) {
		for(Integer player : playerToPlayMaker.keySet()) {
			UpdatingPlayMaker playMaker = playerToPlayMaker.get(player);
			if(playMaker.pieces().contains(piece)) {
				return playMaker.makePlay(piece, playIndex);
			}
		}
		return null;
	}


	public Map<Integer, Move[][]> pieceToPlays(Integer player) {
		return playerToPlayMaker.get(player).pieceToPlays();
	}


	public Map<Integer, Map<Integer, RecordBasedMoveLogic>> 
	update(Board board, Move[] play) {
		Map<Integer, Map<Integer, RecordBasedMoveLogic>> playerToPieceToML =
			new HashMap<Integer, Map<Integer, RecordBasedMoveLogic>>();

		for(Integer player : playerToPlayMaker.keySet()) {
			UpdatingPlayMaker playerPlayMaker = playerToPlayMaker.get(player);
			playerToPieceToML.put(
				player, playerPlayMaker.update(board, play)
			);
		}
		
		return playerToPieceToML;
	}
}