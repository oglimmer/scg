package de.oglimmer.scg.core;

import org.junit.Assert;
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

		Assert.assertEquals(true, removeAfterPlay);
		Assert.assertEquals(false, playerOwner.isDead());
		Assert.assertEquals(true, playerOther.isDead());
	}

	@Test
	public void doubleThreeLoss() {
		playerOwner.getCardHand().addCard(Card.get(3), Type.HAND);
		playerOwner.getCardHand().addCard(card, Type.DRAWN);
		playerOther.getCardHand().addCard(Card.get(5), Type.HAND);

		boolean removeAfterPlay = card.play(playerOther.getNo(), null);

		Assert.assertEquals(true, removeAfterPlay);
		Assert.assertEquals(true, playerOwner.isDead());
		Assert.assertEquals(false, playerOther.isDead());
	}

	@Test
	public void eightWin() {
		playerOwner.getCardHand().addCard(Card.get(8), Type.HAND);
		playerOwner.getCardHand().addCard(card, Type.DRAWN);
		playerOther.getCardHand().addCard(Card.get(1), Type.HAND);

		boolean removeAfterPlay = card.play(playerOther.getNo(), null);

		Assert.assertEquals(true, removeAfterPlay);
		Assert.assertEquals(false, playerOwner.isDead());
		Assert.assertEquals(true, playerOther.isDead());
	}

	@Test
	public void eightLoss() {
		playerOwner.getCardHand().addCard(Card.get(7), Type.HAND);
		playerOwner.getCardHand().addCard(card, Type.DRAWN);
		playerOther.getCardHand().addCard(Card.get(8), Type.HAND);

		boolean removeAfterPlay = card.play(playerOther.getNo(), null);

		Assert.assertEquals(true, removeAfterPlay);
		Assert.assertEquals(true, playerOwner.isDead());
		Assert.assertEquals(false, playerOther.isDead());
	}

	@Test
	public void sevenTie() {
		playerOwner.getCardHand().addCard(Card.get(7), Type.HAND);
		playerOwner.getCardHand().addCard(card, Type.DRAWN);
		playerOther.getCardHand().addCard(Card.get(7), Type.HAND);

		boolean removeAfterPlay = card.play(playerOther.getNo(), null);

		Assert.assertEquals(true, removeAfterPlay);
		Assert.assertEquals(false, playerOwner.isDead());
		Assert.assertEquals(false, playerOther.isDead());
	}

}
