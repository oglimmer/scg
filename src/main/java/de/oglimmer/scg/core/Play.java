package de.oglimmer.scg.core;

public class Play {

	private Turn turn;

	private int cardTypeNo;
	private Integer targetPlayerNo;
	private Integer targetCardNo;

	private Card cardToPlay;

	public Play(Turn turn, String[] inputString) {
		this.turn = turn;
		parseInputString(inputString);
		buildCardToPlay();
	}

	private void parseInputString(String[] inputString) {
		cardTypeNo = Integer.parseInt(inputString[0]);
		if (inputString.length >= 2) {
			targetPlayerNo = Integer.valueOf(inputString[1]);
		}
		if (inputString.length >= 3) {
			targetCardNo = Integer.valueOf(inputString[2]);
		}
	}

	public boolean play() throws GameEndException {
		if (isPlayValid()) {
			playValid();
			return true;
		}
		return false;
	}

	private void playValid() throws GameEndException {
		changePlayedCardToInPlay();
		playCard();
		if (!cardToPlay.isEnduring()) {
			movePlayedCardToOpenStack();
		}
		changeDrawnCardToInHand();
		checkWinCondition();
		nextTurn();
	}

	private void playCard() {
		cardToPlay.play(targetPlayerNo, targetCardNo);
	}

	private void nextTurn() {
		turn.nextTurn();
	}

	private void checkWinCondition() throws GameEndException {
		WinCondition winCond = new WinCondition(turn.getGame());
		winCond.checkWinner();
	}

	private void changeDrawnCardToInHand() {
		turn.getCurrentPlayer().getCardHand().changeType(Type.DRAWN, Type.HAND);
	}

	private void movePlayedCardToOpenStack() {
		turn.getCurrentPlayer().getCardHand().removeCardAndAddToOpenStack(cardToPlay);
	}

	private void changePlayedCardToInPlay() {
		turn.getCurrentPlayer().getCardHand().changeType(Type.fromOrdinal(cardTypeNo), Type.IN_PLAY);
	}

	private void buildCardToPlay() {
		cardToPlay = turn.getCurrentPlayer().getCardHand().getCard(Type.fromOrdinal(cardTypeNo));
	}

	private boolean isPlayValid() {
		return cardToPlay != null && cardToPlay.isPlayable() && cardToPlay.isTargetValid(targetPlayerNo);
	}
}