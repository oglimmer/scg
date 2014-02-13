package de.oglimmer.scg.core;

public class GameEndException extends Exception {

	private static final long serialVersionUID = -3422287115986184739L;

	public GameEndException(String winner) {
		super(winner);
	}

	public String getWinner() {
		return getMessage();
	}

}
