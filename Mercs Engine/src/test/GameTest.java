package test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import main.Board;
import mercs.PieceType;
import mercs.PlayState;
import mercs.TurnState;
import mercs.info.GameInfo;
import mercs.info.OrderInfo;
import mercs.info.PieceInfo;
import mercs.info.PlayerInfo;
import mercs.pieceLogics.CommandoLogic;
import mercs.pieceLogics.Leaper;
import mercs.pieceLogics.PawnLogic;



public class GameTest {
	private static Map<Integer, Boolean> pieceToIsPieceWhite = 
		pieceToIsPieceWhite();

	private static Map<Integer, PieceType> pieceToType = pieceToType();

	private static Map<PieceType, Boolean> typeToDoesTypeHaveColoredLogic =
		typeToDoesTypeHaveColoredLogic();

	private static Map<PieceType, Function<
		Integer, Function<Board, PieceInfo>
	>> uncoloredPieceTypeToPieceInfoFunction =
		uncoloredPieceTypeToPieceInfoFunction();

	private static Map<PieceType, Function<
		Boolean, Function<Integer, Function<Board, PieceInfo>>
	>> coloredPieceTypeToPieceInfoFunction =
		coloredPieceTypeToPieceInfoFunction();


	/**
	 * @return The GameInfo that represents the start of a standard game of
	 * Mercs between player 1 and player 2.
	 */
	public static GameInfo start() {
		Board board = Board.startingMercsBoard();

		Map<Integer, PieceInfo> pieceToInfo = new HashMap<>();
		for(Integer piece : pieces()) {
			PieceInfo pieceInfo;
			PieceType type = pieceToType.get(piece);
			if(typeToDoesTypeHaveColoredLogic.get(type)) {
				pieceInfo = coloredPieceTypeToPieceInfoFunction.get(type)
					.apply(pieceToIsPieceWhite.get(piece))
					.apply(piece)
					.apply(board);
			}
			else {
				pieceInfo = uncoloredPieceTypeToPieceInfoFunction.get(type)
					.apply(piece)
					.apply(board);
			}
			pieceToInfo.put(piece, pieceInfo);
		}

		Map<Integer, PlayerInfo> playerToInfo = new HashMap<>();
		Set<Integer> player1Pieces = new HashSet<>();
		Set<Integer> player2Pieces = new HashSet<>();
		for(Integer piece : pieces()) {
			if(pieceToIsPieceWhite.get(piece)) {
				player1Pieces.add(piece);
			}
			else {
				player2Pieces.add(piece);
			}
		}
		Integer player1 = 1; Integer player2 = 2;
		playerToInfo.put(player1, new PlayerInfo(player1Pieces, 0, 0));
		playerToInfo.put(player2, new PlayerInfo(player2Pieces, 0, 0));

		OrderInfo order = new OrderInfo(
			new TurnState(player1, PlayState.MOVE),
			player1, player2
		);

		return new GameInfo(board, pieceToInfo, playerToInfo, order);
	}


	private static Set<Integer> pieces() {
		Set<Integer> pieces = new HashSet<>();
		for(Integer piece = 0; piece < 36; piece++) {
			pieces.add(piece);
		}
		return pieces;
	}


	private static Map<PieceType, Boolean> typeToDoesTypeHaveColoredLogic() {
		Map<PieceType, Boolean> typeToDoesTypeHaveColoredLogic =
			new HashMap<>();
		typeToDoesTypeHaveColoredLogic.put(PieceType.PAWN, true);
		typeToDoesTypeHaveColoredLogic.put(PieceType.FERZ, false);
		typeToDoesTypeHaveColoredLogic.put(PieceType.WAZIR, false);
		typeToDoesTypeHaveColoredLogic.put(PieceType.KNIGHT, false);
		typeToDoesTypeHaveColoredLogic.put(PieceType.COMMANDO, true);
		return typeToDoesTypeHaveColoredLogic;
	}


	private static Map<PieceType, Function<
		Integer, Function<Board, PieceInfo>
	>> uncoloredPieceTypeToPieceInfoFunction() {
		Map<PieceType, Function<
			Integer, Function<Board, PieceInfo>
		>> uncoloredPieceTypeToPieceInfoFunction = new HashMap<>();

		uncoloredPieceTypeToPieceInfoFunction.put(PieceType.FERZ,
			piece -> board -> {
				return new PieceInfo(
					PieceType.FERZ, Leaper.ferz(piece, board)
				);
			}
		);
		uncoloredPieceTypeToPieceInfoFunction.put(PieceType.WAZIR,
			piece -> board -> {
				return new PieceInfo(
					PieceType.WAZIR, Leaper.wazir(piece, board)
				);
			}
		);
		uncoloredPieceTypeToPieceInfoFunction.put(PieceType.KNIGHT,
			piece -> board -> {
				return new PieceInfo(
					PieceType.KNIGHT, Leaper.knight(piece, board)
				);
			}
		);

		return uncoloredPieceTypeToPieceInfoFunction;
	}


	private static Map<PieceType, Function<
		Boolean, Function<Integer, Function<Board, PieceInfo>>
	>> coloredPieceTypeToPieceInfoFunction() {
		Map<PieceType, Function<
			Boolean, Function<Integer, Function<Board, PieceInfo>>
		>> coloredPieceTypeToPieceInfoFunction = new HashMap<>();

		coloredPieceTypeToPieceInfoFunction.put(PieceType.PAWN,
			isWhite -> piece -> board -> {
				return new PieceInfo(
					PieceType.PAWN, new PawnLogic(isWhite, piece, board)
				);
			}
		);
		coloredPieceTypeToPieceInfoFunction.put(PieceType.COMMANDO,
			isWhite -> piece -> board -> {
				return new PieceInfo(
					PieceType.COMMANDO, new CommandoLogic(isWhite, piece, board)
				);
			}
		);

		return coloredPieceTypeToPieceInfoFunction;
	}


	private static Map<Integer, PieceType> pieceToType() {
		Map<Integer, PieceType> pieceToType = new HashMap<>();
		for(Integer piece : pieces()) {
			if((piece >= 0 && piece <= 5) || (piece >= 18 && piece <= 23)) {
				pieceToType.put(piece, PieceType.PAWN);
			}
			else if((piece >= 6 && piece <= 9) || (piece >= 24 && piece <= 27)) {
				pieceToType.put(piece, PieceType.FERZ);
			}
			else if((piece >= 10 && piece <= 13) || (piece >= 28 && piece <= 31)) {
				pieceToType.put(piece, PieceType.WAZIR);
			}
			else if((piece >= 14 && piece <= 15) || (piece >= 32 && piece <= 33)) {
				pieceToType.put(piece, PieceType.KNIGHT);
			}
			else if((piece >= 16 && piece <= 17) || (piece >= 34 && piece <= 35)) {
				pieceToType.put(piece, PieceType.COMMANDO);
			}
			else {
				throw new IndexOutOfBoundsException("piece is out of range.");
			}
		}
		return pieceToType;
	}


	private static Map<Integer, Boolean> pieceToIsPieceWhite() {
		Map<Integer, Boolean> pieceToIsPieceWhite = new HashMap<>();
		for(Integer piece : pieces()) {
			if(piece >= 0 && piece <= 17) {
				pieceToIsPieceWhite.put(piece, true);
			}
			else if(piece >= 18 && piece <= 35) {
				pieceToIsPieceWhite.put(piece, false);
			}
			else {
				throw new IndexOutOfBoundsException("piece is out of range.");
			}
		}
		return pieceToIsPieceWhite;
	}
}
