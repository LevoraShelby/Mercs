package mercs.pieceLogics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import main.Board;
import main.Move;
import main.RecordBasedMoveLogic;
import main.Tile;


/**
 * TODO: Document
 * @author trevor
 */
public class CommandoLogic implements RecordBasedMoveLogic {
	private final boolean isWhite;
	private final Integer piece;
	private final Board board;


	/**
	 * @param isWhite Is true if this piece is white (and so moves towards
	 * higher ranks), and is false if this piece is black (and so moves towards
	 * lower ranks).
	 * @param piece The piece that this CommandoLogic informs. (non-null)
	 * @param board The board that this CommandoLogic's piece plays on.
	 * (non-null)
	 */
	public CommandoLogic(boolean isWhite, Integer piece, Board board) {
		handleExceptions(piece, board);

		this.isWhite = isWhite;
		this.piece = piece;
		this.board = board;
	}


	public Move[][] plays() {
		Tile location = board.tileForPiece(piece);

		/**
		 * Immediately returns an empty array of plays if CommandoLogic's piece
		 * isn't even on the board to make a move.
		 */
		if(location == null) {
			return new Move[0][];
		}

		List<Move[]> plays = new ArrayList<>();
		/**
		 * Adds all the plays that CommandoLogic can take.
		 */
		for(Tile movementOffset : movementOffsets()) {
			Tile destination = location.add(movementOffset);
			/**
			 * Checks that the Tile this CommandoLogic is moving their piece to
			 * exists.
			 */
			if(board.tiles().contains(destination)) {
				plays.add(playThatTakesOverTile(destination));
			}
		}

		return plays.toArray(new Move[plays.size()][]);
	}


	/**
	 * @return The offsets from CommandoLogic's piece that this logic will
	 * inform the piece to move by if that piece can.
	 */
	private List<Tile> movementOffsets() {
		List<Tile> offsets = new ArrayList<>();
		/**
		 * Can't start by setting offsets to list below, because Array.asList()
		 * returns a fixed-size list.
		 */
		offsets.addAll(Arrays.asList(new Tile(0, -1), new Tile(0, 1)));
		if(isWhite) {
			offsets.addAll(Arrays.asList(
				new Tile(1, -1), new Tile(1, 0), new Tile(1, 1)
			));
		}
		else {
			offsets.addAll(Arrays.asList(
				new Tile(-1, -1), new Tile(-1, 0), new Tile(-1, 1)
			));
		}
		return offsets;
	}


	/**
	 * @param tile The tile that is being taken over by CommandoLogic.
	 * @return The play that when performed, would move CommandoLogic's piece
	 * to tile, removing any piece that is on tile beforehand.
	 */
	private Move[] playThatTakesOverTile(Tile tile) {
		List<Move> play = new ArrayList<>();
		Integer occupyingPiece = board.pieceOnTile(tile);
		if(occupyingPiece != null) {
			play.add(new Move(occupyingPiece, null));
		}
		play.add(new Move(piece, tile));
		return play.toArray(new Move[play.size()]);
	}


	public RecordBasedMoveLogic update(Board board, Move[] play) {
		return new CommandoLogic(isWhite, piece, board);
	}


	/**
	 * Throws exceptions if any of the arguments aren't allowed for the
	 * constructor.
	 */
	private static void handleExceptions(Integer piece, Board board) {
		if(piece == null) {
			throw new NullPointerException(
				"piece is null: There isn't a piece for this CommandoLogic to "
				+ "control."
			);
		}
		if(board == null) {
			throw new NullPointerException(
				"board is null: There isn't a board that this CommandoLogic "
				+ "can act on."
			);
		}
	}
}
