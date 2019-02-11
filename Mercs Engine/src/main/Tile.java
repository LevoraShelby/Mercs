package main;

import java.util.Objects;


/**
 * This is a structure that represents a space on a 2D board.
 * @author trevor
 */
public final class Tile implements Comparable<Tile> {
	private final int rank;
	private final int file;

	public Tile(int rank, int file) {
		this.rank = rank;
		this.file = file;
	}

	/**
	 * @return the row of the Tile.
	 */
	public int rank() {
		return rank;
	}

	/**
	 * @return the column of the Tile.
	 */
	public int file() {
		return file;
	}

	public boolean equals(Object o) {
		if(o == null) {
			return false;
		}
		Tile tile = (Tile) o;
		return this.rank == tile.rank && this.file == tile.file;
	}

	public int hashCode() {
		return Objects.hash(rank, file);
	}

	public String toString() {
		return "{rank: " + rank + ", file: " + file + "}";
	}

	public int compareTo(Tile tile) {
		if(rank < tile.rank()) {
			return -1;
		}
		else if(rank > tile.rank()) {
			return 1;
		}
		else {
			if(file < tile.file()) {
				return -1;
			}
			else if(file > tile.file()) {
				return 1;
			}
			else {
				return 0;
			}
		}
	}

	public Tile add(Tile tile) {
		return new Tile(
			rank + tile.rank(),
			file + tile.file()
		);
	}
}
