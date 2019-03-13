package test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import main.Board;
import main.RecordBasedMoveLogic;
import mercs.PieceType;
import mercs.PlayState;
import mercs.TurnState;
import mercs.info.*;
import mercs.pieceLogics.*;



public class MercsStartingGameInfo implements Supplier<GameInfo> {
	public GameInfo get() {
		return startingInfo();
	}


	public static GameInfo startingInfo() {
		Board board = Board.startingMercsBoard();

		Map<Integer, PieceInfo> pieceToInfo = new HashMap<>();
		for(Integer piece : board.pieces()) {
			pieceToInfo.put(piece, pieceInfoFromPiece(piece, board));
		}

		Map<Integer, PlayerInfo> playerToInfo = new HashMap<>();
		Integer player1 = 1; Integer player2 = 2;
		playerToInfo.put(player1, new PlayerInfo(
			alliesOfTeam(true), 0, 0
		));
		playerToInfo.put(player2, new PlayerInfo(
			alliesOfTeam(false), 0, 0
		));

		OrderInfo order = new OrderInfo(
			new TurnState(player1, PlayState.MOVE), player1, player2
		);

		return new GameInfo(board, pieceToInfo, playerToInfo, order);
	}


	private static PieceInfo pieceInfoFromPiece(Integer piece, Board board) {
		Boolean isWhite = isPieceWhite(piece);
		if(isWhite == null) {
			return null;
		}

		PieceType type;
		RecordBasedMoveLogic logic;
		if( (piece >= 0 && piece < 6) || (piece >= 18 && piece < 24) ) {
			type = PieceType.PAWN;
			logic = new PawnLogic(isWhite, piece, board);
		}
		else if( (piece >= 6 && piece < 10) || (piece >= 24 && piece < 28) ) {
			type = PieceType.FERZ;
			logic = Leaper.ferz(piece, board);
		}
		else if( (piece >= 10 && piece < 14) || (piece >= 28 && piece < 32) ) {
			type = PieceType.WAZIR;
			logic = Leaper.wazir(piece, board);
		}
		else if( (piece >= 14 && piece < 16) || (piece >= 32 && piece < 34) ) {
			type = PieceType.KNIGHT;
			logic = Leaper.knight(piece, board);
		}
		else if( (piece >= 16 && piece < 18) || (piece >= 34 && piece < 36) ) {
			type = PieceType.COMMANDO;
			logic = new CommandoLogic(isWhite, piece, board);
		}
		else {
			return null;
		}

		Set<Integer> alliesOfPiece = alliesOfTeam(isWhite);
		return new PieceInfo(
			type, new AlliedLogic(logic, alliesOfPiece)
		);
	}


	private static Boolean isPieceWhite(Integer piece) {
		if(piece >= 0 && piece < 18) {
			return true;
		}
		else if(piece >= 18 && piece < 36) {
			return false;
		}
		else {
			return null;
		}
	}


	private static Set<Integer> alliesOfTeam(boolean isWhite) {
		Set<Integer> allies = new HashSet<>();
		if(isWhite) {
			for(Integer piece = 0; piece < 18; piece++) {
				allies.add(piece);
			}
		}
		else {
			for(Integer piece = 18; piece < 36; piece++) {
				allies.add(piece);
			}
		}
		return allies;
	}
}