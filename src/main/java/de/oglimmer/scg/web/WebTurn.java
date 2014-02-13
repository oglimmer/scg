package de.oglimmer.scg.web;

import de.oglimmer.scg.core.Game;
import de.oglimmer.scg.core.GameEndException;
import de.oglimmer.scg.core.Messages;
import de.oglimmer.scg.core.Play;
import de.oglimmer.scg.core.Player;
import de.oglimmer.scg.email.EmailSender;

public class WebTurn {

	private Game game;
	private String[] firstRow;
	protected Player currentPlayer;
	protected String outcome;

	public WebTurn(String gameId, String playerId, String firstRow) {
		this.firstRow = firstRow.split("-");

		game = GameManager.INSTANCE.getGame(gameId);
		if (game == null) {
			throw new RedirectException(gameId + " does not exist.", "Landing.action");
		}
		currentPlayer = game.getPlayer(playerId);
		if (!currentPlayer.isCurrentPlayer()) {
			throw new RedirectException(playerId + " is not the current player", "Landing.action");
		}
	}

	public String process() {
		outcome = null;
		processValidPlayer();
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
		Play play = game.getTurn().getPlay(firstRow);
		if (play.play()) {
			processValidOutcome();
		} else {
			processInvalidOutcome();
		}
	}

	private void processInvalidOutcome() {
		EmailSender.sendTurn(game.getTurn().getCurrentPlayer(), "Repeate Your Turn", game);
	}

	protected void processValidOutcome() {
		getLastTurnsMessages();
		sendEmailNextPlayer();
	}

	private void sendEmailNextPlayer() {
		EmailSender.sendTurn(game.getTurn().getCurrentPlayer(), "Your Turn", game);
	}

	private void getLastTurnsMessages() {
		outcome = currentPlayer.getMessages().getLastTurn();
	}

	private void processGameEnd(GameEndException e) {
		Messages.addTo(String.format("Game is over. Winner is: %s", e.getWinner()), game);
		getLastTurnsMessages();
		game.getTurn().setCurrentPlayer(null);
		for (Player p : game.getPlayers()) {
			GameEndTextBuilder getb = new GameEndTextBuilder(game);
			getb.buildGameEndText(e, p);
			EmailSender.sendPlain(p, "Game Ended", getb.toString());
		}
		// GameManager.INSTANCE.removeGameMemoryAndFile(game);
	}

}
