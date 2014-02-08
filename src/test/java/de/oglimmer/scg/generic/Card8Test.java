package de.oglimmer.scg.generic;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import de.oglimmer.scg.core.GameEndException;

@RunWith(value = Parameterized.class)
public class Card8Test extends CardBaseTest {

	public Card8Test(int playerBCard) {
		super(playerBCard);
	}

	@Test
	public void testCard8() throws GameEndException {
		ExpectedResult result = ExpectedResult.builder().playerADead(true).playerBHand(playerBCard).playResult(true)
				.build();
		for (int cardNo = 1; cardNo < 8; cardNo++) {
			new CardTest(8, cardNo, 1, result).test();
			new CardTest(cardNo, 8, 2, result).test();
		}
	}
}
