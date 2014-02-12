package de.oglimmer.scg.core;

import java.io.Serializable;

import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import de.oglimmer.scg.email.EmailSender;

@Data
@ToString(exclude = { "game", "messages" })
@Slf4j
public class Player implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id = Integer.toHexString((int) (Math.random() * 1000000));
	private int no;
	private String email;
	private Game game;
	private Messages messages;

	private CardHand cardHand = new CardHand(this);

	public Player(int no, String email, Game game) {
		this.no = no;
		this.email = email;
		this.game = game;
		messages = new Messages(game);
	}

	public String getDisplayName() {
		return email;
	}

	public boolean isDead() {
		return cardHand.isEmpty();
	}

	protected void killPlayer() {
		cardHand.moveAllToOpenStack();
		EmailSender.sendPlain(this, "Your are dead", messages.getAll());
		log.debug("{} is dead", this);
	}

	public boolean isTargetable() {
		boolean protectedOrDead = isProtectedByCard() || isDead();
		return !protectedOrDead;
	}

	private boolean isProtectedByCard() {
		for (AssociatedCard card : cardHand.getAssociatedCards()) {
			if (card.getCard().isProtectsOwner() && card.getType() == Type.OPEN) {
				return true;
			}
		}
		return false;
	}

	public boolean isCurrentPlayer() {
		return game.getTurn().getCurrentPlayer() == this;
	}
}
