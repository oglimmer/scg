package de.oglimmer.scg.core;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class WinCondition {

	private Game game;

	public void checkWinner() throws GameEndException {
		new WinnerLastManStanding().check();
		new checkWinnerNoCards().check();
	}

	class checkWinnerNoCards {
		private int maxCardNo;
		private String winner;

		void check() throws GameEndException {
			if (checkWinCondition()) {
				findPlayerWithMaxCard();
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
				throwGameEndException(aPlayerAlive);
			}
		}
	}

	private void throwGameEndException(String winner) throws GameEndException {
		throw new GameEndException("Game over. Winner is : " + winner);
	}
}
