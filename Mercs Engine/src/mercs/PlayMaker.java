package mercs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import main.Board;
import main.Move;
import main.MoveLogic;
import main.RecordBasedMoveLogic;
import main.Tile;
import test.Forward;



/**
 * This class executes a play for a piece, with the given information of the
 * different plays that each piece can make.
 * @author trevor
 */
public class PlayMaker {
	private final Board board;
	private final Map<Integer, MoveLogic> pieceToLogic;


	/**
	 * Constructs a PlayMaker.
	 * @param board the Board of the game.
	 * @param pieceToLogic a mapping of each piece to the MoveLogic that 
	 * describes which plays that piece can take.
	 */
	public PlayMaker(
		Board board, 
		Map<Integer, ? extends MoveLogic> pieceToLogic
	) {
		this.board = board;
		this.pieceToLogic = new HashMap<>(pieceToLogic);
	}


	/**
	 * Executes a given play from a given piece.
	 * @param piece the piece that is making the play.
	 * @param playIndex the index for the play in the given piece's MoveLogic's
	 * array of plays
	 * @return the Board that results from executing the given play.
	 */
	public Board makePlay(Integer piece, int playIndex) {
		Move[] play = pieceToLogic.get(piece).plays()[playIndex];

		/**
		 * For each move, set the new tileIndex to the newPosition of the Move.
		 * Check to see that there are no repeat tileIndexes (that aren't -1).
		 * If the newPosition of the Move is null, remove the piece from the
		 * board.
		 */
		Board newBoard = board;
		for(Move move : play) {
			newBoard = newBoard.makeMove(move);
		}
		
		return newBoard;
	}


	/**
	 * @return a mapping of each piece to the different plays which that piece
	 * can take.
	 */
	public Map<Integer, Move[][]> pieceToPlays() {
		Map<Integer,Move[][]> pieceToPlays = new HashMap<>();
		for(Integer piece : pieceToLogic.keySet()) {
			MoveLogic ml = pieceToLogic.get(piece);
			pieceToPlays.put(piece, ml.plays());
		}
		return pieceToPlays;
	}


	public List<Integer> pieces() {
		return new ArrayList<Integer>(pieceToLogic.keySet());
	}


	public static void main(String[] args) {
		Set<Tile> tiles = new HashSet<>(25);
		for(int rank = 1; rank <= 5; rank++) {
			for(int file = 1; file <= 5; file++) {
				tiles.add(new Tile(rank, file));
			}
		}
		
		Map<Integer, Tile> pieceToTile = new HashMap<>(2);
		pieceToTile.put(0, new Tile(1, 1));
		pieceToTile.put(1, new Tile(3, 1));
		
		Board board = new Board(tiles, pieceToTile);

		Map<Integer, RecordBasedMoveLogic> pieceToLogic = new HashMap<>();
		pieceToLogic.put(0, new Forward(0, board));


		System.out.println(board);

		PlayMaker pm;
		for(int i = 0; i < 5; i++) {
			pm = new PlayMaker(board, pieceToLogic);
			board = pm.makePlay(0, 0);
			pieceToLogic.put(
				0, 
				pieceToLogic.get(0).update(board, pm.pieceToPlays().get(0)[0])
			);
			System.out.println(board);
			
		}
	}
}
