package de.oglimmer.scg.core;

import static org.mockito.Mockito.ignoreStubs;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class IsPlayableTest {

	@Mock
	private Game game;
	@Mock
	private Player playerOwner;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

		when(playerOwner.getCardHand()).thenReturn(new CardHand(playerOwner));
		when(game.getPlayer(0)).thenReturn(playerOwner);
		when(game.getPlayers()).thenReturn(Arrays.asList(new Player[] { playerOwner }));
	}

	// ----------------------------------------------------------------------------------------

	@Test
	public void checkAllPlayableCombinations() {
		checkHandDrawnCombi(1, 1, true, true);
		checkHandDrawnCombi(1, 2, true, true);
		checkHandDrawnCombi(1, 3, true, true);
		checkHandDrawnCombi(1, 4, true, true);
		checkHandDrawnCombi(1, 5, true, true);
		checkHandDrawnCombi(1, 6, true, true);
		checkHandDrawnCombi(1, 7, true, true);
		checkHandDrawnCombi(1, 8, true, true);

		checkHandDrawnCombi(2, 2, true, true);
		checkHandDrawnCombi(2, 3, true, true);
		checkHandDrawnCombi(2, 4, true, true);
		checkHandDrawnCombi(2, 5, true, true);
		checkHandDrawnCombi(2, 6, true, true);
		checkHandDrawnCombi(2, 7, true, true);
		checkHandDrawnCombi(2, 8, true, true);

		checkHandDrawnCombi(3, 3, true, true);
		checkHandDrawnCombi(3, 4, true, true);
		checkHandDrawnCombi(3, 5, true, true);
		checkHandDrawnCombi(3, 6, true, true);
		checkHandDrawnCombi(3, 7, true, true);
		checkHandDrawnCombi(3, 8, true, true);

		checkHandDrawnCombi(4, 4, true, true);
		checkHandDrawnCombi(4, 5, true, true);
		checkHandDrawnCombi(4, 6, true, true);
		checkHandDrawnCombi(4, 7, true, true);
		checkHandDrawnCombi(4, 8, true, true);

		checkHandDrawnCombi(5, 5, true, true);
		checkHandDrawnCombi(5, 6, true, true);
		checkHandDrawnCombi(5, 7, false, true);
		checkHandDrawnCombi(5, 8, true, true);

		checkHandDrawnCombi(6, 6, true, true);
		checkHandDrawnCombi(6, 7, false, true);
		checkHandDrawnCombi(6, 8, true, true);

		checkHandDrawnCombi(7, 7, true, true);
		checkHandDrawnCombi(7, 8, true, true);

		checkHandDrawnCombi(8, 8, true, true);
	}

	// ----------------------------------------------------------------------------------------

	private void checkHandDrawnCombi(int card1, int card2, boolean expected1, boolean expected2) {
		Collection<AssociatedCard> assocCardsCol = new ArrayList<>();
		assocCardsCol.add(new AssociatedCard(Card.get(card1), Type.HAND));
		assocCardsCol.add(new AssociatedCard(Card.get(card2), Type.DRAWN));
		playerOwner.getCardHand().setCards(assocCardsCol);

		checkIsPlayable(Card.get(card1), playerOwner, expected1);
		checkIsPlayable(Card.get(card2), playerOwner, expected2);
	}

	private void checkIsPlayable(Card card, Player targetPlayer, boolean expected) {
		card.setOwner(playerOwner);

		boolean effective = card.isPlayable();

		Assert.assertEquals(expected, effective);
		verifyNoMoreInteractions(ignoreStubs(game));
		verifyNoMoreInteractions(ignoreStubs(playerOwner));
	}

}
