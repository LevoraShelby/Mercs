package mercs;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import main.Board;
import main.RecordBasedMoveLogic;
import main.Tile;
import mercs.PieceType;
import mercs.PlayState;
import mercs.info.*;
import mercs.pieceLogics.*;



public class StandardMercsUtil {
	public static GameInfo startingInfo() {
		Board board = Board.startingMercsBoard();

		Map<Integer, PieceInfo> pieceToInfo = new HashMap<>();
		for(Integer piece : board.pieces()) {
			pieceToInfo.put(piece, pieceInfoFromPiece(piece, board));
		}

		PlayerInfo firstPlayerInfo = new PlayerInfo(
			alliesOfTeam(true), 0, 0
		);
		PlayerInfo secondPlayerInfo = new PlayerInfo(
			alliesOfTeam(false), 0, 0
		);

		OrderInfo order = new OrderInfo(true, PlayState.MOVE);

		return new GameInfo(
			board, pieceToInfo, firstPlayerInfo, secondPlayerInfo, order
		);
	}


	public static GameInfo addPiece(
		GameInfo info, Tile tile,
		PieceType addedType, boolean isFirstPlayersPieceBeingAdded
	) {
		Integer addedPiece = availablePieceRef(info.board().pieces());

		Map<Integer, Tile> pieceToTile = info.board().pieceToTile();
		pieceToTile.put(addedPiece, tile);
		Board board = new Board(info.board().tiles(), pieceToTile);

		Set<Integer> firstPlayersPieces = info.firstPlayerInfo().pieces();
		Set<Integer> secondPlayersPieces = info.secondPlayerInfo().pieces();
		if(isFirstPlayersPieceBeingAdded) {
			firstPlayersPieces.add(addedPiece);
		}
		else {
			secondPlayersPieces.add(addedPiece);
		}

		Map<Integer, PieceInfo> pieceToInfo = info.pieceToInfo();
		for(Integer piece : info.pieceToInfo().keySet()) {
			boolean isFirstPlayersPiece = info
				.firstPlayerInfo()
				.pieces().contains(piece);

			PieceType type = info.pieceToInfo().get(piece).type();
			RecordBasedMoveLogic logic = unalliedLogic(
				piece, type, isFirstPlayersPiece, board
			);
			if(isFirstPlayersPiece) {
				logic = new AlliedLogic(logic, firstPlayersPieces);
			}
			else {
				logic = new AlliedLogic(logic, secondPlayersPieces);
			}
			pieceToInfo.put(piece, new PieceInfo(type, logic));
		}
		RecordBasedMoveLogic addedLogic = unalliedLogic(
			addedPiece, addedType, isFirstPlayersPieceBeingAdded, info.board()
		);
		if(isFirstPlayersPieceBeingAdded) {
			addedLogic = new AlliedLogic(addedLogic, firstPlayersPieces);	
		}
		else {
			addedLogic = new AlliedLogic(addedLogic, secondPlayersPieces);
		}
		pieceToInfo.put(addedPiece, new PieceInfo(addedType, addedLogic));
	}


	private static RecordBasedMoveLogic unalliedLogic(
		Integer piece, PieceType type, boolean isFirstPlayersPiece, Board board
	) {
		if(type == PieceType.PAWN) {
			return new PawnLogic(isFirstPlayersPiece, piece, board);
		}
		else if(type == PieceType.FERZ) {
			return Leaper.ferz(piece, board);
		}
		else if(type == PieceType.WAZIR) {
			return Leaper.wazir(piece, board);
		}
		else if(type == PieceType.KNIGHT) {
			return Leaper.knight(piece, board);
		}
		else if(type == PieceType.COMMANDO) {
			return new CommandoLogic(isFirstPlayersPiece, piece, board);
		}
		else {
			return null;
		}
	}


	private static Integer availablePieceRef(Set<Integer> pieces) {
		Integer pieceRef = 0;
		for(Integer piece : pieces) {
			if(pieceRef == piece) {
				pieceRef++;
			}
			else {
				return pieceRef;
			}
		}
		return pieceRef;
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