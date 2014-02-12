package de.oglimmer.scg.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(exclude = "player")
public class CardHand implements Serializable {

	private static final long serialVersionUID = 1L;

	private Collection<AssociatedCard> cards = new ArrayList<>();

	private Player player;

	public CardHand(Player player) {
		this.player = player;
	}

	public void removeCardAndAddToOpenStack(Card card) {
		Card ac = removeCard(card);
		if (ac != null) {
			putCardToOpenStack(ac);
		}
	}

	private void putCardToOpenStack(Card card) {
		player.getGame().getStackOpen().add(card);
	}

	public Card removeCard(Card card) {
		for (AssociatedCard ac : cards) {
			if (ac.getCard() == card) {
				cards.remove(ac);
				return ac.getCard();
			}
		}
		return null;
	}

	public void addCard(Card card, Type type) {
		card.setOwner(player);
		cards.add(new AssociatedCard(card, type));
	}

	public void putOpenCardsToOpenStack() {
		for (Iterator<AssociatedCard> it = cards.iterator(); it.hasNext();) {
			AssociatedCard ac = it.next();
			if (ac.getType() == Type.OPEN) {
				it.remove();
				putCardToOpenStack(ac.getCard());
			}
		}
	}

	private Collection<Card> getCardNotInPlay() {
		Collection<Card> cardsNotInPlay = new ArrayList<>();
		for (AssociatedCard ac : cards) {
			if (ac.getType() != Type.IN_PLAY) {
				cardsNotInPlay.add(ac.getCard());
			}
		}
		return cardsNotInPlay;
	}

	public Card getCard(Type type) {
		for (AssociatedCard ac : cards) {
			if (ac.getType() == type) {
				return ac.getCard();
			}
		}
		assert false;
		return null;
	}

	public Card getCardNotInPlayAndNotOpen() {
		for (AssociatedCard ac : cards) {
			if (ac.getType() != Type.IN_PLAY && ac.getType() != Type.OPEN) {
				return ac.getCard();
			}
		}
		assert false;
		return null;
	}

	public void changeType(Card card, Type type) {
		for (AssociatedCard ac : cards) {
			if (ac.getCard() == card) {
				ac.setType(type);
				break;
			}
		}
	}

	public void changeType(Type from, Type to) {
		for (AssociatedCard ac : cards) {
			if (ac.getType() == from) {
				ac.setType(to);
			}
		}
	}

	public boolean isEmpty() {
		return cards.isEmpty();
	}

	public Card getOwnerOtherCard(Card card) {
		for (AssociatedCard ac : cards) {
			if (ac.getCard() != card) {
				return ac.getCard();
			}
		}
		assert false;
		return null;
	}

	public boolean hasCard(int no) {
		for (AssociatedCard ac : cards) {
			if (ac.getCard().getNo() == no) {
				return true;
			}
		}
		return false;
	}

	private void clear() {
		cards.clear();
	}

	public boolean hasOpenCard(int no) {
		for (AssociatedCard ac : cards) {
			if (ac.getType() == Type.OPEN && ac.getCard().getNo() == no) {
				return true;
			}
		}
		return false;
	}

	public Collection<AssociatedCard> getAssociatedCards() {
		return cards;
	}

	public void moveAllToOpenStack() {
		for (Card ac : getCardNotInPlay()) {
			player.getGame().getStackOpen().add(ac);
		}
		clear();
	}
}
