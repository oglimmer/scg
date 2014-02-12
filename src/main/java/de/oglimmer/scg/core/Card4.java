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
	public void play(Integer targetPlayerNo, Integer targetCardNo) {
		addMsg(getOwner());
		getOwner().getCardHand().changeType(this, Type.OPEN);
	}

	@Override
	public boolean isProtectsOwner() {
		return true;
	}

	@Override
	public boolean isEnduring() {
		return true;
	}

	private void addMsg(Player player) {
		String msgPlayer = "You have played " + getName() + ".";
		String msgOthers = "Player " + player.getDisplayName() + " played " + getName() + ".";
		Messages.addTo(msgPlayer, player);
		Messages.addNotTo(msgOthers, player);
	}

}
