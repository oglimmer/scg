package de.oglimmer.scg.email;

import de.oglimmer.scg.web.WebTurn;

public class EmailTurn extends WebTurn {

	public EmailTurn(String gameId, String playerId, String firstRow) {
		super(gameId, playerId, firstRow);
	}

	@Override
	protected void processValidOutcome() {
		super.processValidOutcome();
		sendEmailLastPlayer();
	}

	private void sendEmailLastPlayer() {
		EmailSender.send(currentPlayer, "Outcome Of Your Turn", outcome, false);
	}

}
