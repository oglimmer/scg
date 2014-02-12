package de.oglimmer.scg.core;

import lombok.AllArgsConstructor;
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
	public void playOnTarget(Player targetPlayer, Integer targetCardNo) {
		Guess guess = new Guess(targetCardNo, targetPlayer);
		guess.processOutcome();
	}

	class Guess {

		private final int targetCardNo;
		private final Card targetPlayersHandCard;
		private final Card guessedCard;
		private final Player targetPlayer;
		private final Message message;

		public Guess(int targetCardNo, Player targetPlayer) {
			this.targetCardNo = targetCardNo;
			this.targetPlayer = targetPlayer;
			this.targetPlayersHandCard = getTargetPlayersHandCard();
			this.guessedCard = Card.get(targetCardNo);
			this.message = new Message(getOwner(), targetPlayer, guessedCard);
		}

		void processOutcome() {
			log.debug("Card1 played on {} who has {} you guessed {}", targetPlayer, targetPlayersHandCard, targetCardNo);
			if (targetCardNo == 1) {
				handleInvalidGuess();
			} else if (guessedCard.getNo() == targetPlayersHandCard.getNo()) {
				handleHitGuess();
			} else {
				handleMissGuess();
			}
		}

		private void handleMissGuess() {
			message.addMsgMiss();
		}

		private void handleHitGuess() {
			message.addMsgTargetDead();
			targetPlayer.killPlayer();
		}

		private void handleInvalidGuess() {
			message.addMsgError();
		}

		private Card getTargetPlayersHandCard() {
			return targetPlayer.getCardHand().getCard(Type.HAND);
		}
	}

	@AllArgsConstructor
	class Message {

		private Player player;
		private Player targetPlayer;
		private Card guessedCard;

		private void addMessages(String msgPlayer, String msgTargetPlayer, String msgOthers) {
			Messages.addTo(msgPlayer, player);
			Messages.addTo(msgTargetPlayer, targetPlayer);
			Messages.addNotTo(msgOthers, player, targetPlayer);
		}

		void addMsgMiss() {
			String msgPlayer = String.format("Your guess (%s) was wrong.", guessedCard.getName());
			String msgTargetPlayer = String.format(
					"The player %s has tried to kill you with %s, but failed. He guessed %s.", player.getDisplayName(),
					getName(), guessedCard.getName());
			String msgOthers = String.format(
					"The player %s has tried to kill the player %s with %s, but failed. The wrong guess was "
							+ guessedCard.getName() + ".", player.getDisplayName(), targetPlayer.getDisplayName(),
					getName());
			addMessages(msgPlayer, msgTargetPlayer, msgOthers);
		}

		void addMsgTargetDead() {
			String msgPlayer = String.format("Your guess (%s) was right. Player " + targetPlayer.getDisplayName()
					+ " dead.", guessedCard.getName());
			String msgTargetPlayer = String.format("The player %s has killed you with %s.", player.getDisplayName(),
					getName());
			String msgOthers = String.format("The player %s has killed the player %s with %s. Guess %s was correct.",
					player.getDisplayName(), targetPlayer.getDisplayName(), getName(), guessedCard.getName());
			addMessages(msgPlayer, msgTargetPlayer, msgOthers);
		}

		void addMsgError() {
			String msgPlayer = String.format("Bad guess. %s is not allowed.", getName());
			String msgTargetPlayer = String.format(
					"The player %s has tried to kill you with %s, but failed as he guessed %s.",
					player.getDisplayName(), getName(), getName());
			String msgOthers = String.format(
					"The player %s has tried to kill the player %s with %s, but failed as he guessed %s.",
					player.getDisplayName(), targetPlayer.getDisplayName(), getName(), getName());
			addMessages(msgPlayer, msgTargetPlayer, msgOthers);
		}
	}
}
