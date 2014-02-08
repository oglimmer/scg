package de.oglimmer.scg.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Data
@ToString(exclude = "player")
@Slf4j
public class CardHand implements Serializable {

	private static final long serialVersionUID = 1L;

	private Collection<AssociatedCard> cards = new ArrayList<>();

	private Player player;

	public CardHand(Player player) {
		this.player = player;
	}

	public void remove(Card card) {
		cards.remove(card);
		card.setOwner(null);
	}

	public void addCard(Card card, Type type) {
		card.setOwner(player);
		cards.add(new AssociatedCard(card, type));
	}

	public void clearOpenCards() {
		for (Iterator<AssociatedCard> it = cards.iterator(); it.hasNext();) {
			AssociatedCard ac = it.next();
			if (ac.getType() == Type.OPEN) {
				it.remove();
				player.getGame().getStackOpen().add(ac.getCard());
			}
		}
	}

	public Collection<AssociatedCard> getAssociatedCardNotInPlay() {
		Collection<AssociatedCard> cardsNotInPlay = new ArrayList<>();
		for (AssociatedCard ac : cards) {
			if (ac.getType() != Type.IN_PLAY) {
				cardsNotInPlay.add(ac);
			}
		}
		return cardsNotInPlay;
	}

	public AssociatedCard getCard(Type type) {
		for (AssociatedCard ac : cards) {
			if (ac.getType() == type) {
				return ac;
			}
		}
		assert false;
		return null;
	}

	public AssociatedCard getCardNotInPlayAndNotOpen() {
		for (AssociatedCard ac : cards) {
			if (ac.getType() != Type.IN_PLAY && ac.getType() != Type.OPEN) {
				return ac;
			}
		}
		assert false;
		return null;
	}

	public void removeCard(Card card, boolean putOnOpenStack) {
		for (AssociatedCard ac : cards) {
			if (ac.getCard() == card) {
				cards.remove(ac);
				if (putOnOpenStack) {
					player.getGame().getStackOpen().add(ac.getCard());
				}
				break;
			}
		}
	}

	public Card removeCard(Type type, boolean putOnOpenStack) {
		for (AssociatedCard ac : cards) {
			if (ac.getType() == type) {
				cards.remove(ac);
				if (putOnOpenStack) {
					player.getGame().getStackOpen().add(ac.getCard());
				}
				return ac.getCard();
			}
		}
		log.warn("removed card not found type:{} ", type);
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

	public AssociatedCard getOwnerOtherAssociatedCard(Card card) {
		for (AssociatedCard ac : cards) {
			if (ac.getCard() != card) {
				return ac;
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

	public void clear() {
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

}
