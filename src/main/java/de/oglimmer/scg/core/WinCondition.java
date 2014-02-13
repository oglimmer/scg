package de.oglimmer.scg.core;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class WinCondition {

	private Game game;

	public void checkWinner() throws GameEndException {
		new WinnerLastManStanding().check();
		new CheckWinnerNoCards().check();
	}

	private void throwGameEndException(String winner) throws GameEndException {
		throw new GameEndException(winner);
	}

	class CheckWinnerNoCards {
		private int maxCardNo;
		private String winner;

		void check() throws GameEndException {
			if (checkWinCondition()) {
				findPlayerWithMaxCard();
				log.debug("Game {} ended no cards left. {}", game.getId(), winner);
				throwGameEndException(winner);
			}
		}

		private boolean checkWinCondition() {
			return game.getStackHidden().getCards().isEmpty();
		}

		private void findPlayerWithMaxCard() {
			for (Player p : game.getPlayers()) {
				processPlayer(p);
			}
		}

		private void processPlayer(Player player) {
			if (!player.isDead()) {
				int handCardNo = player.getCardHand().getCard(Type.HAND).getNo();
				if (handCardNo > maxCardNo) {
					newBestPlayer(player, handCardNo);
				} else if (handCardNo == maxCardNo) {
					addPlayerAsWinner(player);
				}
			}
		}

		private void addPlayerAsWinner(Player p) {
			winner += ", " + p.getEmail();
		}

		private void newBestPlayer(Player p, int handCardNo) {
			winner = p.getEmail();
			maxCardNo = handCardNo;
		}
	}

	class WinnerLastManStanding {
		private int playerAlive;
		private String aPlayerAlive;

		void check() throws GameEndException {
			checkPlayers();
			checkWinCondition();
		}

		private void checkPlayers() {
			for (Player player : game.getPlayers()) {
				checkPlayer(player);
			}
		}

		private void checkPlayer(Player p) {
			if (!p.isDead()) {
				aPlayerAlive = p.getEmail();
				playerAlive++;
			}
		}

		private void checkWinCondition() throws GameEndException {
			if (playerAlive < 2) {
				log.debug("Game {} ended player alive < 2. {}", game.getId(), aPlayerAlive);
				throwGameEndException(aPlayerAlive);
			}
		}
	}

}
