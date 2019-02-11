package mercs;

import java.util.HashMap;
import java.util.Map;

import main.Board;


public class PieceTypePlacer {
	private final Map<PieceType, Integer[]> piecesTypes;
	private final Board bl;

	public PieceTypePlacer(
		Map<PieceType, Integer[]> piecesTypes,
		Board bl
	) {
		this.piecesTypes = new HashMap<PieceType, Integer[]>(piecesTypes);
		this.bl = bl;
	}

	public Map<PieceType, Integer[]> piecesTypes() {
		return new HashMap<PieceType, Integer[]>(piecesTypes);
	}

	public Board boardLayout() {
		return bl;
	}
}
