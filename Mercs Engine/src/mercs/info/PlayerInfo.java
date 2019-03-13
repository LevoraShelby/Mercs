package mercs.info;

import java.util.HashSet;
import java.util.Set;



/**
 * Describes a player acting in a game of Mercs.
 * @author trevor
 */
public final class PlayerInfo {
	private final Set<Integer> pieces;
	private final int numPiecesCaptured;
	private final int cooldown;


	/**
	 * @param pieces The pieces that this player controls. (non-null)
	 * @param numPiecesCaptured The number of pieces that this player has
	 * captured.
	 * @param cooldown The number of buy rounds that this player will sit out.
	 */
	public PlayerInfo(
		Set<Integer> pieces,
		int numPiecesCaptured,
		int cooldown
	) {
		handleExceptions(pieces, numPiecesCaptured, cooldown);

		this.pieces = new HashSet<Integer>(pieces);
		this.numPiecesCaptured = numPiecesCaptured;
		this.cooldown = cooldown;
	}


	/**
	 * @return the pieces that this player controls or "owns".
	 */
	public Set<Integer> pieces() {
		return new HashSet<Integer>(pieces);
	}


	/**
	 * @return the number of pieces that this player has captured.
	 */
	public int numPiecesCaptured() {
		return numPiecesCaptured;
	}


	/**
	 * @return the number of buy rounds that this player will sit out.
	 */
	public int cooldown() {
		return cooldown;
	}


	/**
	 * Throws exceptions if any of the arguments aren't allowed for the
	 * constructor.
	 */
	private static void handleExceptions(
		Set<Integer> pieces,
		int numCaptured,
		int cooldown
	) {
		if(pieces == null) {
			throw new NullPointerException(
				"PlayerInfo wasn't given a set of pieces."
			);
		}
		if(numCaptured < 0) {
			throw new IllegalArgumentException(
				"numCaptured is less than zero: it's not possible to capture "
				+ "a negative number of pieces."
			);
		}
		if(cooldown < 0) {
			throw new IllegalArgumentException(
				"cooldown is less than zero: It's not possible to have to sit "
				+ "out a negative number of buy rounds."
			);
		}
	}


	public String toString() {
		String toString = "{pieces: " + pieces + ", numPiecesCaptured: ";
		toString += numPiecesCaptured + ", cooldown: " + cooldown + "}";
		return toString;
	}
}
