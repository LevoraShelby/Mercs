package mercs;


import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import main.Board;
import main.Move;
import main.Tile;
import main.TurnTaker;



/**
 * TODO: Document
 */
public class GameState {
	private final int turnState;
	private final PlayerState[] playerStates;
	private final PieceTypedBoard typedBoard;


	/**
	 * TODO: Implement correctly
	 */
	public GameState() {
		turnState = 0;
		playerStates = initialPlayerStates();
		typedBoard = initialTypedBoard();
	}


	/**
	 * Creates the two starting PlayerStates for the two players in the
	 * game. The first player gets the first 18 pieces (six pawns, four
	 * ferzes, four wizars, two knights, and two commandos), and the second
	 * player gets the latter 18 pieces.
	 */
	private static PlayerState[] initialPlayerStates() {
		PlayerState[] initialPlayerStates = new PlayerState[2];
		for(int i = 0; i < initialPlayerStates.length; i++) {
			//Creates the ownedPieces for the PlayerState.
			Integer[] ownedPieces = new Integer[18];
			for(int j = 0; j < ownedPieces.length; j++) {
				ownedPieces[j] = (j * i) + j;
			}

			initialPlayerStates[i] = new PlayerState(0, ownedPieces);
		}
		return initialPlayerStates;
	}


	private static PieceTypedBoard initialTypedBoard() {
		//Creates 6x6 Tile set.
		List<Tile> tiles = new ArrayList<Tile>(6 * 6);
		for(int rank = 1; rank <= 6; rank++) {
			for(int file = 1; file <= 6; file++) {
				Tile tile = new Tile(rank, file);
				int tileIndex = ((rank - 1) * 6) + (file -1);
				tiles.add(tileIndex, tile);
			}
		}
		//Creates the tileIndexes for all 36 possible pieces.
		Integer[] tileIndexesOfPieces = {
			7, 8, 9, 10, -1, -1,    //white pawns
			-1, -1, -1, -1,         //white ferzes
			1, 4, -1, -1,           //white wizars
			2, -1,                  //white knights
			3, -1,                  //white commandos
			25, 26, 27, 28, -1, -1, //black pawns
			-1, -1, -1, -1,         //black ferzes
			31, 34, -1 ,-1,         //black wizars
			33, -1,                 //black knights
			32, -1                  //black commandos
		};

		Board board = new Board(tiles, Arrays.asList(tileIndexesOfPieces));
		Map<Integer, PieceType> initialPieceToType = initialPieceToType();
		return new PieceTypedBoard(board, initialPieceToType);
	}


	private static Map<Integer, PieceType> initialPieceToType() {
		/**
		 * (0-5, 18-23)   pawns, 
		 * (6-9, 24-27)   ferzes,
		 * (10-13, 28-31) wizars,
		 * (14-15, 32-33) knights,
		 * (16-17, 34-35) commandos
		 */
		Map<Integer, PieceType> initialPieceToType = 
			new HashMap<Integer, PieceType>(36);

		Integer[] pieces = {
			0,1,2,3,4,5,18,19,20,21,22,23,6,7,8,9,24,25,26,27,10,11,12,13,28,
			29,30,31,14,15,32,33,16,17,34,35
		};
		int[] sectionLengths = {12, 8, 8, 4, 4};
		PieceType[] sections = {
			PieceType.PAWN, PieceType.FERTZ, PieceType.WIZAR, PieceType.KNIGHT,
			PieceType.COMMANDO
		};

		int startingIndex = 0;
		for(int i = 0; i < sectionLengths.length; i++) {
			PieceType type = sections[i];
			for(int j = 0; j < sectionLengths[i]; j++) {
				Integer piece = pieces[j + startingIndex];
				initialPieceToType.put(piece, type);
			}
			startingIndex += sectionLengths[i];
		}
		return initialPieceToType;
	}


	/**
	 * ...
	 */
	protected GameState(
		int turnState,
		PlayerState[] playerStates,
		PieceTypedBoard typedBoard
	) {
		this.turnState = turnState;
		this.playerStates = playerStates.clone();
		this.typedBoard = typedBoard;
	}


	/**
	 * 0 - White's move
	 * 1 - White's buy
	 * 2 - Black's move
	 * 3 - Black's buy
	 * ...
	 */
	public int turnState() {
		return turnState;
	}


	/**
	 * ...
	 */
	public int nextTurnState() {
		int nextTurnState;
		if(turnState == 1 || turnState == 3) {
			nextTurnState = (turnState + 1) % 4;
		}
		else {
			if(playerStates[turnState/2].cooldown() == 0) {
				nextTurnState = (turnState + 1) % 4;
			}
			else {
				nextTurnState = (turnState + 2) % 4;
			}
		}
		return nextTurnState;
	}


	/**
	 * ...
	 */
	public PlayerState playerState(int playerNum) {
		return playerStates[playerNum];
	}


	/**
	 * ...
	 */
	public Board board() {
		return typedBoard.board();
	}


	/**
	 * ...
	 */
	public PieceTypedBoard typedBoard() {
		return typedBoard;
	}


	/**
	 * ...
	 */
	public static Map<PieceType, Integer> cooldownCosts() {
		Map<PieceType, Integer> cooldownCosts =
			new HashMap<PieceType, Integer>(5);
		cooldownCosts.put(PieceType.PAWN, 1);
		cooldownCosts.put(PieceType.FERTZ, 3);
		cooldownCosts.put(PieceType.WIZAR, 3);
		cooldownCosts.put(PieceType.KNIGHT, 7);
		cooldownCosts.put(PieceType.COMMANDO, 7);

		return cooldownCosts;
	}


	/**
	 * ...
	 */
	public static Map<PieceType, Integer> maxPieceTypes() {
		Map<PieceType, Integer> maxPieceTypes =
			new HashMap<PieceType, Integer>(5);
		maxPieceTypes.put(PieceType.PAWN, 6);
		maxPieceTypes.put(PieceType.FERTZ, 4);
		maxPieceTypes.put(PieceType.WIZAR, 4);
		maxPieceTypes.put(PieceType.KNIGHT, 2);
		maxPieceTypes.put(PieceType.COMMANDO, 2);

		return maxPieceTypes;
	}


	/**
	 * TODO: Implement
	 * notes: buy has to make sure that the turnState for buying is valid. Then
	 * it has to find the first piece off the board that belongs to the current
	 * player. Once that piece is found, remake typedBoard so that its board
	 * now has the new piece placed on tile. Then change the cooldown of the
	 * current player to match the piece they bought. Then change the
	 * turnState (maybe add method to find next turnState).
	 */
	public GameState buy(PieceType pieceType, Tile tile) {
		//Throws a RuntimeException if the GameState is in a non-buy round.
		if(turnState != 1 && turnState != 3) {
			//TODO: Improve exception message
			throw new RuntimeException("Cannot buy right now.");
		}
		int currentPlayer = turnState % 2;
		//Throws a RuntimeException if the player who is buying is in cooldown.
		if(playerStates[currentPlayer].cooldown() != 0) {
			throw new RuntimeException(
				"Player " + currentPlayer + " is still in cooldown."
			);
		}

		Integer placeablePiece = placeablePiece(pieceType);
		if(placeablePiece == null) {
			throw new RuntimeException(
				"There is no available piece of the PieceType " + pieceType +
				" to place on the board."
			);
		}

		PieceTypedBoard newTypedBoard = placePiece(placeablePiece, tile);
		int newTurnState = nextTurnState();
		PlayerState[] newPlayerStates = new PlayerState[2];
		int[] cooldowns = {
			playerStates[0].cooldown(), playerStates[1].cooldown()
		};
		newPlayerStates[currentPlayer] =
			playerStates[currentPlayer].setCooldown(newCooldown);
		return null;
	}


	/**
	 * ...
	 * FIXME: Returns piece that might not belong to proper player
	 */
	private Integer placeablePiece(PieceType pieceType) {
		Integer placeablePiece = null;
		for(Integer piece = 0; piece < 36; piece++) {
			/** Checks to see if piece is of the right PieceType and isn't being
			 * used on the board. */
			if(
				typedBoard.pieceType(piece) == pieceType 
				&& typedBoard.board().tileForPiece(piece) == null
			) {
				placeablePiece = piece;
				break;
			}
		}
		return placeablePiece;
	}


	/**
	 * ...
	 */
	private PieceTypedBoard placePiece(Integer piece, Tile tile) {
		Move[] placingNewPiece = {new Move(piece, tile)};
		Board newBoard = TurnTaker.takeTurn(
			typedBoard.board(), placingNewPiece
		);
		PieceTypedBoard newTypedBoard = new PieceTypedBoard(
			newBoard, typedBoard.pieceToType()
		);
		return newTypedBoard;
	}


	/**
	 * TODO: Implement
	 */
	public GameState passBuyRound() {
		return null;
	}


	/**
	 * TODO: Implement
	 */
	public GameState move(Move[] moves) {
		return null;
	}


	public static void main(String[] args) {
		new GameState();
	}
}
