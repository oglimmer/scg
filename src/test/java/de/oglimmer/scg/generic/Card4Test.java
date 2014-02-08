package de.oglimmer.scg.generic;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import de.oglimmer.scg.core.GameEndException;

@RunWith(value = Parameterized.class)
public class Card4Test extends CardBaseTest {

	public Card4Test(int playerBCard) {
		super(playerBCard);
	}

	@Test
	public void testCard4() throws GameEndException {
		for (int cardNo = 1; cardNo <= 8; cardNo++) {
			ExpectedResult result = ExpectedResult.builder().playerAHand(cardNo).playerBHand(playerBCard).playResult(true)
					.playerAOpen(4).build();
			new CardTest(4, cardNo, 1, result).test();
			new CardTest(cardNo, 4, 2, result).test();
		}
	}

}
