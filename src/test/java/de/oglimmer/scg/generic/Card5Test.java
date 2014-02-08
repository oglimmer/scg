package de.oglimmer.scg.generic;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import de.oglimmer.scg.core.GameEndException;

@RunWith(value = Parameterized.class)
public class Card5Test extends CardBaseTest {

	private int targetPlayerNo;

	public Card5Test(int playerBCard) {
		super(playerBCard);
	}

	@Test
	public void testCard5() throws GameEndException {
		for (targetPlayerNo = 0; targetPlayerNo <= 1; targetPlayerNo++) {
			testForTargetPlayer();
		}
	}

	private void testForTargetPlayer() throws GameEndException {
		for (int cardNo = 1; cardNo <= 6; cardNo += (cardNo != 6 ? 1 : 2)) {
			testWithCard(cardNo);
		}
		testWithCard7();
	}

	private void testWithCard(int cardNo) throws GameEndException {
		ExpectedResult resultTrue = buildExpectedResult(cardNo);
		new CardTest(5, cardNo, 1, resultTrue).testPlayerNo(targetPlayerNo);
		new CardTest(cardNo, 5, 2, resultTrue).testPlayerNo(targetPlayerNo);
	}

	private ExpectedResult buildExpectedResult(int cardNo) {
		if (targetPlayerNo == 0) {
			return buildExpectedResultSelfTarget(cardNo);
		} else {
			return buildExpectedResultPlayerBTarget(cardNo);
		}
	}

	private ExpectedResult buildExpectedResultSelfTarget(int cardNo) {
		if (cardNo != 8) {
			return ExpectedResult.builder().playerAHand(1).playerBHand(playerBCard).playResult(true).build();
		} else {
			return ExpectedResult.builder().playerBHand(playerBCard).playerADead(true).playResult(true).build();
		}
	}

	private ExpectedResult buildExpectedResultPlayerBTarget(int cardNo) {
		if (playerBCard != 8) {
			return ExpectedResult.builder().playerAHand(cardNo).playerBHand(1).playResult(true).build();
		} else {
			return ExpectedResult.builder().playerAHand(cardNo).playerBDead(true).playResult(true).build();
		}
	}

	private void testWithCard7() throws GameEndException {
		ExpectedResult resultFalse = ExpectedResult.builder().playResult(false).build();
		new CardTest(5, 7, 1, resultFalse).test();
		new CardTest(7, 5, 2, resultFalse).test();
	}
}
