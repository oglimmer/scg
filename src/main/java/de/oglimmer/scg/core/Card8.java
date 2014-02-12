package de.oglimmer.scg.core;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Card8 extends Card {
	private static final long serialVersionUID = 1L;

	public Card8() {
		super(8);
	}

	@Override
	public void play(Integer targetPlayerNo, Integer targetCardNo) {
		discard(null);
	}

	@Override
	public void discard(Runnable onSuccess) {
		addMsg(getOwner());
		getOwner().killPlayer();
	}

	private void addMsg(Player player) {
		Messages.addTo("You have played " + getName() + " and therefore died.", player);
		Messages.addNotTo("Player " + player.getDisplayName() + " played " + getName() + " and therefore died.", player);
	}

}
