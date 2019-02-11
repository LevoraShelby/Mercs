package main;

import java.util.HashSet;
import java.util.List;
import java.util.Set;



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
	}


//	private static Board buildChessSet() {
//		List<Tile> boardList = buildBoard();
//		Integer[] pieceIndexes = new Integer[2];
//		pieceIndexes[0] = boardList.indexOf(new Tile(2, 2));
//		pieceIndexes[1] = boardList.indexOf(new Tile(4, 4));
//		Tile[] board = boardList.toArray(new Tile[boardList.size()]);
//		return new Board(Arrays.asList(board), Arrays.asList(pieceIndexes));
//	}
//
//
//	private static List<Tile> buildBoard() {
//		List<Tile> board = new ArrayList<Tile>(25);
//		for(int rank = 1; rank <= 5; rank++) {
//			for(int file = 1; file <= 5; file++) {
//				board.add(new Tile(rank, file));
//			}
//		}
//		return board;
//	}
}
