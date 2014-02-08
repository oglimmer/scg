package de.oglimmer.scg.generic;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import de.oglimmer.scg.core.GameEndException;

@RunWith(value = Parameterized.class)
public class Card2Test extends CardBaseTest {

	public Card2Test(int playerBCard) {
		super(playerBCard);
	}

	@Test
	public void testCard2() throws GameEndException {
		for (int cardNo = 1; cardNo <= 8; cardNo++) {
			ExpectedResult result = ExpectedResult.builder().playerAHand(cardNo).playerBHand(playerBCard)
					.playResult(true).build();
			new CardTest(2, cardNo, 1, result).test();
			new CardTest(cardNo, 2, 2, result).test();
		}
	}

}
