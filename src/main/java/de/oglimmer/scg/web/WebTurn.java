package de.oglimmer.scg.web;

import de.oglimmer.scg.core.Card;
import de.oglimmer.scg.core.Game;
import de.oglimmer.scg.core.GameEndException;
import de.oglimmer.scg.core.Play;
import de.oglimmer.scg.core.Player;
import de.oglimmer.scg.core.Type;
import de.oglimmer.scg.email.EmailSender;
import de.oglimmer.scg.printer.PrinterGamePlan;

public class WebTurn {

	private Game game;
	private String[] firstRow;
	protected Player currentPlayer;
	protected String outcome;

	public WebTurn(String gameId, String playerId, String firstRow) {
		this.firstRow = firstRow.split("-");

		game = GameManager.INSTANCE.getGame(gameId);
		if (game == null) {
			throw new RuntimeException(gameId + " does not exist.");
		}
		currentPlayer = game.getPlayer(playerId);
		if (!currentPlayer.isCurrentPlayer()) {
			throw new RuntimeException(playerId + " is not the current player");
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
		for (Player p : game.getPlayers()) {
			GameEndTextBuilder getb = new GameEndTextBuilder();
			getb.buildGameEndText(e, p);
			EmailSender.sendPlain(p, "Game Ended", getb.toString());
		}
		GameManager.INSTANCE.removeGameMemoryAndFile(game);
	}

	class GameEndTextBuilder {
		StringBuilder buff = new StringBuilder();

		void buildGameEndText(GameEndException e, Player emailTarget) {
			addExceptionText(e);
			addHandcardHeader();
			for (Player gamePlayer : game.getPlayers()) {
				addHandcardForPlayer(gamePlayer);
			}
			addAllActions(emailTarget);
		}

		private void addAllActions(Player emailTarget) {
			buff.append(PrinterGamePlan.CR + PrinterGamePlan.CR);
			buff.append("The game:");
			buff.append(PrinterGamePlan.CR);
			buff.append(emailTarget.getMessages().getAll());
		}

		private void addHandcardForPlayer(Player gamePlayer) {
			buff.append(gamePlayer.getDisplayName());
			buff.append(" => ");
			if (gamePlayer.isDead()) {
				buff.append("dead");
			} else {
				Card handCard = gamePlayer.getCardHand().getCard(Type.HAND);
				buff.append(handCard.getName());
				buff.append(" (");
				buff.append(handCard.getNo());
				buff.append(")");
			}
			buff.append(PrinterGamePlan.CR);
		}

		private void addHandcardHeader() {
			buff.append(PrinterGamePlan.CR + PrinterGamePlan.CR);
			buff.append("Handcards of the players:");
			buff.append(PrinterGamePlan.CR);
		}

		private void addExceptionText(GameEndException e) {
			buff.append(e.getMessage());
		}

		public String toString() {
			return buff.toString();
		}
	}

}
