package mercs;

import java.util.Objects;



/**
 * This is used to describe pieces by their type and by who owns them.
 * @author trevor
 */
public final class PieceGroup {
	private final PieceType type;
	private final Integer player;


	/**
	 * Constructs a piece.
	 * @param type the type for the piece.
	 * @param player the owner of the piece.
	 */
	public PieceGroup(PieceType type, Integer player) {
		this.type = type;
		this.player = player;
	}


	public PieceType type() {
		return type;
	}


	public Integer player() {
		return player;
	}


	public boolean equals(Object o) {
		if(o == null) {
			return false;
		}
		PieceGroup group = (PieceGroup) o;
		return type == group.type() && player.equals(group.player());
	}


	public int hashCode() {
		return Objects.hash(type, player);
	}


	public String toString() {
		return "{type: " + type + ", player: " + player + " }";
	}
}
