package display;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import main.Tile;
import mercs.BuyRound;
import mercs.MoveRound;
import mercs.PieceType;
import mercs.PlayState;
import mercs.info.GameInfo;
import test.MercsStartingGameInfo;
import test.player.SelectPiece;



public class GamePlayer {
	public static void main(String[] args) {
		GameInfo info = MercsStartingGameInfo.startingInfo();

		Scanner in = new Scanner(System.in);
		for(int turnNum = 0; turnNum < 20; turnNum++) {
			Map<Tile, String> tileToDisplay = tileToDisplay(info);
			GamePrinter.printGame(info.board().tiles(),tileToDisplay,"X");
			System.out.println();
			info.playerToInfo().forEach((player, playerInfo) -> {
				System.out.println(
					"Player " + player + "'s cooldown: " + playerInfo.cooldown()
				);
			});

			String input;
			if(info.order().turn().playState() == PlayState.MOVE) {
				System.out.println(
					"Move player " + info.order().turn().currentPlayer() + "!"
				);
				info = playMoveRound(info, tileToDisplay, in);
			}
			else if(info.order().turn().playState() == PlayState.BUY) {
				BuyRound round = new BuyRound(info);
				System.out.println(round.pieceTypeToAvailableTiles());
				System.out.println(
					"Buy player " + info.order().turn().currentPlayer() + "!"
				);

				input = in.nextLine();
				String[] command = input.split(" ");
				if(command[0].equals("buy")) {
					PieceType type = PieceType.valueOf(
						command[1].toUpperCase()
					);
					int rank = Integer.parseInt(command[2]);
					int file = Integer.parseInt(command[3]);
					info = round.buy(type, new Tile(rank, file));
				}
				else if(command[0].equals("pass")) {
					info = round.pass();
				}
				else {
					throw new RuntimeException();
				}
			}
			System.out.println();
		}
		in.close();
	}


	private static GameInfo playMoveRound(
		GameInfo info, Map<Tile, String> tileToDisplay, Scanner in
	) {
		MoveRound round = new MoveRound(info);

		
		while(true) {
			String[] command = in.nextLine().split(" ");
			if(command[0].equals("move")) {
				Integer piece = Integer.parseInt(command[1]);
				int playIndex = Integer.parseInt(command[2]);
				return round.play(piece, playIndex);
			}
			else if(command[0].equals("pieces")) {
				SelectPiece.selectPiece(info, tileToDisplay, in);
			}
			else {
				throw new RuntimeException();
			}	
		}
	}


	private static Map<Tile, String> tileToDisplay(GameInfo info) {
		Map<Tile, String> tileToDisplay = new HashMap<>();
		for(Tile tile : info.board().tiles()) {
			Integer piece = info.board().pieceOnTile(tile);
			if(piece == null) continue;
			String display = " ";
			PieceType type = info.pieceToInfo().get(piece).type();
			if(type == PieceType.PAWN) display = "p";
			else if(type == PieceType.FERZ) display = "f";
			else if(type == PieceType.WAZIR) display = "w";
			else if(type == PieceType.KNIGHT) display = "n";
			else if(type == PieceType.COMMANDO) display = "c";
			Integer blackPlayer = info.order().secondPlayer();
			if(info.playerToInfo().get(blackPlayer).pieces().contains(piece)) {
				display = display.toUpperCase();
			}
			tileToDisplay.put(tile, display);
		}
		return tileToDisplay;
	}
}
