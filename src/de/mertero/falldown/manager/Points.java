package de.mertero.falldown.manager;

public enum Points {
	KILL(5), FIRST_PLAYER_WIN(20), SECOND_PLAYER_WIN(15), THIRD_PLAYER_WIN(10);
	public int points;

	Points(int points) {
		this.points = points;
	}

	public int getPoints() {
		return points;
	}
}