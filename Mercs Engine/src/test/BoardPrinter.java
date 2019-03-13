package test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import main.Board;
import main.Tile;



public class BoardPrinter {
	public static void printBoard(Board board) {
		Tile[] tiles = board.tiles().toArray(new Tile[board.tiles().size()]);
		Arrays.sort(tiles);
		List<Tile[]> tilesByRank = new ArrayList<Tile[]>();

		int to = 0;
		while(to < tiles.length) {
			int currentRank = tiles[to].rank();
			int from = to;
			while(to < tiles.length && tiles[to].rank() == currentRank) {
				to++;
			}
			tilesByRank.add(Arrays.copyOfRange(tiles, from, to));
		}

		boolean first = true;
		for(int i = tilesByRank.size() - 1; i >= 0; i--) {
			Tile[] rankOfTiles = tilesByRank.get(i);
			if(!first) {
				int currentRank = rankOfTiles[0].rank();
				int previousRank = tilesByRank.get(i + 1)[0].rank();
				for(int j = previousRank - currentRank; j > 0; j--) {
					System.out.println();
				}
			}
			else {
				first = false;
			}

			int previousFile = 0;
			for(Tile tile : rankOfTiles) {
				for(int j = tile.file() - previousFile; j > 1; j--) {
					System.out.print("  ");
				}

				Integer pieceOnTile = board.pieceOnTile(tile);
				if(pieceOnTile == null) {
					System.out.print("X ");
				}
				else {
					System.out.print(pieceOnTile);
					if(pieceOnTile < 10) {
						System.out.print(" ");
					}
				}
				System.out.print(" ");
				previousFile = tile.file();
			}
		}
		
		System.out.println();
	}
}
