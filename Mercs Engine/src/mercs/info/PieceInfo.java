package mercs.info;

import java.util.Arrays;

import main.Move;
import main.RecordBasedMoveLogic;

import mercs.PieceType;



/**
 * Describes a piece used in a game of Mercs.
 * @author trevor
 */
public final class PieceInfo {
	private final PieceType type;
	private final RecordBasedMoveLogic logic;


	/**
	 * @param type the type that this piece can be described as.
	 * @param logic the logic that this piece will follow in play.
	 */
	public PieceInfo(PieceType type, RecordBasedMoveLogic logic) {
		handleExceptions(type, logic);

		this.type = type;
		this.logic = logic;
	}


	/**
	 * @return the type that this piece can be described as.
	 */
	public PieceType type() {
		return type;
	}


	/**
	 * @return the logic that this piece will follow.
	 */
	public RecordBasedMoveLogic logic() {
		return logic;
	}


	/**
	 * Throws exceptions if any of the arguments aren't allowed for the
	 * constructor.
	 */
	private static void handleExceptions(
		PieceType type, RecordBasedMoveLogic logic
	) {
		if(type == null) {
			throw new NullPointerException(
				"type is null: This piece has no type."
			);
		}
		if(logic == null) {
			throw new NullPointerException(
				"logic is null: This piece can't tell what it can do."
			);
		}
	}


	public String toString() {
		//Joins together the string for each play with ", "
		String playsString = "";
		Move[][] plays = logic.plays();
		for(int i = 0; i < plays.length; i++) {
			playsString += Arrays.deepToString(plays[i]);
			if(i < plays.length - 1) {
				playsString += ", ";
			}
		}

		String toString = "{type: " + type + ", plays: " + playsString + "}";
		return toString;
	}
}
