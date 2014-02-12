package de.oglimmer.scg.cmdline;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import de.oglimmer.scg.core.GameEndException;
import de.oglimmer.scg.printer.PrinterGamePlan;

@Slf4j
public class ProcessLoop {

	private BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

	@Getter
	@Setter
	private boolean running;

	private CommandLine commandLine;

	public ProcessLoop(CommandLine commandLine) {
		this.commandLine = commandLine;
	}

	public void loop() {
		try {
			runProcessingLoop();
		} catch (IOException e) {
			handleIOException(e);
		} catch (GameEndException e) {
			processEndOfGame(e);
		}
	}

	private void processEndOfGame(GameEndException e) {
		System.out.println(e.getMessage());
	}

	private void handleIOException(IOException e) {
		log.error("Failed to read from console", e);
	}

	private void runProcessingLoop() throws IOException, GameEndException {
		while (running) {
			LoopTurn lt = new LoopTurn();
			lt.turn();
		}
	}

	class LoopTurn {
		private PrinterGamePlan pg = new PrinterGamePlan(commandLine.getGame());

		public void turn() throws IOException, GameEndException {
			printTable();
			processUserInput();
		}

		private void printTable() {
			pg.printTable();
			System.out.println(pg.toString() + "$");
		}

		private void processUserInput() throws GameEndException, IOException {
			commandLine.parse(userInput.readLine());
		}
	}

}
