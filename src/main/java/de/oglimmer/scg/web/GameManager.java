package de.oglimmer.scg.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import de.oglimmer.scg.core.Game;

@Slf4j
public enum GameManager {
	INSTANCE;

	private Map<String, Game> games = new HashMap<>();

	public Game addGame() {
		Game game = new Game();
		games.put(game.getId(), game);
		return game;
	}

	public Game getGame(String id) {
		return games.get(id);
	}

	public void removeGameMemoryAndFile(Game game) {
		games.remove(game.getId());
		new File(game.getBackupFilename()).delete();
	}

	public Collection<Game> getAllGames() {
		return games.values();
	}

	public void loadGame(File file) {
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
			Game game = (Game) ois.readObject();
			games.put(game.getId(), game);
		} catch (IOException | ClassNotFoundException e) {
			log.error("Failed to load game " + file.getAbsolutePath(), e);
		}

	}

	public void restoreSavedGames() {
		File backupDir = new File(ScgProperties.INSTANCE.getBackupDir());
		File[] listFiles = backupDir.listFiles(new _FilenameFilter());
		for (File file : listFiles) {
			loadGame(file);
		}
	}

	class _FilenameFilter implements FilenameFilter {
		@Override
		public boolean accept(File dir, String name) {
			return name.endsWith(".scg");
		}
	};
}
