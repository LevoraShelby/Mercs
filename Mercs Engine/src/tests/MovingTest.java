package tests;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import pieces.ForTestPiece;

import main.Move;
import main.Piece;
import main.Tile;


@Deprecated
public class MovingTest {
	public static void main(String[] args) {
		Map<Tile, Boolean> board = new HashMap<Tile, Boolean>(49);
		Tile position1 = null;
		Tile position2 = null;
		for(int rank = 1; rank <= 7; rank++) {
			for(int file = 1; file <= 7; file++) {
				if(rank == 3 && file == 3) {
					position1 = new Tile(rank, file);
					board.put(position1, true);
				}
				else if(rank == 5 && file == 5) {
					position2 = new Tile(rank, file);
					board.put(position2, true);
				}
				else {
					board.put(new Tile(rank, file), false);
				}
			}
		}

		Piece[] pieces = {
			new ForTestPiece(board, position1),
			new ForTestPiece(board, position2)
		};
		Scanner s = new Scanner(System.in);
		String in = "";
		while(in != "quit") {
			printTurns(pieces[0]);

			in = s.nextLine();

			boolean isNum;
			try {
				Integer.parseInt(in);
				isNum = true;
			} catch(NumberFormatException nfe) {
				isNum = false;
			}
			
			if(isNum) {
				int turnIndex = Integer.parseInt(in);
				pieces = updateBoard(pieces, pieces[0].turns()[turnIndex]);
			}
		}
		s.close();
	}


	private static void printTurns(Piece piece) {
		for(Move[] turn : piece.turns()) {
			System.out.println(turn[0]);
		}
	}


	private static Piece[] updateBoard(Piece[] pieces, Move[] turn) {
		Piece[] newPieces = new Piece[pieces.length];
		for(int i = 0; i < pieces.length; i++) {
			newPieces[i] = pieces[i].updateBoard(turn);
		}
		return newPieces;
	}
}
