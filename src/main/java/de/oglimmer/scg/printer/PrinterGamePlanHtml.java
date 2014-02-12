package de.oglimmer.scg.printer;

import de.oglimmer.scg.core.Game;
import de.oglimmer.scg.core.Player;

public class PrinterGamePlanHtml extends PrinterGamePlan {

	public PrinterGamePlanHtml(Game game) {
		super(game);
	}

	@Override
	protected PrinterPlayerPlan createPrinterPlayer(Player p) {
		return new PrinterPlayerPlanHtml(p);
	}

	protected void addLastActions() {
		buff.append(CR);
		buff.append("Last actions: ").append(CR);
		buff.append("<ul>");
		buff.append(game.getTurn().getCurrentPlayer().getMessages().getAllHtml());
		buff.append("</ul>");
	}
}
