package mercs;

/**
 * TODO: Document
 */
public final class PieceInfo {
	private final PieceType type;
	private final Integer owner; //Player


	public PieceInfo(PieceType type, Integer owner) {
		this.type = type;
		this.owner = owner;
	}


	public PieceType type() {
		return type;
	}


	public Integer owner() {
		return owner;
	}
}
