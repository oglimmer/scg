package de.oglimmer.scg.generic;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import de.oglimmer.scg.core.GameEndException;

@RunWith(value = Parameterized.class)
public class Card7Test extends CardBaseTest {

	public Card7Test(int playerBCard) {
		super(playerBCard);
	}

	@Test
	public void testCard7() throws GameEndException {
		for (int cardNo = 1; cardNo < 8; cardNo++) {
			ExpectedResult result = ExpectedResult.builder().playerAHand(cardNo).playerBHand(playerBCard)
					.playResult(true).build();
			new CardTest(7, cardNo, 1, result).test();
			new CardTest(cardNo, 7, 2, result).test();
		}
	}
}
