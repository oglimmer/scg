package de.oglimmer.scg.printer;

import de.oglimmer.scg.core.Game;
import de.oglimmer.scg.core.Player;

public class PrinterGameHtml extends PrinterGame {

	public PrinterGameHtml(Game game) {
		super(game);
	}

	@Override
	protected PrinterPlayer createPrinterPlayer(Player p) {
		return new PrinterPlayerHtml(p);
	}

	protected void addLastActions() {
		buff.append(CR);
		buff.append("Last actions: ").append(CR);
		buff.append("<ul>");
		buff.append(game.getCurrentPlayer().getMessages().getAllHtml());
		buff.append("</ul>");
	}
}
