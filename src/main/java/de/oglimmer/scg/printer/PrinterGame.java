package de.oglimmer.scg.printer;

import java.util.HashMap;
import java.util.Map;

import de.oglimmer.scg.core.Card;
import de.oglimmer.scg.core.Game;
import de.oglimmer.scg.core.Player;

public class PrinterGame {

	public static final String CR = "\r\n";

	protected StringBuilder buff = new StringBuilder(1024);

	protected Game game;

	public PrinterGame(Game game) {
		this.game = game;
	}

	public void printTable() {
		addPlayers();
		addCurrentPlayersCards();
		printStacksInfo();
		addLastActions();
	}

	protected void addLastActions() {
		buff.append(CR);
		buff.append("Last actions: ").append(CR);
		buff.append(game.getCurrentPlayer().getMessages().getAll());
	}

	protected PrinterPlayer createPrinterPlayer(Player p) {
		return new PrinterPlayer(p);
	}

	private void addCurrentPlayersCards() {
		PrinterPlayer pp = createPrinterPlayer(game.getCurrentPlayer());
		pp.addCurrentPlayersCards();
		buff.append(pp.toString());
	}

	private void printStacksInfo() {
		buff.append(CR);
		buff.append("Stacks: ").append(CR);
		buff.append("Undisclosed cards:").append(game.getStackHidden().getCards().size()).append(CR);
		addUsedCards();
		buff.append(CR);
	}

	private void addUsedCards() {
		Map<String, Integer> map = aggregateDataUsedCards();
		addAggregatedDataUsedCards(map);

	}

	private void addAggregatedDataUsedCards(Map<String, Integer> map) {
		buff.append("Used cards:");
		boolean first = true;
		for (String key : map.keySet()) {
			first = addAggregatedDataUsedCards(map, first, key);
		}
		if (first) {
			buff.append("-");
		}
	}

	private boolean addAggregatedDataUsedCards(Map<String, Integer> map, boolean first, String key) {
		if (!first) {
			buff.append(";");
		} else {
			first = false;
		}
		buff.append(key).append("(").append(map.get(key)).append("x)");
		return first;
	}

	private Map<String, Integer> aggregateDataUsedCards() {
		Map<String, Integer> map = new HashMap<>();
		for (Card c : game.getStackOpen().getCards()) {
			Integer i = map.get(c.getName());
			if (i == null) {
				i = 0;
			}
			map.put(c.getName(), i + 1);
		}
		return map;
	}

	private void addPlayers() {
		buff.append("Players:").append(CR);
		for (Player p : game.getPlayersSorted()) {
			addPlayer(p);
		}
	}

	private void addPlayer(Player p) {
		PrinterPlayer pp = createPrinterPlayer(p);
		pp.addPlayer();
		buff.append(pp.toString());
	}

	@Override
	public String toString() {
		return buff.toString();
	}

}
