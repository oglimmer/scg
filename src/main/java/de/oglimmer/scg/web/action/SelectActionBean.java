package de.oglimmer.scg.web.action;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import de.oglimmer.scg.core.Game;
import de.oglimmer.scg.core.Player;
import de.oglimmer.scg.printer.PrinterGamePlan;
import de.oglimmer.scg.printer.PrinterGamePlanHtml;
import de.oglimmer.scg.web.GameManager;

@Data
@EqualsAndHashCode(callSuper = false)
public class SelectActionBean extends BaseAction {

	private String gid;

	private String pid;

	private String response;

	@DefaultHandler
	public Resolution show() {
		Game game = GameManager.INSTANCE.getGame(gid);
		if (game != null) {
			return showGame(game);
		} else {
			return getNoGameFoundResolution();
		}
	}

	private Resolution showGame(Game game) {
		Player player = game.getPlayer(pid);
		if (player.isDead()) {
			return getPlayerDeadResolution();
		} else {
			return showPlayer(game);
		}
	}

	private Resolution showPlayer(Game game) {
		if (isPlayersTurn(game)) {
			buildResponse(game);
			return getDisplayTurnResolution();
		} else {
			return getNoTurnResolution();
		}
	}

	private Resolution getNoTurnResolution() {
		return new ForwardResolution("/WEB-INF/jsp/noTurn.jsp");
	}

	private boolean isPlayersTurn(Game game) {
		return pid.equals(game.getTurn().getCurrentPlayer().getId());
	}

	private Resolution getDisplayTurnResolution() {
		return new ForwardResolution("/WEB-INF/jsp/do.jsp");
	}

	private void buildResponse(Game game) {
		PrinterGamePlan printGame = new PrinterGamePlanHtml(game);
		printGame.printTable();
		response = printGame.toString();
		response = response.replace(PrinterGamePlan.CR, "<br/>");
	}

	private Resolution getPlayerDeadResolution() {
		return new ForwardResolution("/WEB-INF/jsp/dead.jsp");
	}

	private Resolution getNoGameFoundResolution() {
		return new RedirectResolution(LandingActionBean.class);
	}

}
