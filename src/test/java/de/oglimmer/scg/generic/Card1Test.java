package de.oglimmer.scg.generic;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import de.oglimmer.scg.core.GameEndException;

@RunWith(value = Parameterized.class)
public class Card1Test extends CardBaseTest {

	private int card1TargetNo;

	public Card1Test(int playerBCard) {
		super(playerBCard);
	}

	@Test
	public void testCard1() throws GameEndException {
		for (card1TargetNo = 2; card1TargetNo <= 8; card1TargetNo++) {
			testTargetNo();
		}
	}

	private void testTargetNo() throws GameEndException {
		for (int cardNo = 1; cardNo <= 8; cardNo++) {
			testCardNo(cardNo);
		}
	}

	private void testCardNo(int cardNo) throws GameEndException {
		ExpectedResult result = buildExpectedResult(cardNo);
		new CardTest(1, cardNo, 1, result).testTargetNo(card1TargetNo);
		new CardTest(cardNo, 1, 2, result).testTargetNo(card1TargetNo);
	}

	private ExpectedResult buildExpectedResult(int cardNo) {
		if (playerBCard == card1TargetNo) {
			return ExpectedResult.builder().playerAHand(cardNo).playerBDead(true).playResult(true).build();
		} else {
			return ExpectedResult.builder().playerAHand(cardNo).playerBHand(playerBCard).playResult(true).build();
		}
	}

}
