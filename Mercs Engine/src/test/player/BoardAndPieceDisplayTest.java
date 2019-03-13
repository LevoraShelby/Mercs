package test.player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import console.BoardDisplay;
import console.MovementView;
import console.SelectionView;
import mercs.info.GameInfo;
import test.MercsStartingGameInfo;



public class BoardAndPieceDisplayTest {
	public static void main(String[] args) {
		GameInfo info = MercsStartingGameInfo.startingInfo();
		Integer[] pieces = pieces(info);
		SelectionView sv = selectionView(pieces);
		Map<String, Integer> selectionToIndex = new HashMap<>();
		for(int i = 0; i < pieces.length; i++) {
			selectionToIndex.put(String.valueOf(pieces[i]), i);
		}
		MovementView mv = new MovementView(info, null);

		Scanner in = new Scanner(System.in);

		Integer selectedPiece = null;
		while(true) {
			String[] selectionDisplay = sv.display();
			String[] movementDisplay = BoardDisplay.addGrid(mv.display(), 6);
			clearScreen();
			printDisplays(selectionDisplay, movementDisplay);

			String input = in.nextLine();
			if(input.equals("q")) {
				break;
			}

			if(selectionToIndex.containsKey(input)) {
				selectedPiece = Integer.parseInt(input);
				sv = sv.select(selectionToIndex.get(input));
				mv = mv.select(selectedPiece);
			}
			else {
				continue;
			}
		}
		in.close();
	}


	private static Integer[] pieces(GameInfo info) {
		Integer currentPlayer = info.order().turn().currentPlayer();
		Set<Integer> pieceSet = info.playerToInfo().get(currentPlayer)
			.pieces();
		Iterator<Integer> iter = pieceSet.iterator();

		while(iter.hasNext()) {
			if(info.board().tileForPiece(iter.next()) == null) {
				iter.remove();
			}
		}

		Integer[] pieces = pieceSet.toArray(new Integer[pieceSet.size()]);
		Arrays.sort(pieces);
		return pieces;
	}


	private static SelectionView selectionView(Integer[] pieces) {
		String[] selections = Arrays.stream(pieces)
				.map(piece -> String.valueOf(piece) + ".")
				.toArray(size->new String[size]);
		return new SelectionView(selections, -1, 5);
	}


	private static void printDisplays(String[] display1, String[] display2) {
		int length = display1.length > display2.length ? 
			display1.length : display2.length;

		for(int i = 0; i < length; i++) {
			if(i < display1.length) {
				System.out.print(display1[i]);
			}
			else if(i >= display1.length) {
				System.out.print("     ");
			}
			System.out.print("   ");

			if(i < display2.length) {
				System.out.print(display2[i]);
			}

			System.out.println();
		}
	}


//	private static Tile[] endTiles(GameInfo info, Integer selectedPiece) {
//		PieceInfo pieceInfo = info.pieceToInfo().get(selectedPiece);
//		Move[][] plays = pieceInfo.logic().plays();
//
//		Tile[] endTiles = new Tile[plays.length];
//		for(int i = 0; i < endTiles.length; i++) {
//			Move lastMove = plays[i][plays[i].length - 1];
//			endTiles[i] = lastMove.newPosition();
//		}
//		Arrays.sort(endTiles);
//
//		return endTiles;
//	}


	private static void clearScreen() {
		System.out.println("\u001b[H\u001b[2J");
	}
}
