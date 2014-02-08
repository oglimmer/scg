package de.oglimmer.scg.core;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

@Data
@EqualsAndHashCode(callSuper = true)
@Slf4j
public class Card6 extends TargetableCard {
	private static final long serialVersionUID = 1L;

	public Card6() {
		super(6);
	}

	@Override
	public ResultPattern getPattern() {
		return ResultPattern.PLAYER;
	}

	@Override
	public boolean playOnTarget(Player otherPlayer, Integer param2) {

		Card otherAc = otherPlayer.getCardHand().removeCard(Type.HAND, false);

		AssociatedCard myAc = getOwner().getCardHand().getOwnerOtherAssociatedCard(this);

		getOwner().getCardHand().removeCard(myAc.getCard(), false);

		otherPlayer.getCardHand().addCard(myAc.getCard(), Type.HAND);
		getOwner().getCardHand().addCard(otherAc, Type.HAND);

		addMsg(getOwner(), otherPlayer, myAc.getCard(), otherAc);

		log.debug("Card 6: Player {} got {}, player {} got {}", getOwner(), otherAc, otherAc, myAc);
		return true;
	}

	@Override
	public int getNo() {
		return 6;
	}

	@Override
	public boolean isPlayable() {
		return !getOwner().getCardHand().hasCard(7);
	}

	private void addMsg(Player player, Player otherPlayer, Card playersCard, Card otherPlayersCard) {
		Messages.addTo("You have played " + getName() + " against " + otherPlayer.getDisplayName() + ". You got "
				+ otherPlayersCard.getName() + " for " + playersCard.getName(), player);
		Messages.addTo("Player " + player.getDisplayName() + " played " + getName() + " against you. You gave away "
				+ otherPlayersCard.getName() + " and just got " + playersCard.getName(), otherPlayer);
		Messages.addNotTo(
				"Player " + player.getDisplayName() + " played " + getName() + " against "
						+ otherPlayer.getDisplayName() + ".", player, otherPlayer);
	}

}
