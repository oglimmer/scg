package de.oglimmer.scg.cmdline;

import lombok.Getter;
import de.oglimmer.scg.core.Game;
import de.oglimmer.scg.core.GameEndException;
import de.oglimmer.scg.core.Player;
import de.oglimmer.scg.printer.PrinterDebugGame;

public class CommandLine {

	public static void main(String[] args) {
		CommandLine cl = new CommandLine();
		cl.processLoop.loop();
	}

	@Getter
	private Game game;

	private ProcessLoop processLoop = new ProcessLoop(this);

	public CommandLine() {
		createGame();
	}

	private void createGame() {
		game = new Game();
		game.addPlayer("a");
		game.addPlayer("b");
		game.addPlayer("c");
		game.addPlayer("d");
		game.start();
	}

	public void parse(String commandLine) throws GameEndException {
		String[] turnData = commandLine.split("-");
		if (Character.valueOf('x').equals(turnData[0])) {
			executeQuit();
		} else if (Character.valueOf('d').equals(turnData[0])) {
			executeDebugOut();
		} else {
			processUserTurn(turnData);
		}
	}

	private void processUserTurn(String[] turnData) throws GameEndException {
		if (exeucteUserTurn(turnData)) {
			outputUserTurn();
		}
	}

	private void outputUserTurn() {
		Player currentPlayer = game.getTurn().getCurrentPlayer();
		System.out.println(currentPlayer.getMessages().getAll());
		System.out.println("---");
		System.out.println(currentPlayer.getMessages().getAll());
	}

	private boolean exeucteUserTurn(String[] turnData) throws GameEndException {
		return game.getTurn().getPlay(turnData).play();
	}

	private void executeDebugOut() {
		System.out.println("---");
		PrinterDebugGame pd = new PrinterDebugGame(game);
		pd.debugPrint();
		System.out.println(pd.toString());
		System.out.println("---");
	}

	private void executeQuit() {
		processLoop.setRunning(false);
	}
}
