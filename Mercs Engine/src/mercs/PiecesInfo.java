package mercs;

import java.util.HashMap;
import java.util.Map;

import main.Board;


public class PiecesInfo {
	private Board bl;
	private Map<PieceType, Integer[]> piecesTypes;
	private Map<Integer, Integer[]> playerPieces;

	public PiecesInfo(
		Board bl,
		Map<PieceType, Integer[]> piecesTypes,
		Map<Integer, Integer[]> plaerPieces
	) {
		this.bl = bl;
		this.piecesTypes = new HashMap<PieceType, Integer[]>(piecesTypes);
		this.playerPieces = new HashMap<Integer, Integer[]>(playerPieces);
	}

	public Board boardLayout() {
		return bl;
	}

	public Map<PieceType, Integer[]> piecesTypes() {
		return new HashMap<PieceType, Integer[]>(piecesTypes);
	}

	public Map<Integer, Integer[]> playerPieces() {
		return new HashMap<Integer, Integer[]>(playerPieces);
	}
}
