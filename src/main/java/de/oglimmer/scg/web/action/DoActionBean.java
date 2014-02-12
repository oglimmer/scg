package de.oglimmer.scg.web.action;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import de.oglimmer.scg.core.Game;
import de.oglimmer.scg.core.Player;
import de.oglimmer.scg.printer.PrinterGamePlan;
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
		CommandString command = new CommandString();
		command.build();
		response = command.execute();
		return findResolution();
	}

	private Resolution findResolution() {
		Game game = GameManager.INSTANCE.getGame(gid);
		if (game != null) {
			Player player = game.getPlayer(pid);
			if (player.isDead()) {
				return getPlayerDeadView();
			} else {
				return getTurnResultView();
			}
		} else {
			return getGameEndedView();
		}
	}

	private Resolution getTurnResultView() {
		return new ForwardResolution("/WEB-INF/jsp/do.jsp");
	}

	private Resolution getPlayerDeadView() {
		return new ForwardResolution("/WEB-INF/jsp/dead.jsp");
	}

	private Resolution getGameEndedView() {
		return new ForwardResolution("/WEB-INF/jsp/gameover.jsp");
	}

	class CommandString {

		private StringBuilder commandString = new StringBuilder();

		String execute() {
			String response = new WebTurn(gid, pid, commandString.toString()).process();
			response = response.replace(PrinterGamePlan.CR, "<br/>");
			return response;
		}

		void build() {
			addPart1();
			if (hasPart2()) {
				addPart2();
				if (hasPart3()) {
					addPart3();
				}
			}
		}

		private void addPart1() {
			commandString.append(card);
		}

		private void addPart2() {
			commandString.append("-").append(player);
		}

		private boolean hasPart2() {
			return player != null && !player.isEmpty();
		}

		private void addPart3() {
			commandString.append("-").append(targetCard);
		}

		private boolean hasPart3() {
			return targetCard != null && !targetCard.isEmpty();
		}
	}

}
