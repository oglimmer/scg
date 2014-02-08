package de.oglimmer.scg.generic;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import de.oglimmer.scg.core.GameEndException;

@RunWith(value = Parameterized.class)
public class Card6Test extends CardBaseTest {

	public Card6Test(int playerBCard) {
		super(playerBCard);
	}

	@Test
	public void testCard6() throws GameEndException {
		for (int cardNo = 1; cardNo <= 8; cardNo += (cardNo != 6 ? 1 : 2)) {
			ExpectedResult resultTrue = ExpectedResult.builder().playerAHand(playerBCard).playerBHand(cardNo)
					.playResult(true).build();
			new CardTest(6, cardNo, 1, resultTrue).test();
			new CardTest(cardNo, 6, 2, resultTrue).test();
		}
		ExpectedResult resultFalse = ExpectedResult.builder().playResult(false).build();
		new CardTest(6, 7, 1, resultFalse).test();
		new CardTest(7, 6, 2, resultFalse).test();
	}
}
