package de.oglimmer.scg.core;

import static de.oglimmer.scg.core.ScgMatcher.isAlive;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import java.util.Iterator;

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

		assertThat(playResult, is(true));
		assertThat(a, isAlive());
		assertThat(b, isAlive());

		assertThat(a.getCardHand().getCards(), hasSize(1));
		AssociatedCard ac = a.getCardHand().getCards().iterator().next();
		assertThat(ac.getCard(), is(Card.get(5)));
		assertThat(ac.getType(), is(Type.HAND));
	}

}
