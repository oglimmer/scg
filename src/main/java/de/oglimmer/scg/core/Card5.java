package de.oglimmer.scg.core;

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
	public boolean playOnTarget(final Player otherPlayer, Integer param2) {

		final AssociatedCard toRemove = otherPlayer.getCardHand().getCardNotInPlayAndNotOpen();
		toRemove.getCard().discard(new OnDiscardSucceded(getOwner(), otherPlayer, toRemove));

		addMsg(getOwner(), otherPlayer, toRemove.getCard());

		return true;
	}

	@Override
	public boolean isEffective(Integer param1) {
		return true;
	}

	@Override
	public boolean isTargetValid(Integer targetPlayerNo) {
		return getOwner().getGame().getPlayer(targetPlayerNo).isTargetable();
	}

	private void addMsg(Player player, final Player otherPlayer, Card dropped) {
		Messages.addTo("You have played " + getName() + " against " + otherPlayer.getDisplayName() + ". He dropped "
				+ dropped.getName(), player);

		Messages.addNotTo(
				"Player " + player.getDisplayName() + " played " + getName() + " against "
						+ otherPlayer.getDisplayName() + ", so he lost " + dropped.getName(), player, otherPlayer);
	}

	@Override
	public int getNo() {
		return 5;
	}

	@Override
	public boolean isPlayable() {
		return !getOwner().getCardHand().hasCard(7);
	}

	class OnDiscardSucceded implements Runnable {

		private Player player;
		private Player otherPlayer;
		private AssociatedCard toRemove;

		public OnDiscardSucceded(Player player, Player otherPlayer, AssociatedCard toRemove) {
			this.player = player;
			this.otherPlayer = otherPlayer;
			this.toRemove = toRemove;
		}

		@Override
		public void run() {
			Card card;
			if (getOwner().getGame().getStackHidden().getCards().isEmpty()) {
				card = getOwner().getGame().getSpare();
			} else {
				card = getOwner().getGame().getStackHidden().takeTop();
			}

			otherPlayer.getCardHand().addCard(card, Type.HAND);

			log.debug("Card 5: removed {} and got {}", toRemove, card);

			Messages.addTo("Player " + player.getDisplayName() + " played " + getName() + " against you. You lost "
					+ toRemove.getCard().getName() + " and got "
					+ otherPlayer.getCardHand().getCard(Type.HAND).getCard().getName(), otherPlayer);
		}
	}
}
