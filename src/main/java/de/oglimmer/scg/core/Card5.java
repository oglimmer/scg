package de.oglimmer.scg.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

@Data
@EqualsAndHashCode(callSuper = true)
@Slf4j
public class Card5 extends TargetableCard {
	private static final long serialVersionUID = 1L;

	public Card5() {
		super(5);
	}

	@Override
	public ResultPattern getPattern() {
		return ResultPattern.PLAYER;
	}

	@Override
	public void playOnTarget(Player targetPlayer, Integer targetCardNo) {
		Card targetPlayersCardToRemove = getTargetPlayersCardToRemove(targetPlayer);
		targetPlayersCardToRemove.discard(new OnDiscardSucceded(getOwner(), targetPlayer, targetPlayersCardToRemove));
		addMsg(getOwner(), targetPlayer, targetPlayersCardToRemove);
	}

	private Card getTargetPlayersCardToRemove(Player targetPlayer) {
		return targetPlayer.getCardHand().getCardNotInPlayAndNotOpen();
	}

	@Override
	public boolean isEffective(Integer param1) {
		return true;
	}

	@Override
	public boolean isTargetValid(Integer targetPlayerNo) {
		return getOwner().getGame().getPlayer(targetPlayerNo).isTargetable();
	}

	@Override
	public boolean isPlayable() {
		return !getOwner().getCardHand().hasCard(7);
	}

	private void addMsg(Player player, final Player otherPlayer, Card dropped) {
		String msgPlayer = "You have played " + getName() + " against " + otherPlayer.getDisplayName()
				+ ". He dropped " + dropped.getName();
		String msgOthers = "Player " + player.getDisplayName() + " played " + getName() + " against "
				+ otherPlayer.getDisplayName() + ", so he lost " + dropped.getName();
		Messages.addTo(msgPlayer, player);
		// No message to otherPlayer at this point
		Messages.addNotTo(msgOthers, player, otherPlayer);
	}

	@AllArgsConstructor
	class OnDiscardSucceded implements Runnable {

		private Player originPlayer;
		private Player targetPlayer;
		private Card toRemoveCard;

		@Override
		public void run() {
			Card card = drawCard();
			targetPlayer.getCardHand().addCard(card, Type.HAND);
			log.debug("Card 5: removed {} and got {}", toRemoveCard, card);
			addMsg(card);
		}

		private void addMsg(Card newCard) {
			String msgTargetPlayer = String.format("Player %s played %s against you. You lost %s and got %s.",
					originPlayer.getDisplayName(), getName(), toRemoveCard.getName(), newCard.getName());
			Messages.addTo(msgTargetPlayer, targetPlayer);
		}

		private Card drawCard() {
			if (getOwner().getGame().getStackHidden().getCards().isEmpty()) {
				return getOwner().getGame().getSpare();
			} else {
				return getOwner().getGame().getStackHidden().takeTop();
			}
		}
	}
}
