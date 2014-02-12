package de.oglimmer.scg.core;

import static de.oglimmer.scg.core.ScgMatcher.isAlive;
import static de.oglimmer.scg.core.ScgMatcher.isDead;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Before;
import org.junit.Test;

public class Card3Test {

	private Game game;
	private Player playerOwner;
	private Player playerOther;
	private Card card = Card.get(3);

	@Before
	public void setUp() throws Exception {
		game = new Game();
		playerOwner = game.addPlayer("a");
		playerOther = game.addPlayer("b");
	}

	@Test
	public void doubleThreeWin() {
		playerOwner.getCardHand().addCard(Card.get(3), Type.HAND);
		playerOwner.getCardHand().addCard(card, Type.DRAWN);
		playerOther.getCardHand().addCard(Card.get(1), Type.HAND);

		boolean removeAfterPlay = card.play(playerOther.getNo(), null);

		assertThat(removeAfterPlay, is(true));
		assertThat(playerOwner, isAlive());
		assertThat(playerOther, isDead());
	}

	@Test
	public void doubleThreeLoss() {
		playerOwner.getCardHand().addCard(Card.get(3), Type.HAND);
		playerOwner.getCardHand().addCard(card, Type.DRAWN);
		playerOther.getCardHand().addCard(Card.get(5), Type.HAND);

		boolean removeAfterPlay = card.play(playerOther.getNo(), null);

		assertThat(removeAfterPlay, is(true));
		assertThat(playerOwner, isDead());
		assertThat(playerOther, isAlive());
	}

	@Test
	public void eightWin() {
		playerOwner.getCardHand().addCard(Card.get(8), Type.HAND);
		playerOwner.getCardHand().addCard(card, Type.DRAWN);
		playerOther.getCardHand().addCard(Card.get(1), Type.HAND);

		boolean removeAfterPlay = card.play(playerOther.getNo(), null);

		assertThat(removeAfterPlay, is(true));
		assertThat(playerOwner, isAlive());
		assertThat(playerOther, isDead());
	}

	@Test
	public void eightLoss() {
		playerOwner.getCardHand().addCard(Card.get(7), Type.HAND);
		playerOwner.getCardHand().addCard(card, Type.DRAWN);
		playerOther.getCardHand().addCard(Card.get(8), Type.HAND);

		boolean removeAfterPlay = card.play(playerOther.getNo(), null);

		assertThat(removeAfterPlay, is(true));
		assertThat(playerOwner, isDead());
		assertThat(playerOther, isAlive());
	}

	@Test
	public void sevenTie() {
		playerOwner.getCardHand().addCard(Card.get(7), Type.HAND);
		playerOwner.getCardHand().addCard(card, Type.DRAWN);
		playerOther.getCardHand().addCard(Card.get(7), Type.HAND);

		boolean removeAfterPlay = card.play(playerOther.getNo(), null);

		assertThat(removeAfterPlay, is(true));
		assertThat(playerOwner, isAlive());
		assertThat(playerOther, isAlive());
	}

}
