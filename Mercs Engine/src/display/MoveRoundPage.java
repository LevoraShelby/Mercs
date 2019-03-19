package display;

import java.util.Scanner;

import main.Move;
import main.Tile;
import mercs.MoveRound;
import mercs.info.GameInfo;
import mercs.info.PlayerInfo;



public class MoveRoundPage {
	public static GameInfo load(GameInfo info, Scanner in) {
		Integer selectedPiece = null;
		MovementView movementView = new MovementView(info, null);
		MoveRound round = new MoveRound(info);

		/**
		 * Each iteration of this loop displays the board with the current
		 * selected piece to the user and asks for an input. The only input
		 * that will break out of this loop is an input that gets read as
		 * the user wanting to move their selected piece.
		 */
		while(true) {
			//Prints out the board and asks user for input.
			printDisplay(info, movementView);
			String input = in.nextLine();

			//This is a check to see if the user wants to quit out of the game.
			if(input.equals("q")) {
				in.close();
				System.exit(1);
			}

			/**
			 * This is a check to see if the user is trying to select a tile.
			 * For example, the input "b4" would mean the user is trying to
			 * select the tile on rank 4 and file 2.
			 */
			else if(input.matches("[a-f][1-6]")) {
				Tile selectedTile = toTile(input);

				/**
				 * If the selected tile is a position that the selected piece
				 * can move to, an updated version of the game's state where
				 * that play is taken by the selected piece is returned.
				 */
				if(selectedPiece != null) {
					Move[][] plays = info.pieceToInfo()
							.get(selectedPiece)
							.logic().plays();
					int playIndex = playIndexFromEndTile(plays, selectedTile);
					if(playIndex != -1) {
						return round.play(selectedPiece, playIndex);
					}
				}


				Integer tempSelectedPiece = info.board()
					.pieceOnTile(selectedTile);
				/**
				 * If the selected tile has a piece that belongs to the current
				 * player on it, that piece gets selected.
				 */
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
			}

			/**
			 * If the input doesn't make sense, we interpret it as a command
			 * to unselected the currently selected piece.
			 */
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
