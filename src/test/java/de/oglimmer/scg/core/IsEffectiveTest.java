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

public class IsEffectiveTest {

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

		when(game.getPlayer(0)).thenReturn(playerOwner);
		when(game.getPlayer(1)).thenReturn(playerOther);
		when(game.getPlayers()).thenReturn(Arrays.asList(new Player[] { playerOwner, playerOther }));
	}

	// ----------------------------------------------------------------------------------------

	@Test
	public void checkIsEffectiveForOtherPlayer() {
		checkIsEffective(new Card1(), playerOther, true);
		checkIsEffective(new Card2(), playerOther, true);
		checkIsEffective(new Card3(), playerOther, true);
		checkIsEffective(new Card5(), playerOther, true);
		checkIsEffective(new Card6(), playerOther, true);
	}

	@Test
	public void checkIsEffectiveForOwner() {
		checkIsEffective(new Card1(), playerOwner, false);
		checkIsEffective(new Card2(), playerOwner, false);
		checkIsEffective(new Card3(), playerOwner, false);
		checkIsEffective(new Card5(), playerOwner, true);
		checkIsEffective(new Card6(), playerOwner, false);
	}

	// ----------------------------------------------------------------------------------------

	private void checkIsEffective(TargetableCard card, Player targetPlayer, boolean expected) {
		card.setOwner(playerOwner);

		boolean effective = card.isEffective(targetPlayer.getNo());

		assertThat(effective, is(expected));

		verifyNoMoreInteractions(ignoreStubs(game));
		verifyNoMoreInteractions(ignoreStubs(playerOwner));
		verifyNoMoreInteractions(ignoreStubs(playerOther));
	}

}
