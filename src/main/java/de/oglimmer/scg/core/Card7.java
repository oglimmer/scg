package de.oglimmer.scg.core;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Card7 extends Card {
	private static final long serialVersionUID = 1L;

	public Card7() {
		super(7);
	}

	@Override
	public boolean playImpl(Integer param1, Integer param2) {
		addMsg(getOwner());
		return true;
	}

	@Override
	public int getNo() {
		return 7;
	}

	private void addMsg(Player player) {
		Messages.addTo("You have played " + getName() + ".", player);
		Messages.addNotTo("Player " + player.getDisplayName() + " played " + getName() + ".", player);
	}
}
