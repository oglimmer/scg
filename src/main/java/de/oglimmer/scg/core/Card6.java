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
	public void playOnTarget(Player targetPlayer, Integer targetCardNo) {
		new Swap(targetPlayer).swap();
	}

	@Override
	public boolean isPlayable() {
		return !getOwner().getCardHand().hasCard(7);
	}

	class Swap {

		private final Player targetPlayer;
		private final Card targetPlayersHandCard;
		private final Card playersOtherCard;

		public Swap(Player targetPlayer) {
			this.targetPlayer = targetPlayer;
			targetPlayersHandCard = getTargetPlayersHandCard();
			playersOtherCard = getPlayersOtherCard();
		}

		private void swap() {
			removeCards();
			addCards();
			addMsg();
			log.debug("Card 6: Player {} got {}, player {} got {}", getOwner(), targetPlayersHandCard,
					targetPlayersHandCard, playersOtherCard);
		}

		private void addCards() {
			targetPlayer.getCardHand().addCard(playersOtherCard, Type.HAND);
			getOwner().getCardHand().addCard(targetPlayersHandCard, Type.HAND);
		}

		private void removeCards() {
			getOwner().getCardHand().removeCard(playersOtherCard);
			targetPlayer.getCardHand().removeCard(targetPlayersHandCard);
		}

		private Card getPlayersOtherCard() {
			Card playersOtherCard = getOwner().getCardHand().getOwnerOtherCard(Card6.this);
			return playersOtherCard;
		}

		private Card getTargetPlayersHandCard() {
			return targetPlayer.getCardHand().getCard(Type.HAND);
		}

		private void addMsg() {
			String msgPlayer = String.format("You have played %s against %s. You got %s for %s.", getName(),
					targetPlayer.getDisplayName(), targetPlayersHandCard.getName(), playersOtherCard.getName());
			String msgTargetPlayer = String.format(
					"Player %s played %s against you. You gave away %s and just got %s.", getOwner().getDisplayName(),
					getName(), targetPlayersHandCard.getName(), playersOtherCard.getName());
			String msgOthers = String.format("Player %s played %s against %s.", getOwner().getDisplayName(), getName(),
					targetPlayer.getDisplayName());
			Messages.addTo(msgPlayer, getOwner());
			Messages.addTo(msgTargetPlayer, targetPlayer);
			Messages.addNotTo(msgOthers, getOwner(), targetPlayer);
		}
	}

}
