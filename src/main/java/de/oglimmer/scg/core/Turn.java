package de.oglimmer.scg.core;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Iterator;

import lombok.Getter;
import lombok.Setter;

public class Turn implements Serializable {

	private static final long serialVersionUID = 1L;

	@Getter
	private int turnNo = 1;

	@Getter
	private Game game;

	@Setter
	private transient Iterator<Player> playerIterator;

	@Getter
	@Setter
	private Player currentPlayer;

	public Turn(Game game) {
		this.game = game;
	}

	private void readObject(ObjectInputStream ios) throws ClassNotFoundException, IOException {
		ios.defaultReadObject();
		playerIterator = game.getPlayers().iterator();
		while (playerIterator.next().getNo() != currentPlayer.getNo()) {
			// next() already used
		}
	}

	private void writeObject(ObjectOutputStream oos) throws IOException {
		oos.defaultWriteObject();
	}

	public void nextTurn() {
		determineNextPlayer();
		currentPlayer.getCardHand().putOpenCardsToOpenStack();
		currentPlayer.getCardHand().addCard(game.getStackHidden().takeTop(), Type.DRAWN);
		game.persist();
		incTurnNo();
	}

	private void determineNextPlayer() {
		nextPlayer();
		while (currentPlayer.isDead()) {
			nextPlayer();
		}
	}

	private void nextPlayer() {
		if (!playerIterator.hasNext()) {
			playerIterator = game.getPlayers().iterator();
		}
		currentPlayer = playerIterator.next();
	}

	private void incTurnNo() {
		turnNo++;
	}

	public void start() {
		playerIterator = game.getPlayers().iterator();
	}

	public Play getPlay(String[] cmdLine) {
		return new Play(this, cmdLine);
	}

}
