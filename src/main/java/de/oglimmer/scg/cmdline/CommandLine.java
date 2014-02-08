package de.oglimmer.scg.cmdline;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import lombok.extern.slf4j.Slf4j;
import de.oglimmer.scg.core.Game;
import de.oglimmer.scg.core.GameEndException;
import de.oglimmer.scg.core.Player;
import de.oglimmer.scg.printer.PrinterDebug;
import de.oglimmer.scg.printer.PrinterGame;

@Slf4j
public class CommandLine {

	public static void main(String[] args) {
		CommandLine cl = new CommandLine();
		cl.runGame();
	}

	private boolean running;

	private Game game;

	public CommandLine() {
		game = new Game();
		game.addPlayer("a@test.com");
		game.addPlayer("b@test.com");
		game.addPlayer("c@test.com");
		game.addPlayer("d@test.com");
		game.start();
	}

	private void runGame() {
		running = true;
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			while (running) {
				PrinterGame pg = new PrinterGame(game);
				pg.printTable();
				System.out.println(pg.toString() + "$");
				String commandLine = br.readLine();
				parse(commandLine);
			}
		} catch (IOException e) {
			log.error("Failed to read from console", e);
		} catch (GameEndException e) {
			System.out.println(e.getMessage());
		}
	}

	private void parse(String commandLine) throws GameEndException {
		// card-param
		String[] turnData = commandLine.split("-");
		if (turnData[0].equals("x")) {
			running = false;
		} else if (turnData[0].equals("d")) {
			System.out.println("---");
			PrinterDebug pd = new PrinterDebug(game);
			pd.debugPrint();
			System.out.println(pd.toString());
			System.out.println("---");
		} else {
			Player currentPlayer = game.getCurrentPlayer();
			if (game.play(turnData)) {
				System.out.println(currentPlayer.getMessages().getAll());
				System.out.println("---");
				System.out.println(game.getCurrentPlayer().getMessages().getAll());
			}
		}
	}
}
