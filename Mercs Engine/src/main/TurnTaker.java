package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pieces.Rook;


//TODO: Add documentation.
public class TurnTaker {
	public static Board takeTurn(Board board, Move[] turn) {
		List<Integer> newTileIndexesOfPieces = board.tileIndexesOfPieces();
		
		for(Move move : turn) {
			if(move.newPosition() == null) {
				newTileIndexesOfPieces.set(move.piece(), -1);
			}
			else {
				int newPieceIndex = board.tiles().indexOf(move.newPosition());
				if(newPieceIndex == -1) {
					throw new RuntimeException(
						"piece was moved to a non-existent Tile."
					);
				}
				newTileIndexesOfPieces.set(move.piece(), newPieceIndex);
				Set<Integer> occupiedTileIndexes = new HashSet<Integer>(
					newTileIndexesOfPieces
				);
				if(newTileIndexesOfPieces.size() > occupiedTileIndexes.size()){
					throw new RuntimeException("Two pieces on the same Tile.");
				}
			}
		}

		return new Board(board.tiles(), newTileIndexesOfPieces);


//		TODO: Remove once it is known this is no longer needed.
//		List<Integer> newPieceIndexes = bl.pieceIndexes();
//
//		for(Move move : turn) {
//			//"Picks up" the piece being moved.
//			int movingPiece = newPieceIndexes.indexOf(
//				bl.tiles().indexOf(move.startingPosition())
//			);
//			//Throws an error if there is no piece at move's startingPosition.
//			if(movingPiece == -1) {
//				throw new IndexOutOfBoundsException(
//					"pieceIndex out of bounds."
//				);
//			}
//			newPieceIndexes.set(movingPiece, -1);
//
//			//Captures piece if one exists on the move's capturedPosition.
//			if(move.capturedPosition() != null) {
//				/**
//				 * TODO: Consider if throwing an exception for a cap attempt
//				 * that's out of bounds to the board is worth it or not.
//				 */
//				int cappedPieceIndex = newPieceIndexes.indexOf(
//					bl.tiles().indexOf(move.capturedPosition())
//				);
//				if(cappedPieceIndex != -1) {
//					newPieceIndexes.set(cappedPieceIndex, -1);
//				}
//			}
//
//			/**
//			 * Makes sure there isn't a piece blocking the way of the moving
//			 * piece.
//			 */
//			int blockingPieceIndex = newPieceIndexes.indexOf(
//				bl.tiles().indexOf(move.endingPosition())
//			);
//			if(blockingPieceIndex != -1) {
//				throw new RuntimeException(
//					"At least two pieceIndexes to the same Tile."
//				);
//			}
//
//			/**
//			 * TODO: Add a check to see if the moving piece is being moved to a
//			 * Tile that actually exists on the board.
//			 */
//			newPieceIndexes.set(
//				movingPiece, bl.tiles().indexOf(move.endingPosition())
//			);
//		}
//
//		return new BoardLayout(bl.tiles(), newPieceIndexes);
	}


	public static void main(String[] args) {
		Board cs = buildChessSet();
		Piece[] pieces = new Piece[2];
		pieces[0] = new Rook(cs, 0);
		pieces[1] = new Rook(cs, 1);

		System.out.println("turns: " + Arrays.deepToString(pieces[0].turns()));
		System.out.print("0: " + cs.tileForPiece(0).toString());
		System.out.println(", 1:" + cs.tileForPiece(1).toString() + "\n");


		Move[] turn = pieces[0].turns()[1];
		for(int i = 0; i < pieces.length; i++) {
			pieces[i] = pieces[i].updateBoard(turn);
		}
		cs = TurnTaker.takeTurn(cs, turn);

		System.out.println("turns: " + Arrays.deepToString(pieces[0].turns()));
		System.out.print(cs.tileForPiece(0));
		System.out.println(" " + cs.tileForPiece(1).toString() + "\n");


		turn = pieces[0].turns()[2];
		cs = TurnTaker.takeTurn(cs, turn);

		System.out.println("turns: " + Arrays.deepToString(pieces[0].turns()));
		System.out.print(cs.tileForPiece(0));
		System.out.print(" ");
		System.out.println(cs.tileForPiece(1));
		System.out.println();
	}


	private static Board buildChessSet() {
		List<Tile> boardList = buildBoard();
		Integer[] pieceIndexes = new Integer[2];
		pieceIndexes[0] = boardList.indexOf(new Tile(2, 2));
		pieceIndexes[1] = boardList.indexOf(new Tile(4, 4));
		Tile[] board = boardList.toArray(new Tile[boardList.size()]);
		return new Board(Arrays.asList(board), Arrays.asList(pieceIndexes));
	}


	private static List<Tile> buildBoard() {
		List<Tile> board = new ArrayList<Tile>(25);
		for(int rank = 1; rank <= 5; rank++) {
			for(int file = 1; file <= 5; file++) {
				board.add(new Tile(rank, file));
			}
		}
		return board;
	}


//	private static Move[] buildTurn(Tile startingPosition, Tile offset) {
//		Move[] turn = new Move[1];
//		turn[0] = new Move(
//			startingPosition,
//			startingPosition.add(offset),
//			startingPosition.add(offset)
//		);
//		return turn;
//	}
}
