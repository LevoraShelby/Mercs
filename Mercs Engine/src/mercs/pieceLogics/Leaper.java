package mercs.pieceLogics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import main.Board;
import main.Move;
import main.RecordBasedMoveLogic;
import main.Tile;


/**
 * https://en.wikipedia.org/wiki/Fairy_chess_piece#Leapers
 * @author trevor
 */
public class Leaper implements RecordBasedMoveLogic {
	private final Integer piece;
	private final Board board;
	private final Tile mn;


	public Leaper(Integer piece, Board board, Tile mn) {
		handleExceptions(piece, board, mn);
		
		this.piece = piece;
		this.board = board;
		this.mn = mn;
	}


	public Move[][] plays() {
		Tile location = board.tileForPiece(piece);

		/**
		 * Immediately returns an empty array of plays if Leaper's piece isn't
		 * even on the board to make a move.
		 */
		if(location == null) {
			return new Move[0][];
		}

		List<Move[]> plays = new ArrayList<>();
		/**
		 * Adds all the plays that Leaper can take.
		 */
		for(Tile movementOffset : movementOffsets()) {
			Tile destination = location.add(movementOffset);
			 /**
			  * Checks that the Tile this Leaper is moving their piece to
			  * exists.
			  */
			if(board.tiles().contains(destination)) {
				plays.add(playThatTakesOverTile(destination));
			}
		}

		return plays.toArray(new Move[plays.size()][]);
	}


	private Set<Tile> movementOffsets() {
		Set<Tile> movementOffsets = new HashSet<>();
		int m = mn.rank(); int n = mn.file();
		movementOffsets.addAll(Arrays.asList(
			mn,
			new Tile(-m, n),
			new Tile(m, -n),
			new Tile(-m, -n),
			new Tile(n, m),
			new Tile(-n, m),
			new Tile(n, -m),
			new Tile(-n, -m)
		));
		return movementOffsets;
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
		return new Leaper(piece, board, mn);
	}


	public static RecordBasedMoveLogic knight(Integer piece, Board board) {
		return new Leaper(piece, board, new Tile(2,1));
	}


	public static RecordBasedMoveLogic wazir(Integer piece, Board board) {
		return new Leaper(piece, board, new Tile(1,0));
	}


	public static RecordBasedMoveLogic ferz(Integer piece, Board board) {
		return new Leaper(piece, board, new Tile(1,1));
	}


	/**
	 * Throws exceptions if any of the arguments aren't allowed for the
	 * constructor.
	 */
	private static void handleExceptions(Integer piece, Board board, Tile mn) {
		if(piece == null) {
			throw new NullPointerException(
				"piece is null: There isn't a piece for this Leaper to " +
				"control."
			);
		}
		if(board == null) {
			throw new NullPointerException(
				"board is null: There isn't a board that this Leaper can " +
				"act on."
			);
		}
		if(mn == null) {
			throw new NullPointerException(
				"mn is null: This Leaper has no way to leap."
			);
		}
	}
}
