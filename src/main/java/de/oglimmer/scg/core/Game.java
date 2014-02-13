package de.oglimmer.scg.core;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import de.oglimmer.scg.printer.PrinterDebugGame;
import de.oglimmer.scg.web.ScgProperties;

@Data
@Slf4j
public class Game implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id = Integer.toHexString((int) (Math.random() * 1000000));

	private List<Player> players = new ArrayList<>();

	private CardStack stackHidden = new CardStack();
	private CardStack stackOpen = new CardStack();
	private Card spare;

	private Turn turn = new Turn(this);

	private boolean autoSave;

	private void readObject(ObjectInputStream ios) throws ClassNotFoundException, IOException {
		ios.defaultReadObject();
		turn.reStart();
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
		stackHidden.init();
		spare = stackHidden.takeTop();
		for (Player player : players) {
			player.getCardHand().addCard(stackHidden.takeTop(), Type.HAND);
		}
		Collections.shuffle(players);
		turn.start();
		turn.nextTurn();
	}

	public void persist() {
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
		Collections.sort(sortedList, new PlayerComparator());
		return sortedList;
	}

	public String debugPrint() {
		PrinterDebugGame pd = new PrinterDebugGame(this);
		pd.debugPrint();
		return pd.toString();
	}

	class PlayerComparator implements Comparator<Player> {
		@Override
		public int compare(Player o1, Player o2) {
			return Integer.compare(o1.getNo(), o2.getNo());
		}
	}

}
