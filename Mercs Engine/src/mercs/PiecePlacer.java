package mercs;

import java.util.Map;
import java.util.HashMap;

import main.Board;
import main.Move;
import main.Tile;
import main.TurnTaker;



/**
 * Chooses a piece to place on a board based on its assigned PieceGroup.
 * @author trevor
 */
public class PiecePlacer {
	private final Board board;
	private final Map<Integer, PieceGroup> pieceToGroup;


	/**
	 * Constructs a PiecePlacer
	 * @param board the Board to place pieces on.
	 * @param pieceToGroup the assignment of pieces to PieceGroups.
	 */
	public PiecePlacer(
		Board board, Map<Integer, PieceGroup> pieceToGroup
	) {
		this.board = board;
		this.pieceToGroup = new HashMap<Integer, PieceGroup>(pieceToGroup);
	}


	/**
	 * @param group the PieceGroup needed for the piece being placed.
	 * @param tile the tile to place the piece on.
	 * @returns a Board with a piece matching the PieceGroup placed on tile.
	 */
	public Board placePiece(PieceGroup group, Tile tile) {
		//Finds a piece that is described by group that is off the board.
		Integer pieceToPlace = null;
		for(Integer piece : pieceToGroup.keySet()) {
			boolean isPieceOffBoard = board.tileForPiece(piece) == null;
			boolean isCorrectGrouping = group.equals(pieceToGroup.get(piece));
			if(isPieceOffBoard && isCorrectGrouping) {
				pieceToPlace = piece;
				break;
			}
		}

		//Instantiates the move to place the piece on tile.
		Move[] placingPiece = {
			new Move(pieceToPlace, tile)
		};

		//Places the piece on tile, constructing a new Board to be returned.
		return TurnTaker.takeTurn(board, placingPiece);
	}
}
