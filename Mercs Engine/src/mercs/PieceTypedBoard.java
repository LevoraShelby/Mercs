package mercs;

import java.util.HashMap;
import java.util.Map;

import main.Board;
import main.Tile;


/**
 * TODO: Document
 */
public final class PieceTypedBoard {
	private final Board board;
	private final Map<Integer, PieceType> pieceToType;

	public PieceTypedBoard(
		Board board,
		Map<Integer, PieceType> pieceToType
	) {
		this.board = board;
		this.pieceToType = new HashMap<Integer, PieceType>(pieceToType);
	}

	public Board board() {
		return board;
	}

	public PieceType pieceTypeOnTile(Tile tile) {
		Integer piece = board.pieceOnTile(tile);
		if(piece == null) {
			return null;
		}
		return pieceToType.get(piece);
	}

	public PieceType pieceType(Integer piece) {
		return pieceToType.get(piece);
	}

	//TODO: Consider removing if it can be replaced with other methods.
	public Map<Integer, PieceType> pieceToType() {
		return new HashMap<Integer, PieceType>(pieceToType);
	}
}
