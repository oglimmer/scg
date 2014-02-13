package de.oglimmer.scg.web;

import de.oglimmer.scg.core.Card;
import de.oglimmer.scg.core.Game;
import de.oglimmer.scg.core.GameEndException;
import de.oglimmer.scg.core.Player;
import de.oglimmer.scg.core.Type;
import de.oglimmer.scg.core.WinCondition;
import de.oglimmer.scg.printer.PrinterGamePlan;

public class GameEndTextBuilder {

	private StringBuilder buff = new StringBuilder();
	private Game game;

	public GameEndTextBuilder(Game game) {
		this.game = game;
	}

	public void buildGameEndText() {
		try {
			new WinCondition(game).checkWinner();
		} catch (GameEndException e) {
			buildGameEndText(e);
		}
	}

	public void buildGameEndText(GameEndException e, Player emailTarget) {
		buildGameEndText(e);
		addAllActions(emailTarget);
	}

	private void buildGameEndText(GameEndException e) {
		addExceptionText(e);
		addHandcardHeader();
		for (Player gamePlayer : game.getPlayers()) {
			addHandcardForPlayer(gamePlayer);
		}
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
		buff.append("Game over. The winner is ").append(e.getWinner());
	}

	public String toString() {
		return buff.toString();
	}
}