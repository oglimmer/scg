package de.oglimmer.scg.core;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

@Data
@EqualsAndHashCode(callSuper = true)
@Slf4j
public class Card1 extends TargetableCard {

	private static final long serialVersionUID = 1L;

	public Card1() {
		super(1);
	}

	@Override
	public ResultPattern getPattern() {
		return ResultPattern.PLAYER_TARGETCARD;
	}

	@Override
	public boolean playOnTarget(Player otherPlayer, Integer targetCard) {

		AssociatedCard otherAc = otherPlayer.getCardHand().getCard(Type.HAND);
		Card guessedCard = Card.get(targetCard);

		log.debug("Card1 played on {} who has {} you guessed {}", otherPlayer, otherAc, targetCard);

		if (targetCard == 1) {
			addMsgError(getOwner(), otherPlayer);
		} else if (guessedCard.getNo() == otherAc.getCard().getNo()) {
			addMsgTargetDead(getOwner(), otherPlayer, guessedCard);
			otherPlayer.killPlayer();
		} else {
			addMsgMiss(getOwner(), otherPlayer, guessedCard);
		}

		return true;
	}

	private void addMsgMiss(Player player, Player otherPlayer, Card guessedCard) {
		Messages.addTo("Your guess (" + guessedCard.getName() + ") was wrong.", player);
		Messages.addTo("The player " + player.getDisplayName() + " has tried to kill you with " + getName()
				+ ", but failed. He guessed " + guessedCard.getName() + ".", otherPlayer);
		Messages.addNotTo(
				"The player " + player.getDisplayName() + " has tried to kill the player "
						+ otherPlayer.getDisplayName() + " with " + getName() + ", but failed. The wrong guess was "
						+ guessedCard.getName() + ".", player, otherPlayer);
	}

	private void addMsgTargetDead(Player player, Player otherPlayer, Card guessedCard) {
		Messages.addTo("Your guess (" + guessedCard.getName() + ") was right. Player " + otherPlayer.getDisplayName()
				+ " dead.", player);
		Messages.addTo("The player " + player.getDisplayName() + " has killed you with " + getName() + ".", otherPlayer);
		Messages.addNotTo(
				"The player " + player.getDisplayName() + " has killed the player " + otherPlayer.getDisplayName()
						+ " with " + getName() + ". Guess " + guessedCard.getName() + " was correct.", player,
				otherPlayer);
	}

	private void addMsgError(Player player, Player otherPlayer) {
		Messages.addTo("Bad guess. " + getName() + " is not allowed.", player);
		Messages.addTo("The player " + player.getDisplayName() + " has tried to kill you with " + getName()
				+ ", but failed as he guessed " + getName(), otherPlayer);
		Messages.addNotTo(
				"The player " + player.getDisplayName() + " has tried to kill the player "
						+ otherPlayer.getDisplayName() + " with " + getName() + ", but failed as he guessed "
						+ getName() + ".", player, otherPlayer);
	}

}
