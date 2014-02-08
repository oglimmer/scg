package de.oglimmer.scg.core;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import de.oglimmer.scg.printer.PrinterDebug;
import de.oglimmer.scg.web.ScgProperties;

@Data
@Slf4j
public class Game implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<Player> players = new ArrayList<>();

	private CardStack stackHidden = new CardStack();
	private CardStack stackOpen = new CardStack();

	private Card spare;

	private transient Iterator<Player> playerIterator;
	private Player currentPlayer;

	private String id = Integer.toHexString((int) (Math.random() * 1000000));

	private boolean autoSave;
	private int turn = 1;

	private void readObject(ObjectInputStream ios) throws ClassNotFoundException, IOException {
		ios.defaultReadObject();
		playerIterator = players.iterator();
		while (playerIterator.next().getNo() != currentPlayer.getNo()) {
			// next() already used
		}
	}

	private void writeObject(ObjectOutputStream oos) throws IOException {
		oos.defaultWriteObject();
	}

	public Player getPlayer(int playerNo) {
		for (Player p : players) {
			if (p.getNo() == playerNo) {
				return p;
			}
		}
		return null;
	}

	public Player getPlayer(String playerId) {
		for (Player p : players) {
			if (p.getId().equals(playerId)) {
				return p;
			}
		}
		return null;
	}

	public Player addPlayer(String email) {
		Player newPlayer = new Player(players.size(), email, this);
		players.add(newPlayer);
		return newPlayer;
	}

	public void start() {
		stackHidden.add(Card.get(8));
		stackHidden.add(Card.get(7));
		stackHidden.add(Card.get(6));
		stackHidden.add(Card.get(5));
		stackHidden.add(Card.get(5));
		stackHidden.add(Card.get(4));
		stackHidden.add(Card.get(4));
		stackHidden.add(Card.get(3));
		stackHidden.add(Card.get(3));
		stackHidden.add(Card.get(2));
		stackHidden.add(Card.get(2));
		stackHidden.add(Card.get(1));
		stackHidden.add(Card.get(1));
		stackHidden.add(Card.get(1));
		stackHidden.add(Card.get(1));
		stackHidden.add(Card.get(1));
		stackHidden.shuffle();
		spare = stackHidden.takeTop();
		for (Player player : players) {
			player.getCardHand().addCard(stackHidden.takeTop(), Type.HAND);
		}
		Collections.shuffle(players);
		playerIterator = players.iterator();
		turn();
	}

	public void turn() {
		determineNextPlayer();
		currentPlayer.getCardHand().clearOpenCards();
		currentPlayer.getCardHand().addCard(stackHidden.takeTop(), Type.DRAWN);
		persist();
		turn++;
	}

	private void determineNextPlayer() {
		nextPlayer();
		while (currentPlayer.isDead()) {
			nextPlayer();
		}
	}

	private void nextPlayer() {
		if (!playerIterator.hasNext()) {
			playerIterator = players.iterator();
		}
		currentPlayer = playerIterator.next();
	}

	private void persist() {
		if (autoSave) {
			try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(getBackupFilename()))) {
				os.writeObject(this);
			} catch (IOException e) {
				log.error("Failed to persist game " + id, e);
			}
		}
	}

	public String getBackupFilename() {
		return ScgProperties.INSTANCE.getBackupDir() + "/" + id + ".scg";
	}

	public List<Player> getPlayersSorted() {
		List<Player> sortedList = new ArrayList<>(players);
		Collections.sort(sortedList, new Comparator<Player>() {
			@Override
			public int compare(Player o1, Player o2) {
				return Integer.compare(o1.getNo(), o2.getNo());
			}
		});
		return sortedList;
	}

	public boolean play(String[] inputString) throws GameEndException {
		int cardTypeNo = Integer.parseInt(inputString[0]);
		Integer targetPlayerNo = null;
		if (inputString.length >= 2) {
			targetPlayerNo = Integer.valueOf(inputString[1]);
		}
		Integer targetCardNo = null;
		if (inputString.length >= 3) {
			targetCardNo = Integer.valueOf(inputString[2]);
		}
		return play(cardTypeNo, targetPlayerNo, targetCardNo);
	}

	private boolean play(int cardTypeNo, Integer targetPlayerNo, Integer targetCardNo) throws GameEndException {
		boolean valid = false;
		Player player = getCurrentPlayer();

		AssociatedCard card = player.getCardHand().getCard(Type.fromOrdinal(cardTypeNo));
		if (card != null && card.getCard().isPlayable() && card.getCard().isTargetValid(targetPlayerNo)) {
			valid = true;
			card.setType(Type.IN_PLAY);
			if (card.getCard().play(targetPlayerNo, targetCardNo)) {
				player.getCardHand().removeCard(card.getCard(), true);
			}
			getCurrentPlayer().getCardHand().changeType(Type.DRAWN, Type.HAND);
			new WinCondition(this).checkWinner();
			turn();
		}
		return valid;
	}

	public String debugPrint() {
		PrinterDebug pd = new PrinterDebug(this);
		pd.debugPrint();
		return pd.toString();
	}

	public int getTurn() {
		return turn;
	}
}
