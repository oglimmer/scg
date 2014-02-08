package de.oglimmer.scg.generic;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import de.oglimmer.scg.core.GameEndException;
import de.oglimmer.scg.generic.ExpectedResult.ExpectedResultBuilder;

@RunWith(value = Parameterized.class)
public class Card3Test extends CardBaseTest {

	public Card3Test(int playerBCard) {
		super(playerBCard);
	}

	@Test
	public void testCard3() throws GameEndException {
		for (int cardNo = 1; cardNo <= 8; cardNo++) {
			ExpectedResult result = buildExpectedResult(cardNo);
			new CardTest(3, cardNo, 1, result).test();
			new CardTest(cardNo, 3, 2, result).test();
		}
	}

	private ExpectedResult buildExpectedResult(int i) {
		ExpectedResultBuilder builder = ExpectedResult.builder().playResult(true);
		if (i < playerBCard) {
			builder.playerADead(true).playerBHand(playerBCard);
		} else if (i > playerBCard) {
			builder.playerAHand(i).playerBDead(true);
		} else {
			builder.playerAHand(i).playerBHand(playerBCard);
		}
		return builder.build();
	}

}
