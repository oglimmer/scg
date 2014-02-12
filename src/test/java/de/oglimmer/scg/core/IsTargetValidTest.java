package de.oglimmer.scg.core;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.ignoreStubs;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class IsTargetValidTest {

	@Mock
	private Game game;
	@Mock
	private Player playerOwner;
	@Mock
	private Player playerOther;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		when(playerOwner.getNo()).thenReturn(0);
		when(playerOther.getNo()).thenReturn(1);
		when(playerOwner.getGame()).thenReturn(game);
		when(playerOther.getGame()).thenReturn(game);
		when(playerOwner.isTargetable()).thenReturn(true);
		when(playerOther.isTargetable()).thenReturn(true);
		when(playerOwner.isCurrentPlayer()).thenReturn(true);
		when(playerOther.isCurrentPlayer()).thenReturn(false);

		when(game.getTurn().getCurrentPlayer()).thenReturn(playerOwner);
		when(game.getPlayer(0)).thenReturn(playerOwner);
		when(game.getPlayer(1)).thenReturn(playerOther);
		when(game.getPlayers()).thenReturn(Arrays.asList(new Player[] { playerOwner, playerOther }));
	}

	// ----------------------------------------------------------------------------------------

	@Test
	public void checkIsTargetValidForOtherPlayer() {
		checkIsTargetValid(new Card1(), playerOther, true);
		checkIsTargetValid(new Card2(), playerOther, true);
		checkIsTargetValid(new Card3(), playerOther, true);
		checkIsTargetValid(new Card4(), playerOther, true);
		checkIsTargetValid(new Card5(), playerOther, true);
		checkIsTargetValid(new Card6(), playerOther, true);
		checkIsTargetValid(new Card7(), playerOther, true);
		checkIsTargetValid(new Card8(), playerOther, true);
	}

	@Test
	public void checkIsTargetValidForOwner() {
		checkIsTargetValid(new Card1(), playerOwner, false);
		checkIsTargetValid(new Card2(), playerOwner, false);
		checkIsTargetValid(new Card3(), playerOwner, false);
		checkIsTargetValid(new Card4(), playerOwner, true);
		checkIsTargetValid(new Card5(), playerOwner, true);
		checkIsTargetValid(new Card6(), playerOwner, false);
		checkIsTargetValid(new Card7(), playerOwner, true);
		checkIsTargetValid(new Card8(), playerOwner, true);
	}

	@Test
	public void checkIsTargetValidForOwnerWhenNoOther() {
		when(playerOther.isTargetable()).thenReturn(false);
		checkIsTargetValid(new Card1(), playerOwner, true);
		checkIsTargetValid(new Card2(), playerOwner, true);
		checkIsTargetValid(new Card3(), playerOwner, true);
		checkIsTargetValid(new Card4(), playerOwner, true);
		checkIsTargetValid(new Card5(), playerOwner, true);
		checkIsTargetValid(new Card6(), playerOwner, true);
		checkIsTargetValid(new Card7(), playerOwner, true);
		checkIsTargetValid(new Card8(), playerOwner, true);
	}

	// ----------------------------------------------------------------------------------------

	private void checkIsTargetValid(Card card, Player targetPlayer, boolean expected) {
		card.setOwner(playerOwner);

		boolean targetValid = card.isTargetValid(targetPlayer.getNo());

		assertThat(targetValid, is(expected));

		verifyNoMoreInteractions(ignoreStubs(game));
		verifyNoMoreInteractions(ignoreStubs(playerOwner));
		verifyNoMoreInteractions(ignoreStubs(playerOther));
	}

}
