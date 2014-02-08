package de.oglimmer.scg.core;

import lombok.Getter;

public enum ResultPattern {

	CARD_ONLY(false, false), PLAYER(true, false), PLAYER_TARGETCARD(true, true);

	@Getter
	private boolean player;
	@Getter
	private boolean targetCard;

	private ResultPattern(boolean player, boolean targetCard) {
		this.player = player;
		this.targetCard = targetCard;
	}

}
