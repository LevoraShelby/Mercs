package mercs;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import main.Board;
import main.Move;
import main.Tile;
import mercs.info.GameInfo;
import mercs.info.OrderInfo;
import mercs.info.PieceInfo;
import mercs.info.PlayerInfo;



/**
 * This represents the options available to the current player in a round of 
 * buying in a game of Mercs.
 * @author trevor
 */
public class BuyRound {
	private static final List<PieceType> supportedPieceTypes = Arrays.asList(
		PieceType.PAWN, PieceType.FERZ, PieceType.WAZIR, PieceType.KNIGHT,
		PieceType.COMMANDO
	);

	private final GameInfo info;


	/**
	 * @param info The GameInfo that describes the current game of Mercs. info
	 * must be in a BUY round. (non-null)
	 */
	public BuyRound(GameInfo info) {
		handleExceptions(info);

		this.info = info;
	}


	/**
	 * @param type The type of piece that is being bought.
	 * @param tile The tile that the bought piece will be placed on.
	 * @return The resultant GameInfo from the current player buying a piece of
	 * the PieceType type and having it placed on tile.
	 */
	public GameInfo buy(PieceType type, Tile tile) {
		//Checks that that a piece of type can be placed on tile.
		if(!pieceTypeToAvailableTiles().get(type).contains(tile)) {
			throw new IllegalArgumentException(
				"tile is not an available area to place a piece of type: The "
				+ "current player isn't allowed to buy a " + type + " piece "
				+ "and place it on the tile " + tile
			);
		}

		//Makes the Move placePiece, that places a piece of type onto tile.
		Integer pieceToPlace = availablePieceForType(type);
		Move placePiece = new Move(pieceToPlace, tile);

		//Creates a new board based on the Move placePiece.
		Board newBoard = info.board().makeMove(placePiece);

		//Creates a new pieceToInfo based on the Move placePiece.
		Map<Integer, PieceInfo> newPieceToInfo = new HashMap<>();
		Move[] play = {placePiece};
		info.pieceToInfo().forEach( (piece, pieceInfo) -> {
			/**
			 * Creates a new PieceInfo that holds a version of the old piece's
			 * logic that has been updated based on the Move placePiece.
			 */
			PieceInfo newPieceInfo = new PieceInfo(
				pieceInfo.type(),
				pieceInfo.logic().update(newBoard, play)
			);
			newPieceToInfo.put(piece, newPieceInfo);
		});

		/**
		 * Creates a new playerToInfo where the current player has a
		 * cooldown set to a new value based on the piece that they bought.
		 */
		Map<Integer, PlayerInfo> newPlayerToInfo = info.playerToInfo();
		Integer currentPlayer = info.order().turn().currentPlayer();
		newPlayerToInfo.put(
			currentPlayer,
			new PlayerInfo(
				info.playerToInfo().get(currentPlayer).pieces(),
				info.playerToInfo().get(currentPlayer).numPiecesCaptured(),
				cooldownForPieceType(type)
			)
		);

		return new GameInfo(
			newBoard, newPieceToInfo, newPlayerToInfo, nextOrder()
		);
	}


	/**
	 * @return The resultant GameInfo from the current player deciding to
	 * pass for their buy round.
	 */
	public GameInfo pass() {
		return new GameInfo(
			info.board(), info.pieceToInfo(), info.playerToInfo(), nextOrder()
		);
	}


	/**
	 * @return A mapping of available PieceTypes to the Tiles that the current
	 * player can place them to.
	 */
	public Map<PieceType, Set<Tile>> pieceTypeToAvailableTiles() {
		Map<PieceType, Set<Tile>> pieceTypeToAvailableTiles = new HashMap<>();
		for(PieceType type : supportedPieceTypes) {
			/**
			 * If this piece type has an piece that is available for placing,
			 * it adds the tiles that the piece can be placed on.
			 */
			if(availablePieceForType(type) != null) {
				Set<Tile> availableTiles = availableTilesForPieceType(type);
				pieceTypeToAvailableTiles.put(type, availableTiles);
			}
			/**
			 * If there isn't a piece available for placing, adds an empty set
			 * of tiles.
			 */
			else {
				pieceTypeToAvailableTiles.put(type, new HashSet<>());
			}
		}
		return pieceTypeToAvailableTiles;
	}


	/**
	 * @param type The PieceType of the hypothetical piece that's being placed.
	 * @return The Set of Tiles that the current player can place a piece of
	 * PieceType type on.
	 */
	private Set<Tile> availableTilesForPieceType(PieceType type) {
		Set<Tile> placementTiles = new HashSet<>();
		int placementRankForType = placementRankForPieceType(type);

		//Goes through each tile on the board.
		for(Tile tile : info.board().tiles()) {
			/**
			 * Checks to see if tile is of the correct rank and is currently
			 * unoccupied. If it is, the tile is added to placementTiles.
			 */
			if(
				tile.rank() == placementRankForType 
				&& info.board().pieceOnTile(tile) == null
			) {
				placementTiles.add(tile);
			}
		}

		return placementTiles;
	}


	/**
	 * @param type The type of piece being bought.
	 * @return A piece of type that can be bought by the current player. If no
	 * such piece exists, this method returns null.
	 */
	private Integer availablePieceForType(PieceType type) {
		Integer currentPlayer = info.order().turn().currentPlayer();
		//Goes through each of the current player's pieces.
		for(Integer piece : info.playerToInfo().get(currentPlayer).pieces()) {
			if(
				info.board().tileForPiece(piece) == null
				&& info.pieceToInfo().get(piece).type() == type
			) {
				return piece;
			}
		}
		
		return null;
	}


	/**
	 * @param type The PieceType of the hypothetical piece that's being placed.
	 * @return The rank that the PieceType type can be placed on for the current
	 * player.
	 */
	private int placementRankForPieceType(PieceType type) {
		Integer whitePlayer = info.order().firstPlayer();
		Integer currentPlayer = info.order().turn().currentPlayer();

		if(type == PieceType.PAWN) {
			if(currentPlayer == whitePlayer) {
				return 2;
			}
			else {
				return 5;
			}
		}
		else {
			if(currentPlayer == whitePlayer) {
				return 1;
			}
			else {
				return 6;
			}
		}
	}


	/**
	 * @param type The type of piece being bought.
	 * @return The number of buy rounds sat out for buying a piece of type.
	 */
	private int cooldownForPieceType(PieceType type) {
		if(type == PieceType.PAWN) {
			return 1;
		}
		else if(type == PieceType.FERZ || type == PieceType.WAZIR) {
			return 3;
		}
		else if(type == PieceType.COMMANDO || type == PieceType.KNIGHT) {
			return 5;
		}
		else {
			throw new IllegalArgumentException(
				"type is not a supported PieceType: " + type + " is not a "
				+ "recognized PieceType."
			);
		}
	}


	/**
	 * @return The OrderInfo that any GameInfo coming from this BuyRound will
	 * have.
	 */
	private OrderInfo nextOrder() {
		//Finds out the player whose turn it is currently not.
		Integer currentPlayer = info.order().turn().currentPlayer();
		Integer otherPlayer;
		if(info.order().firstPlayer() == currentPlayer) {
			otherPlayer = info.order().secondPlayer();
		}
		else {
			otherPlayer = info.order().firstPlayer();
		}

		return new OrderInfo(
			new TurnState(
				otherPlayer,
				PlayState.MOVE
			),
			info.order().firstPlayer(),
			info.order().secondPlayer()
		);
	}


	/**
	 * Throws exceptions if any of the arguments aren't allowed for the
	 * constructor.
	 */
	private static void handleExceptions(GameInfo info) {
		if(info == null) {
			throw new NullPointerException(
				"info is null: There isn't a game for this round to belong to."
			);
		}
		if(info.order().turn().playState() != PlayState.BUY) {
			throw new IllegalArgumentException(
				"playState of info isn't BUY: BuyRound can't occur when its "
				+ "game isn't in a BUY playState."
			);
		}
	}
}

/**
 * Buying a piece
 * Checks that the tile is free, and that the piece type being placed
 * to it is allowed to be placed to that tile. Finds a piece that
 * belongs to the player, is off the board, and is of the correct type.
 * Once that piece is found, a move is generated to place it. A new
 * board is generated from the move. Each piece's logic is updated from
 * the move. Now that the piece has been placed, the current player,
 * the one who bought the piece, has their cooldown set to a value
 * dependent on the type of piece that is being bought. A new turn
 * state is generated where it is the next player's turn to move (if it
 * was the first player's turn, it is then the second player's and vice
 * versa). This is all compiled into a new GameInfo, that is then
 * returned.
 *
 *
 * Passing
 * A new turn state is generated where it is the next player's turn to
 * move (if it was the first player's turn, it is then the second
 * player's turn and vice versa). This is used to compile a new
 * GameInfo, that is then returned.
 * 
 * 
 * Viewing available tiles for piece types
 * For each different piece type where there exists a piece that
 * belongs to the player and is off the board, gets each tile available
 * for that piece type that is free. For those where there doesn't
 * exist a piece that belongs to the player and is off the board, it
 * will be shown that no tiles are available to them. This information
 * should be returned as a mapping of piece types to sets of tiles.
 */