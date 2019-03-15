package display;

import java.util.Scanner;

import main.Move;
import main.Tile;
import mercs.MercsStartingGameInfo;
import mercs.MoveRound;
import mercs.info.GameInfo;
import mercs.info.PlayerInfo;



public class MoveRoundPage {
	public static void main(String[] args) {
		GameInfo info = MercsStartingGameInfo.startingInfo();
		Scanner in = new Scanner(System.in);
		info = load(info, in);

		String[] display = Display.defaultBoardDisplay(info);
		for(String line : display) {
			System.out.println(line);
		}
	}


	public static GameInfo load(GameInfo info, Scanner in) {
		Integer selectedPiece = null;
		MovementView movementView = new MovementView(info, null);
		MoveRound round = new MoveRound(info);
		while(true) {
			printDisplay(info, movementView);

			String input = in.nextLine();

			if(input.equals("q")) {
				System.exit(1);
			}

			else if(input.matches("[a-f][1-6]")) {
				Tile selectedTile = toTile(input);
				Integer tempSelectedPiece = info.board()
					.pieceOnTile(selectedTile);

				if(tempSelectedPiece != null) {
					Integer currentPlayer = info.order().turn().currentPlayer();

					if(
						info.playerToInfo().get(currentPlayer)
						.pieces().contains(tempSelectedPiece)
					) {
						selectedPiece = tempSelectedPiece;
						movementView = movementView.select(tempSelectedPiece);
					}
				}

				else if(selectedPiece != null) {
					Move[][] plays = info.pieceToInfo()
							.get(selectedPiece)
							.logic().plays();
					int playIndex = playIndexFromEndTile(plays, selectedTile);
					if(playIndex != -1) {
						return round.play(selectedPiece, playIndex);
					}
				}
			}

			else {
				selectedPiece = null;
				movementView = movementView.select(selectedPiece);
			}
		}
	}


	private static void printDisplay(
		GameInfo info, MovementView movementView
	) {
		clearScreen();
		for(String line : BoardDisplay.addGrid(movementView.display(),6)) {
			System.out.println(line);
		}
		System.out.println();

		for(Integer player : info.playerToInfo().keySet()) {
			PlayerInfo playerInfo = info.playerToInfo().get(player);
			System.out.println("Player " + player.toString() + ":");
			System.out.println("Cooldown - " + playerInfo.cooldown());
			System.out.println(
				"# of pieces captured - " + playerInfo.numPiecesCaptured()
			);
			System.out.println();
		}
	}



	private static Tile toTile(String str) {
		int rank = ((int) str.charAt(1)) - 48;
		int file = ((int) str.charAt(0)) - 96;
		return new Tile(rank, file);
	}


	private static int playIndexFromEndTile(Move[][] plays, Tile endTile) {
		for(int i = 0; i < plays.length; i++) {
			Move[] play = plays[i];
			if(play[play.length - 1].newPosition().equals(endTile)) {
				return i;
			}
		}
		return -1;
	}


	private static void clearScreen() {
		System.out.println("\u001b[H\u001b[2J");
	}
}
