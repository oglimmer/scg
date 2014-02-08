package de.oglimmer.scg.core;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Card4 extends Card {
	private static final long serialVersionUID = 1L;

	public Card4() {
		super(4);
	}

	@Override
	public boolean playImpl(Integer param1, Integer param2) {
		addMsg(getOwner());
		getOwner().getCardHand().changeType(this, Type.OPEN);
		return false;
	}

	private void addMsg(Player player) {
		Messages.addTo("You have played " + getName() + ".", player);
		Messages.addNotTo("Player " + player.getDisplayName() + " played " + getName() + ".", player);
	}

	@Override
	public int getNo() {
		return 4;
	}
}
