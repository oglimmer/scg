package de.oglimmer.scg.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

@Data
@EqualsAndHashCode(callSuper = true)
@Slf4j
public class Card3 extends TargetableCard {
	private static final long serialVersionUID = 1L;

	public Card3() {
		super(3);
	}

	@Override
	public ResultPattern getPattern() {
		return ResultPattern.PLAYER;
	}

	@Override
	public boolean playOnTarget(Player otherPlayer, Integer param2) {

		AssociatedCard otherAc = otherPlayer.getCardHand().getCard(Type.HAND);
		AssociatedCard myOtherAc = getOwner().getCardHand().getOwnerOtherAssociatedCard(this);

		log.debug("Card3 played from {} with {} on {} with {}", getOwner(), otherPlayer, myOtherAc, otherAc);

		Card3MessageFacade msg = new Card3MessageFacade(getOwner(), otherPlayer, myOtherAc.getCard(), otherAc.getCard());

		if (myOtherAc.getCard().getNo() > otherAc.getCard().getNo()) {
			msg.addMsgTargetKilled();
			otherPlayer.killPlayer();
		} else if (myOtherAc.getCard().getNo() < otherAc.getCard().getNo()) {
			msg.addMsgInitiatorKilled();
			getOwner().killPlayer();
		} else {
			msg.addMsgTie();
		}

		return true;
	}

	enum Recipient {
		INITIATOR, TARGET, NEUTRALS
	}

	class Card3MessageFacade {

		private Player initiator;
		private Player target;

		private Card3Message msgInit;
		private Card3Message msgTarget;
		private Card3Message msgNeut;

		public Card3MessageFacade(Player initiator, Player target, Card initiatorsCard, Card targetsCard) {
			this.initiator = initiator;
			this.target = target;

			msgInit = getMessage(Recipient.INITIATOR, initiatorsCard, targetsCard);
			msgTarget = getMessage(Recipient.TARGET, initiatorsCard, targetsCard);
			msgNeut = getMessage(Recipient.NEUTRALS, initiatorsCard, targetsCard);
		}

		void addMsgTargetKilled() {
			Messages.addTo(msgInit.addWhoDidWhat() + " " + msgInit.addTargetGotKilled(), initiator);
			Messages.addTo(msgTarget.addWhoDidWhat() + " " + msgTarget.addTargetGotKilled(), target);
			Messages.addNotTo(msgNeut.addWhoDidWhat() + " " + msgNeut.addTargetGotKilled(), initiator, target);
		}

		void addMsgInitiatorKilled() {
			Messages.addTo(msgInit.addWhoDidWhat() + " " + msgInit.addInitiatorGotKilled(), initiator);
			Messages.addTo(msgTarget.addWhoDidWhat() + " " + msgTarget.addInitiatorGotKilled(), target);
			Messages.addNotTo(msgNeut.addWhoDidWhat() + " " + msgNeut.addInitiatorGotKilled(), initiator, target);
		}

		void addMsgTie() {
			Messages.addTo(msgInit.addWhoDidWhat() + " " + msgInit.addTie(), initiator);
			Messages.addTo(msgTarget.addWhoDidWhat() + " " + msgTarget.addTie(), target);
			Messages.addNotTo(msgNeut.addWhoDidWhat() + " " + msgNeut.addTie(), initiator, target);
		}

		private Card3Message getMessage(Recipient rec, Card initiatorsCard, Card targetsCard) {
			switch (rec) {
			case INITIATOR:
				return new InitiatorCard3Message(initiator, target, initiatorsCard, targetsCard);
			case TARGET:
				return new TargetCard3Message(initiator, target, initiatorsCard, targetsCard);
			case NEUTRALS:
				return new NeutralCard3Message(initiator, target, initiatorsCard, targetsCard);
			default:
				assert false;
				return null;
			}
		}
	}

	@AllArgsConstructor
	abstract class Card3Message {
		protected Player initiator;
		protected Player target;
		protected Card initiatorsCard;
		protected Card targetsCard;

		protected abstract String addWhoDidWhat();

		protected abstract String addTargetGotKilled();

		protected abstract String addInitiatorGotKilled();

		protected abstract String addTie();
	}

	class InitiatorCard3Message extends Card3Message {

		public InitiatorCard3Message(Player initiator, Player target, Card initiatorsCard, Card targetsCard) {
			super(initiator, target, initiatorsCard, targetsCard);
		}

		protected String addWhoDidWhat() {
			return String.format("You played %s against %s.", getName(), target.getDisplayName());
		}

		protected String addTargetGotKilled() {
			return String.format("You won as your %s beat his %s.", initiatorsCard.getName(), targetsCard.getName());
		}

		protected String addInitiatorGotKilled() {
			return String.format("You died as your %s lost against his %s.", initiatorsCard.getName(),
					targetsCard.getName());
		}

		protected String addTie() {
			return String.format("Nobody died as you both have %s.", initiatorsCard.getName());
		}
	}

	class TargetCard3Message extends Card3Message {

		public TargetCard3Message(Player initiator, Player target, Card initiatorsCard, Card targetsCard) {
			super(initiator, target, initiatorsCard, targetsCard);
		}

		protected String addWhoDidWhat() {
			return String.format("%s played %s against you", initiator.getDisplayName(), getName());
		}

		protected String addTargetGotKilled() {
			return String.format("You died as his %s beat your %s.", initiatorsCard.getName(), targetsCard.getName());
		}

		protected String addInitiatorGotKilled() {
			return String.format("You won as his %s lost against your %s.", initiatorsCard.getName(),
					targetsCard.getName());
		}

		protected String addTie() {
			return String.format("Nobody died as you both have %s.", initiatorsCard.getName());
		}
	}

	class NeutralCard3Message extends Card3Message {

		public NeutralCard3Message(Player initiator, Player target, Card initiatorsCard, Card targetsCard) {
			super(initiator, target, initiatorsCard, targetsCard);
		}

		protected String addWhoDidWhat() {
			return String.format("%s played %s against %s.", initiator.getDisplayName(), getName(),
					target.getDisplayName());
		}

		protected String addTargetGotKilled() {
			return String.format("%s died.", target.getDisplayName());
		}

		protected String addInitiatorGotKilled() {
			return String.format("%s died.", initiator.getDisplayName());
		}

		protected String addTie() {
			return String.format("Tie, nobody died.");
		}
	}
}
