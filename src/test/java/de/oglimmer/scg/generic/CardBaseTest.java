package de.oglimmer.scg.generic;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import lombok.extern.slf4j.Slf4j;

import org.junit.runners.Parameterized.Parameters;

import de.oglimmer.scg.core.Card;
import de.oglimmer.scg.core.Game;
import de.oglimmer.scg.core.GameEndException;
import de.oglimmer.scg.core.Player;
import de.oglimmer.scg.core.Type;

@Slf4j
public class CardBaseTest {

	@Parameters
	public static Collection<Object[]> createParameters() {
		Object[][] data = new Object[][] { { 1 }, { 2 }, { 3 }, { 4 }, { 5 }, { 6 }, { 7 }, { 8 } };
		return Arrays.asList(data);
	}

	protected int playerBCard;

	public CardBaseTest(int playerBCard) {
		log.info("playerBCard={}", playerBCard);
		this.playerBCard = playerBCard;
	}

	class CardTest {

		private Game game;
		private Player playerA;
		private Player playerB;
		private Player playerC;

		private boolean playResult;
		private ExpectedResult expectedResult;

		private int cardA;
		private int cardB;
		private int play;

		CardTest(int cardA, int cardB, int play, ExpectedResult expectedResult) {
			this.cardA = cardA;
			this.cardB = cardB;
			this.play = play;
			this.expectedResult = expectedResult;
		}

		void test() throws GameEndException {
			test(1, 0);
		}

		void testPlayerNo(int testPlayerNo) throws GameEndException {
			test(testPlayerNo, 0);
		}

		void testTargetNo(int card1TargetNo) throws GameEndException {
			test(1, card1TargetNo);
		}

		private void test(int targetPlayerNo, int card1TargetNo) throws GameEndException {
			prepare();
			play(targetPlayerNo, card1TargetNo);
			validate();
		}

		private void prepare() {
			game = new Game();
			playerA = game.addPlayer("a");
			playerB = game.addPlayer("b");
			playerC = game.addPlayer("c");

			game.getStackHidden().add(Card.get(1));
			game.getStackHidden().add(Card.get(1));

			playerB.getCardHand().addCard(Card.get(playerBCard), Type.HAND);
			playerC.getCardHand().addCard(Card.get(1), Type.HAND);

			playerA.getCardHand().addCard(Card.get(cardA), Type.HAND);
			playerA.getCardHand().addCard(Card.get(cardB), Type.DRAWN);

			Iterator<Player> it = game.getPlayers().iterator();
			game.setPlayerIterator(it);
			game.setCurrentPlayer(it.next());
		}

		private void play(int targetPlayerNo, int card1TargetNo) throws GameEndException {
			playResult = game.play(new String[] { Integer.toString(play), Integer.toString(targetPlayerNo),
					Integer.toString(card1TargetNo) });
		}

		private void validate() {
			validateResult();
			if (playResult) {
				validteDetails();
			}
		}

		private void validateResult() {
			assertThat(expectedResult.toString(), playResult, is(expectedResult.isPlayResult()));
		}

		private void validteDetails() {
			assertThat(expectedResult.toString(), playerA.isDead(), is(expectedResult.isPlayerADead()));
			assertThat(expectedResult.toString(), playerB.isDead(), is(expectedResult.isPlayerBDead()));
			validatePlayerA();
			validatePlayerB();
		}

		private void validatePlayerA() {
			if (!playerA.isDead()) {
				validatePlayer(playerA);
				if (expectedResult.getPlayerAOpen() != null) {
					Card cardOpen = playerA.getCardHand().getCard(Type.OPEN).getCard();
					assertThat(expectedResult.toString(), cardOpen.getNo(), is(expectedResult.getPlayerAOpen()));
				}
			}
		}

		private void validatePlayerB() {
			if (!playerB.isDead()) {
				validatePlayer(playerB);
			}
		}

		private void validatePlayer(Player player) {
			Card cardHand = player.getCardHand().getCard(Type.HAND).getCard();
			assertThat(expectedResult.toString(), cardHand.getNo(), is(getHandForPlayer(player)));
		}

		private int getHandForPlayer(Player player) {
			if (player == playerA) {
				return expectedResult.getPlayerAHand();
			} else {
				return expectedResult.getPlayerBHand();
			}
		}

	}
}
