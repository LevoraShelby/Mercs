package mercs;

public final class PlayerState {
	private final int cooldown;
	private final Integer[] ownedPieces;

	public PlayerState(int cooldown, Integer[] ownedPieces) {
		this.cooldown = cooldown;
		this.ownedPieces = ownedPieces.clone();
	}

	public int cooldown() {
		return cooldown;
	}

	public PlayerState setCooldown(int newCooldown) {
		return new PlayerState(newCooldown, ownedPieces);
	}

	public Integer[] ownedPieces() {
		return ownedPieces.clone();
	}
}