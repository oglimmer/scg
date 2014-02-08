package de.oglimmer.scg.printer;

import de.oglimmer.scg.core.AssociatedCard;
import de.oglimmer.scg.core.Card;
import de.oglimmer.scg.core.Player;
import de.oglimmer.scg.core.Type;

public class PrinterPlayer {

	protected StringBuilder buff = new StringBuilder(1024);

	protected Player player;

	public PrinterPlayer(Player player) {
		this.player = player;
	}

	public void addPlayer() {
		if (player != player.getGame().getCurrentPlayer()) {
			buff.append(player.getEmail()).append(" (").append(player.getNo()).append(") is ");
		} else {
			buff.append("You are ");
		}
		addOtherPlayer();
		buff.append(PrinterGame.CR);
	}

	protected void addOtherPlayer() {
		if (player.isDead()) {
			buff.append("dead");
		} else {
			addIsTargatableStatus();
			buff.append(" ");
			addOpenCards();
		}
	}

	private void addIsTargatableStatus() {
		if (player.isTargetable()) {
			buff.append("alive");
		} else {
			buff.append("protected");
		}
	}

	protected void addOpenCards() {
		for (AssociatedCard ac : player.getCardHand().getAssociatedCards()) {
			addOpenCard(ac);
		}
	}

	private void addOpenCard(AssociatedCard ac) {
		if (ac.getType() == Type.OPEN) {
			if (buff.length() > 0) {
				buff.append(", ");
			}
			buff.append(ac.getCard().getName());
		}
	}

	protected void addCurrentPlayersCards() {
		buff.append(PrinterGame.CR).append("Your cards:").append(PrinterGame.CR);

		for (AssociatedCard ac : player.getCardHand().getAssociatedCards()) {
			addCard(ac);
		}
	}

	private void addCard(AssociatedCard ac) {
		Card card = ac.getCard();
		addCardType(ac);
		addCardText(card);
		buff.append(" for ");
		addCardIsPlayableStatus(ac);
		addCardNameDesc(card);

		buff.append(PrinterGame.CR).append(PrinterGame.CR);
	}

	private void addCardNameDesc(Card card) {
		buff.append(card.getName()).append(" (power:").append(card.getNo()).append(") - ")
				.append(card.getDescription()).append(' ');
	}

	private void addCardText(Card card) {
		if (card.getPattern().isPlayer() && !card.getPattern().isTargetCard()) {
			buff.append("-x");
		}
		if (card.getPattern().isPlayer() && card.getPattern().isTargetCard()) {
			buff.append("-x-y");
		}
	}

	private void addCardType(AssociatedCard ac) {
		buff.append("Type:");
		buff.append(ac.getType().toOrdinal());
	}

	private void addCardIsPlayableStatus(AssociatedCard ac) {
		if (!ac.getCard().isPlayable()) {
			buff.append("-NOT PLAYABLE- ");
		}
	}

	public String toString() {
		return buff.toString();
	}

}
