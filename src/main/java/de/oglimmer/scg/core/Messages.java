package de.oglimmer.scg.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ToString(exclude = "game")
public class Messages implements Serializable {

	private static final long serialVersionUID = 1L;

	public static void addTo(String msg, Player player) {
		player.getMessages().add(msg, player.getGame().getTurn());
		log.debug("Added to player {} = {}", player.getNo(), msg);
	}

	public static void addNotTo(String msg, Player... excludedPlayers) {
		Game game = excludedPlayers[0].getGame();
		List<Player> excludedPlayersCol = Arrays.asList(excludedPlayers);
		for (Player player : game.getPlayers()) {
			if (!excludedPlayersCol.contains(player)) {
				player.getMessages().add(msg, player.getGame().getTurn());
				log.debug("Added to player {} = {}", player.getNo(), msg);
			}
		}
	}

	public static void addTo(String msg, Game game) {
		for (Player player : game.getPlayers()) {
			player.getMessages().add(msg, game.getTurn());
			log.debug("Added to player {} = {}", player.getNo(), msg);
		}
	}

	private List<Message> messages = new ArrayList<>(60);

	private Game game;

	public Messages(Game game) {
		this.game = game;
	}

	private void add(String msg, int round) {
		messages.add(new Message(msg, round));
	}

	public String getLastTurn() {
		StringBuilder buff = new StringBuilder();
		for (Message msg : messages) {
			if (game.getTurn() - 1 == msg.getTurn()) {
				buff.append(msg.getMsg()).append("\r\n");
			}
		}
		if (buff.length() == 0) {
			buff.append("Nothing happened.");
		}
		return buff.toString();
	}

	public String getAll() {
		StringBuilder buff = new StringBuilder();
		for (Message msg : messages) {
			buff.append("* ").append(msg.getMsg()).append("\r\n");
		}
		return buff.toString();
	}

	public String getAllHtml() {
		StringBuilder buff = new StringBuilder();
		for (Message msg : messages) {
			buff.append("<li>").append(msg.getMsg()).append("</li>");
		}
		return buff.toString();
	}

	@Data
	@AllArgsConstructor
	static class Message implements Serializable {
		private static final long serialVersionUID = 1L;
		private String msg;
		private int turn;
	}
}
