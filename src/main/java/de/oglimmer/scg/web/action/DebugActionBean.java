package de.oglimmer.scg.web.action;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.sourceforge.stripes.action.Before;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import de.oglimmer.scg.core.Game;
import de.oglimmer.scg.email.EmailSender;
import de.oglimmer.scg.web.GameManager;

@Data
@EqualsAndHashCode(callSuper = false)
public class DebugActionBean extends BaseAction {

	private GameManager gameManager;

	private String gameId;

	@Before
	public void load() {
		gameManager = GameManager.INSTANCE;
	}

	public Resolution delete() {
		gameManager.removeGameMemoryAndFile(gameManager.getGame(gameId));
		return new ForwardResolution("/WEB-INF/jsp/debug.jsp");
	}

	public Resolution resendEmail() {
		Game game = gameManager.getGame(gameId);
		EmailSender.sendTurn(game.getTurn().getCurrentPlayer(), "Your Turn", game);
		return new ForwardResolution("/WEB-INF/jsp/debug.jsp");
	}

	@DefaultHandler
	public Resolution show() {
		return new ForwardResolution("/WEB-INF/jsp/debug.jsp");
	}

}
