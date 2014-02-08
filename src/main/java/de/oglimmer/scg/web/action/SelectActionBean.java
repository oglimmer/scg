package de.oglimmer.scg.web.action;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import de.oglimmer.scg.core.Game;
import de.oglimmer.scg.core.Player;
import de.oglimmer.scg.printer.PrinterGame;
import de.oglimmer.scg.printer.PrinterGameHtml;
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

			Player player = game.getPlayer(pid);
			if (player.isDead()) {
				return new ForwardResolution("/WEB-INF/jsp/dead.jsp");
			} else {
				if (pid.equals(game.getCurrentPlayer().getId())) {

					PrinterGame printGame = new PrinterGameHtml(game);
					printGame.printTable();
					response = printGame.toString();

					response = response.replace(PrinterGame.CR, "<br/>");

					return new ForwardResolution("/WEB-INF/jsp/do.jsp");
				} else {
					return new ForwardResolution("/WEB-INF/jsp/noTurn.jsp");
				}
			}
		} else {
			return new RedirectResolution(LandingActionBean.class);
		}
	}

}
