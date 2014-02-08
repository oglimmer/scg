package de.oglimmer.scg.web;

import de.oglimmer.scg.core.Card;
import de.oglimmer.scg.core.Game;
import de.oglimmer.scg.core.GameEndException;
import de.oglimmer.scg.core.Player;
import de.oglimmer.scg.core.Type;
import de.oglimmer.scg.email.EmailSender;

public class WebTurn {

	private Game game;
	private String[] firstRow;
	protected Player currentPlayer;
	protected String outcome;

	public WebTurn(String gameId, String playerId, String firstRow) {
		this.firstRow = firstRow.split("-");

		game = GameManager.INSTANCE.getGame(gameId);
		currentPlayer = game.getPlayer(playerId);
	}

	public String process() {
		outcome = null;
		if (game.getCurrentPlayer() == currentPlayer) {
			processValidPlayer();
		}
		return outcome;
	}

	private void processValidPlayer() {
		try {
			processPlay();
		} catch (GameEndException e) {
			processGameEnd(e);
		}
	}

	private void processPlay() throws GameEndException {
		if (game.play(firstRow)) {
			processValidOutcome();
		} else {
			processInvalidOutcome();
		}
	}

	private void processInvalidOutcome() {
		EmailSender.sendTurn(game.getCurrentPlayer(), "Repeate Your Turn", game);
	}

	protected void processValidOutcome() {
		getLastTurnsMessages();
		sendEmailNextPlayer();
	}

	private void sendEmailNextPlayer() {
		EmailSender.sendTurn(game.getCurrentPlayer(), "Your Turn", game);
	}

	private void getLastTurnsMessages() {
		outcome = currentPlayer.getMessages().getLastTurn();
	}

	private void processGameEnd(GameEndException e) {
		for (Player p : game.getPlayers()) {
			EmailSender.send(p, "Game Ended", buildGameEndText(e, p), false);
		}
		GameManager.INSTANCE.removeGame(game);
	}

	private String buildGameEndText(GameEndException e, Player emailTarget) {
		StringBuilder buff = new StringBuilder();
		buff.append(e.getMessage());
		buff.append("\r\n\r\n");
		buff.append("Handcards of the players:");
		buff.append("\r\n");
		for (Player gamePlayer : game.getPlayers()) {
			buff.append(gamePlayer.getDisplayName());
			buff.append(" => ");
			if (gamePlayer.isDead()) {
				buff.append("dead");
			} else {
				Card handCard = gamePlayer.getCardHand().getCard(Type.HAND).getCard();
				buff.append(handCard.getName());
				buff.append(" (");
				buff.append(handCard.getNo());
				buff.append(")");
			}
			buff.append("\r\n");
		}
		buff.append("\r\n\r\n");
		buff.append("The game:");
		buff.append("\r\n");
		buff.append(emailTarget.getMessages().getAll());
		return buff.toString();
	}

}
