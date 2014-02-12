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
	public void play(Integer targetPlayerNo, Integer targetCardNo) {
		addMsg(getOwner());
	}

	private void addMsg(Player player) {
		Messages.addTo("You have played " + getName() + ".", player);
		Messages.addNotTo("Player " + player.getDisplayName() + " played " + getName() + ".", player);
	}
}
