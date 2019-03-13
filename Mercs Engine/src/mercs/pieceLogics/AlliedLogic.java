package mercs.pieceLogics;

import java.util.ArrayList;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import main.Board;
import main.Move;
import main.RecordBasedMoveLogic;



/**
 * Represents the filtered logic for a piece. It only exposes the plays from
 * the given logic that do not end capturing "friendly" pieces.
 */
public class AlliedLogic implements RecordBasedMoveLogic {
	private final RecordBasedMoveLogic innerLogic;
	private final Set<Integer> friendlyPieces;


	/**
	 * @param innerLogic The logic that this AlliedLogic delegates to.
	 * (non-null)
	 * @param friendlyPieces The pieces that this AlliedLogic will avoid
	 * capturing. (non-null).
	 */
	public AlliedLogic(
		RecordBasedMoveLogic innerLogic,
		Set<Integer> friendlyPieces
	) {
		handleExceptions(innerLogic, friendlyPieces);
		
		this.innerLogic = innerLogic;
		this.friendlyPieces = new HashSet<>(friendlyPieces);
	}


	/**
	 * @return The plays from the inner logic of AlliedLogic that don't capture
	 * friendly pieces.
	 */
	public Move[][] plays() {
		Move[][] unfilteredPlays = innerLogic.plays();
		List<Move[]> filteredPlays = new ArrayList<>();

		for(Move[] play : unfilteredPlays) {
			if(!capturesFriendlyPiece(play)) {
				filteredPlays.add(play);
			}
		}

		return filteredPlays.toArray(new Move[filteredPlays.size()][]);
	}


	/**
	 * @param play The play being examined.
	 * @return Whether or not play ends up capturing a friendly piece when it
	 * is being performed.
	 */
	private boolean capturesFriendlyPiece(Move[] play) {
		for(Move move : play) {
			/**
			 * Checks to see if a friendly piece is being moved off the board
			 * by play. If it is, this method returns true.
			 */
			if(
				move.newPosition() == null 
				&& friendlyPieces.contains(move.piece())
			) {
				return true;
			}
		}
		/**
		 * Returns false if no moves in play move a friendly piece off the
		 * board.
		 */
		return false;
	}


	/**
	 * @return A version of this AlliedLogic with an updated inner logic. 
	 */
	public RecordBasedMoveLogic update(Board board, Move[] play) {
		return new AlliedLogic(
			innerLogic.update(board, play),
			friendlyPieces
		);
	}


	/**
	 * Throws exceptions if any of the arguments aren't allowed for the
	 * constructor.
	 */
	private static void handleExceptions(
		RecordBasedMoveLogic innerLogic,
		Set<Integer> friendlyPieces
	) {
		if(innerLogic == null) {
			throw new NullPointerException(
				"innerLogic is null: There isn't a core logic for this "
				+ "AlliedLogic to call to."
			);
		}
		if(friendlyPieces == null) {
			throw new NullPointerException(
				"friendlyPieces is null: This AlliedLogic doesn't know which "
				+ "it is \"friends\" with."
			);
		}
	}
}
