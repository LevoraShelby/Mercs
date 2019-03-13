package mercs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.Board;
import main.Move;
import main.RecordBasedMoveLogic;
import main.Tile;
import test.Forward;



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


	public static void main(String[] args) {
		List<Tile> pieceLocations = new ArrayList<Tile>();
		pieceLocations.add(new Tile(1, 1));
		pieceLocations.add(new Tile(3, 1));
		Board board = Board.squareBoard(5, pieceLocations);

		Map<Integer, RecordBasedMoveLogic> player1Pieces =
			new HashMap<Integer, RecordBasedMoveLogic>();
		Map<Integer, RecordBasedMoveLogic> player2Pieces =
			new HashMap<Integer, RecordBasedMoveLogic>();

		player1Pieces.put(0, new Forward(0, board));
		player2Pieces.put(1, new Forward(1, board));

		Map<Integer, Map<Integer, RecordBasedMoveLogic>> playerToPieceToML =
			new HashMap<Integer, Map<Integer, RecordBasedMoveLogic>>();
		playerToPieceToML.put(1, player1Pieces);
		playerToPieceToML.put(2, player2Pieces);

		System.out.println(board);
		PlayerBoardController pbc;
		for(int i = 0; i < 5; i++) {
			pbc = new PlayerBoardController(board, playerToPieceToML);
			board = pbc.makePlay((i % 2), 0);
			System.out.println(board);
			playerToPieceToML = pbc.update(board, pbc.pieceToPlays((i % 2) + 1).get(i % 2)[0]);
		}
	}
}