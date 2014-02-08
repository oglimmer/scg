package de.oglimmer.scg.printer;

import de.oglimmer.scg.core.AssociatedCard;
import de.oglimmer.scg.core.Card;
import de.oglimmer.scg.core.Game;
import de.oglimmer.scg.core.Player;

public class PrinterDebug {

	private StringBuilder buff = new StringBuilder(1024);

	private Game game;

	public PrinterDebug(Game game) {
		this.game = game;
	}

	public void debugPrint() {
		addGameData();
		addStackData();
		addPlayerData();
	}

	private void addPlayerData() {
		for (Player p : game.getPlayers()) {
			addPlayer(p);
		}
	}

	private void addStackData() {
		buff.append("open:").append(game.getStackOpen().getCards().size()).append(PrinterGame.CR);
		buff.append("hidden:").append(game.getStackHidden().getCards().size()).append(PrinterGame.CR);
	}

	private void addGameData() {
		buff.append("GameId:").append(game.getId()).append(PrinterGame.CR);
		buff.append("current:").append(game.getCurrentPlayer().getNo()).append('/')
				.append(game.getCurrentPlayer().getEmail()).append(PrinterGame.CR);
	}

	private void addPlayer(Player p) {
		buff.append(p.getNo()).append(" - ").append(p.getEmail()).append(" (<a href='Select.action?gid=")
				.append(game.getId()).append("&pid=").append(p.getId()).append("'>").append(p.getId()).append("</a>):");
		addPlayerStatus(p);
		addCards(p);
		buff.append(PrinterGame.CR);
	}

	private void addPlayerStatus(Player p) {
		if (p.isDead()) {
			buff.append("dead");
		} else {
			buff.append("alive");
		}
	}

	private void addCards(Player p) {
		buff.append(" ");
		for (AssociatedCard ac : p.getCardHand().getAssociatedCards()) {
			Card card = ac.getCard();
			buff.append(ac.getType());
			buff.append('=').append(card.getNo()).append(':').append(card.getName()).append(", ");
		}
	}

	public String toString() {
		return buff.toString();
	}

}
