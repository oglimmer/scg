package de.oglimmer.scg.core;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

@Data
@EqualsAndHashCode(callSuper = true)
@Slf4j
public class Card2 extends TargetableCard {
	private static final long serialVersionUID = 1L;

	public Card2() {
		super(2);
	}

	@Override
	public ResultPattern getPattern() {
		return ResultPattern.PLAYER;
	}

	@Override
	public boolean playOnTarget(Player otherPlayer, Integer param2) {

		AssociatedCard otherAc = otherPlayer.getCardHand().getCard(Type.HAND);

		log.debug("Card2 played on {} who has {}", otherPlayer, otherAc);
		addMsg(getOwner(), otherPlayer, otherAc);

		return true;
	}

	private void addMsg(Player player, Player otherPlayer, AssociatedCard otherAc) {
		Messages.addTo("The player " + otherPlayer.getDisplayName() + " has " + otherAc.getCard().getName()
				+ " in his hand.", player);
		Messages.addTo("The player " + player.getDisplayName() + " used " + getName() + " to see your hand.",
				otherPlayer);
		Messages.addNotTo("The player " + player.getDisplayName() + " looked at player " + otherPlayer.getDisplayName()
				+ "'s hand with " + getName() + ".", player, otherPlayer);
	}

}
