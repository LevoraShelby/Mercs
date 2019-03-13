package test.player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

import display.GamePrinter;
import main.Tile;
import main.Move;
import mercs.info.GameInfo;



public class SelectPiece implements Function<
	GameInfo, Function<
		Map<Tile, String>, Consumer<Scanner>
	>
> {
	private static final String SGR_RESET = "\u001b[0m";
	private static final String SGR_HIGHLIGHT = "\u001b[7m";


	public Function<
		Map<Tile, String>, Consumer<Scanner>
	> apply(GameInfo info) {
		return tileToDisplay -> in -> {
			selectPiece(info, tileToDisplay, in);
		};
	}


	public static void selectPiece(
		GameInfo info, Map<Tile, String> tileToDisplay, Scanner in
	) {
		Integer[] sortedPieces = sortedPieces(info);
		Integer selectedPiece = sortedPieces[0];

		String input = "";
		cls();
		while(true) {
			//Prints out all of the current player's pieces.
			String[] pieceListDisplay = pieceListDisplay(
				selectedPiece, sortedPieces
			);
			String[] boardDisplay = boardDisplay(
				info, selectedPiece, tileToDisplay
			);
			System.out.println(combinedDisplay(pieceListDisplay,boardDisplay));

			input = in.nextLine();
			//Checks to see if input is giving the signal to quit.
			if(input.equals("q")) {
				break;
			}

			//If input is a number, selectedIndex gets updated to input's value
			try {
				cls();
				Integer newSelection = Integer.parseInt(input);
				if(Arrays.binarySearch(sortedPieces, newSelection) != -1) {
					selectedPiece = newSelection;
				}
			} catch(NumberFormatException e) {}
		}
	}


	private static Integer[] sortedPieces(GameInfo info) {
		Integer currentPlayer = info.order().turn().currentPlayer();
		Set<Integer> piecesSet = 
			info.playerToInfo().get(currentPlayer).pieces();
		Iterator<Integer> iter = piecesSet.iterator();
		while(iter.hasNext()) {
			Integer piece = iter.next();
			if(info.board().tileForPiece(piece) == null) {
				iter.remove();
			}
		}
		Integer[] sortedPieces = 
			piecesSet.toArray(new Integer[piecesSet.size()]);
		Arrays.sort(sortedPieces);

		return sortedPieces;
	}


	//Clears the console.
	private static void cls() {
		System.out.print("\u001b[2J");  
	    System.out.flush(); 
	}


	private static String combinedDisplay(
		String[] display1, String[] display2
	) {
		String combinedDisplay = "";

		int length = display1.length > display2.length ?
			display1.length : display2.length;
		for(int i = 0; i < length; i++) {
			if(i < display1.length) {
				combinedDisplay += display1[i];
			} else {
				combinedDisplay += "     ";
			}

			if(i < display2.length) {
				combinedDisplay += display2[i];
			}
			combinedDisplay += "\n";
		}

		return combinedDisplay;
	}


	private static String[] pieceListDisplay(
		int selectedPiece, Integer[] sortedPieces
	) {
		List<String> pieceListDisplay = new ArrayList<>();
		for(Integer piece : sortedPieces) {
			String displayForPiece = piece + ".";
			if(piece == selectedPiece) {
				displayForPiece = SGR_HIGHLIGHT + displayForPiece 
					+ SGR_RESET;
			}
			//pads display to be 5 letters long
			for(
				int numSpaces = String.valueOf(piece).length(); 
				numSpaces < 5;
				numSpaces++
			) {
				displayForPiece += " ";
			}
			pieceListDisplay.add(displayForPiece);
		}
		return pieceListDisplay.toArray(new String[pieceListDisplay.size()]);
	}


	private static String[] boardDisplay(
		GameInfo info, Integer selectedPiece, Map<Tile, String> tileToDisplay
	) {
		tileToDisplay = new HashMap<>(tileToDisplay);

		String CSI = "\u001b[";
		Move[][] plays = info.pieceToInfo().get(selectedPiece).logic().plays();
		for(Move[] play : plays) {
			Tile tile = play[play.length - 1].newPosition();

			String display = tileToDisplay.get(tile);
			if(display == null) {
				display = "X";
			}
			display = CSI + "7m" + display + CSI + "m";

			tileToDisplay.put(tile, display);
		}

		Tile tileOfSelectedPiece = info.board().tileForPiece(selectedPiece);
		String display = tileToDisplay.get(tileOfSelectedPiece);
		display = CSI + "5m" + display + CSI + "m";
		tileToDisplay.put(tileOfSelectedPiece, display);

		return GamePrinter.gameToString(
			info.board().tiles(), tileToDisplay, "X"
		).split("\n");
	}
}
