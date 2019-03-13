package mercs;

import mercs.info.GameInfo;

/**
 * Tile[] tiles:
 * 	possible relationship with Integer in pieces
 * 
 * int[] cooldowns
 * 	relationship to Integer in players
 * 
 * Integer[] players
 * 	relationship to Integers in pieces
 * 	relationship to int in cooldown
 * 
 * Integer[] pieces
 * 	relationship to Tile in tiles
 * 	relationship to Integer in players
 *  relationship to PieceType
 * 
 * Map<Player, Piece>
 * Map<Player, Cooldown>
 * Map<Piece, Tile>
 * Map<Piece, PieceType>
 */

/**
 * -controller: PlayerBoardController
 * 		*1 board Board
 * 		pieceToLogic Map<Integer,RecordBasedMoveLogic>
 * 		*3 playerToPieces Map<Integer,List<Integer>>
 * -shop: PieceTypeShop
 * 		*2 playerToCooldown Map<Integer,Integer>
 * -sequencer: TurnSequencer
 * 		turn TurnState
 * 		playerOrder List<Integer>
 * 		*2 playerToCooldown<Integer,Integer>
 * -placer: PiecePlacer
 * 		*1 board Board
 * 		pieceToGroup Map<Integer,PieceGroup>
 * 			*3 pieceToPlayer Map<Integer,Integer>
 * 			pieceToType Map<Integer,PieceType>
 * 
 * PieceAttributes:
 * 	Player
 * 	Type
 * 	RecordLogic
 * 	Tile
 * 
 * Players:
 * 	TurnState
 * 	List<Player>
 * 	Map<Player,Cooldown>
 * 
 * Map<Piece,PieceAttributes>
 * Players
 * 
 * +GameState(UpdatingPlayMaker, TurnSequencer, PieceTypeShop)
 * 
 * +board(): Board
 * 
 * +pieceToPlays(): Map<Integer, Move[][]>
 * 
 * +turn(): TurnState
 * 
 * +playerCooldowns(): Map<Integer, Integer>
 * 
 * the board
 * plays that pieces can make
 * whose turn it is
 * whether the current player is moving or buying
 * the players' cooldowns
 */

/**
 * GameInfo
 * 	Board board
 * 	Map<Integer, PieceInfo> pieceToInfo
 * 	Map<Integer, PlayerInfo> playerToInfo (internal pieces can only consist of
 * 	 given pieces)
 * 	OrderInfo order (internal playerOrder and currentPlayer can only consist
 * 	 of given players)
 * 
 * PieceInfo
 * 	PieceType type
 * 	RecordBasedMoveLogic logic
 * 
 * PlayerInfo
 * 	List<Integer> pieces
 * 	Integer piecesCaptured
 *  Integer cooldown
 * 
 * OrderInfo
 * 	TurnState turnState
 * 	List<Integer> playerOrder
 * 
 * TurnState
 * 	Integer currentPlayer
 * 	PlayState playState
 * 
 * 
 * +GameState(GameInfo)
 * 
 * @author trevor
 */


/**
 * Turn starts
 * 	if buy and there are available PieceTypes, current player can
 * 		get the available Tiles for a PieceType (needs Board), 
 * 		get the available PieceTypes (needs Board and playerToPieceToType),
 * 			depends on PieceType maxes and available Tiles (if there's no
 * 			 available Tiles, the PieceType is unavailable)
 * 			PieceType maxes are handled by player's PiecePlacer (yes, really).
 * 		buy a PieceType on a Tile with PieceTypeShop,
 * 			places the PieceType on Board with PiecePlacer,
 * 			updates the logic for all the pieces with PlayerBoardController,
 * 			updates current player's cooldown,
 * 			updates the TurnState using the TurnSequencer
 * 		pass buy
 * 			updates current player's cooldown,
 * 			updates the TurnState using the TurnSequencer,
 * 
 * 	if buy and there are no availablePieceTypes, current player can
 * 		pass buy
 * 			updates current player's cooldown,
 * 			updates the TurnState using the TurnSequencer
 * 
 * 	if move, current player can
 * 		get the pieceToPlays for their pieces,
 * 		make a play from one of their pieces,
 * 			updates the Board using the Board and the selected play,     (board)
 * 			updates player's capturedPieces (if a piece was captured),   (players)
 * 			updates the logic for all pieces with PlayerBoardController, (pieces)
 * 				remember to check for promotion
 * 			checks for win (if a piece was captured,
 * 			(if the game is won) update TurnState to reflect			 (order)
 * 			(if the game isn't won) updates the TurnState using the 	 (order)
 * 			TurnSequencer
 * 
 * 	if won, nothing more can be done.
 * @author trevor
 */
public class Game {
	private final GameInfo info;


	/**
	 * @param info the description for this game.
	 */
	public Game(GameInfo info) {
		this.info = info;
	}


	/**
	 * @return the turn that this game is in.
	 */
	public PlayState roundType() {
		return info.order().turn().playState();
	}
}
