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
	public void playOnTarget(Player targetPlayer, Integer unused) {
		Card targetPlayersHandCard = getTargetPlayersHandCard(targetPlayer);
		log.debug("Card2 played on {} who has {}", targetPlayer, targetPlayersHandCard);
		addMsg(getOwner(), targetPlayer, targetPlayersHandCard);
	}

	private Card getTargetPlayersHandCard(Player targetPlayer) {
		return targetPlayer.getCardHand().getCard(Type.HAND);
	}

	private void addMsg(Player player, Player targetPlayer, Card targetPlayersHandCard) {
		String msgPlayer = "The player " + targetPlayer.getDisplayName() + " has " + targetPlayersHandCard.getName()
				+ " in his hand.";
		String msgTargetPlayer = "The player " + player.getDisplayName() + " used " + getName() + " to see your hand.";
		String msgOthers = "The player " + player.getDisplayName() + " looked at player "
				+ targetPlayer.getDisplayName() + "'s hand with " + getName() + ".";
		Messages.addTo(msgPlayer, player);
		Messages.addTo(msgTargetPlayer, targetPlayer);
		Messages.addNotTo(msgOthers, player, targetPlayer);
	}

}
