package test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;

import main.Board;
import main.Move;
import main.RecordBasedMoveLogic;
import main.Tile;
import mercs.UpdatingPlayMaker;
import mercs.pieceLogics.AlliedLogic;
import mercs.pieceLogics.Leaper;

public class PieceTesting {
	public static void main(String[] args) {
		Board board = Board.squareBoard(
			5,
			Arrays.asList(new Tile(1,3), new Tile(3,4))
		);
		Map<Integer, RecordBasedMoveLogic> pieceToLogic = new HashMap<>();
		Integer movingLeaper = 0;
		for(Integer piece : board.pieces()) {
			if(piece != movingLeaper) {
				pieceToLogic.put(piece, new BlankLogic());
			}
		}
		pieceToLogic.put(movingLeaper, new AlliedLogic(
			Leaper.wazir(movingLeaper, board),
			new HashSet<>() //board.pieces()
		));

		UpdatingPlayMaker playMaker = new UpdatingPlayMaker(
			board, pieceToLogic
		);

		BoardPrinter.printBoard(board);
		Scanner in = new Scanner(System.in);
		for(int i = 0; i < 10; i++) {
			Move[][] plays = pieceToLogic.get(movingLeaper).plays();

			System.out.println(Arrays.deepToString(plays));
			int playIndex = in.nextInt();
			Move[] play = plays[playIndex];

			board = playMaker.makePlay(movingLeaper, playIndex);
			pieceToLogic = playMaker.update(board, play);
			playMaker = new UpdatingPlayMaker(board, pieceToLogic);

			System.out.println();
			BoardPrinter.printBoard(board);
		}
		in.close();
	}
}
