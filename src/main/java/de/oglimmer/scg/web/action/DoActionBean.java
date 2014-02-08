package de.oglimmer.scg.web.action;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import de.oglimmer.scg.core.Game;
import de.oglimmer.scg.core.Player;
import de.oglimmer.scg.printer.PrinterGame;
import de.oglimmer.scg.web.GameManager;
import de.oglimmer.scg.web.WebTurn;

@Data
@EqualsAndHashCode(callSuper = false)
@Slf4j
public class DoActionBean extends BaseAction {

	private String gid;

	private String pid;

	private String card;

	private String player;

	private String targetCard;

	private String response;

	@DefaultHandler
	public Resolution show() {

		log.debug("game:{}, player:{}, card:{}, targetplayer:{}, targetcard:{}", gid, pid, card, player, targetCard);

		String row = card;
		if (player != null && !player.isEmpty()) {
			row += "-" + player;
			if (targetCard != null && !targetCard.isEmpty()) {
				row += "-" + targetCard;
			}
		}

		response = new WebTurn(gid, pid, row).process();

		Game game = GameManager.INSTANCE.getGame(gid);
		if (game != null) {
			Player player = game.getPlayer(pid);
			if (player.isDead()) {
				return new ForwardResolution("/WEB-INF/jsp/dead.jsp");
			} else {
				response = response.replace(PrinterGame.CR, "<br/>");
				return new ForwardResolution("/WEB-INF/jsp/do.jsp");
			}
		} else {
			return new ForwardResolution("/WEB-INF/jsp/gameover.jsp");
		}
	}

}
