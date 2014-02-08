package de.oglimmer.scg.core;

import java.util.Iterator;

import org.junit.Assert;
import org.junit.Test;

public class Card5Test {

	@Test
	public void twiceCard5() throws GameEndException {
		Game game = new Game();
		Player a = game.addPlayer("a");
		Player b = game.addPlayer("b");

		game.getStackHidden().add(Card.get(2));
		game.getStackHidden().add(Card.get(3));

		b.getCardHand().addCard(Card.get(1), Type.HAND);

		a.getCardHand().addCard(Card.get(5), Type.HAND);
		a.getCardHand().addCard(Card.get(5), Type.DRAWN);

		Iterator<Player> it = game.getPlayers().iterator();
		game.setPlayerIterator(it);
		game.setCurrentPlayer(it.next());

		boolean playResult = game.play(new String[] { "2", "1" });

		Assert.assertEquals(true, playResult);
		Assert.assertEquals(1, a.getCardHand().getCards().size());
		AssociatedCard ac = a.getCardHand().getCards().iterator().next();
		Assert.assertEquals(Card.get(5), ac.getCard());
		Assert.assertEquals(Type.HAND, ac.getType());
	}

}
