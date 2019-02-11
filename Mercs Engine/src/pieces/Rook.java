package pieces;

import java.util.ArrayList;
import java.util.List;

import main.Board;
import main.Move;
import main.Piece;
import main.Tile;
import main.TurnTaker;



//TODO: Actually document this.
public final class Rook implements Piece {
	private final Board bl;
	private final Integer piece;

	private static final Tile[] offsets = {
		new Tile(0, 1),  //right
		new Tile(1, 0),  //up
		new Tile(0, -1), //left
		new Tile(-1, 0)  //down
	};


	public Rook(Board bl, int piece) {
		this.bl = bl;
		this.piece = piece;
	}


	/**
	 * See rook.png in /diagrams/
	 */
	public Move[][] turns() {
		List<Move[]> turnsList = new ArrayList<Move[]>();

		for(Tile offset : offsets) {
			for(Tile newPosition = position().add(offset);
				bl.tiles().contains(newPosition);
				newPosition = newPosition.add(offset)
			) {
				Move[] turn;
				if(bl.pieceOnTile(newPosition) == null) {
					turn = new Move[1];
					turn[0] = new Move(piece, newPosition);
					turnsList.add(turn);
				}
				else {
					turn = new Move[2];
					turn[0] = new Move(bl.pieceOnTile(newPosition), null);
					turn[1] = new Move(piece, newPosition);
					turnsList.add(turn);
					break;
				}
			}
		}

		return turnsList.toArray(new Move[turnsList.size()][]);
	}


	public Piece updateBoard(Move[] turn) {
		Board newBL = TurnTaker.takeTurn(bl, turn);
		return new Rook(newBL, piece);
	}


	/**
	 * @return the Tile that the Rook occupies.
	 */
	private Tile position() {
		return bl.tileForPiece(piece);
	}


	public static void main(String[] args) {
	}
}
