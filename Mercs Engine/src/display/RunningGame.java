package display;

import java.util.Scanner;

import main.Tile;
import mercs.BuyRound;
import mercs.MercsStartingGameInfo;
import mercs.PieceType;
import mercs.PlayState;
import mercs.info.GameInfo;



public class RunningGame {
	public static void main(String[] args) {
		GameInfo info = MercsStartingGameInfo.startingInfo();
		Scanner in = new Scanner(System.in);
		while(
			info.order().turn().playState() != PlayState.WON
		) {
			if(info.order().turn().playState() == PlayState.MOVE) {
				info = MoveRoundPage.load(info, in);
			}
			else {
				System.out.println("\u001b[H\u001b[2J");
				for(String line : Display.defaultBoardDisplay(info)) {
					System.out.println(line);
				}
				info = load(info, in);	
			}
		}
	}


	public static GameInfo load(GameInfo info, Scanner in) {
		while(true) {
			String input = in.nextLine();
			if(input.equals("pass")) {
				return new BuyRound(info).pass();
			}
			else if(input.equals("q")) {
				return info;
			}
			else {
				for(PieceType type : PieceType.values()) {
					if(type.toString().toUpperCase()
						.equals(input.toUpperCase())
					) {
						Tile tileForPiece = inputForTile(in);
						if(tileForPiece != null) {
							return new BuyRound(info).buy(type, tileForPiece);
						}
					}
				}
			}
		}
	}


	public static Tile inputForTile(Scanner in) {
		while(true) {
			String input = in.nextLine();
			if(input.matches("[a-f][1-6]")) {
				return toTile(input);
			}
			else if(input.equals("q")) {
				return null;
			}
		}
	}


	private static Tile toTile(String str) {
		int rank = ((int) str.charAt(1)) - 48;
		int file = ((int) str.charAt(0)) - 96;
		return new Tile(rank, file);
	}
}
