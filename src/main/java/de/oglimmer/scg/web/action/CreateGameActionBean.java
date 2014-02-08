package de.oglimmer.scg.web.action;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import de.oglimmer.scg.core.Game;
import de.oglimmer.scg.email.EmailSender;
import de.oglimmer.scg.web.GameManager;

@Data
@EqualsAndHashCode(callSuper = false)
public class CreateGameActionBean extends BaseAction {

	private String email1;
	private String email2;
	private String email3;
	private String email4;

	@DefaultHandler
	public Resolution show() {
		return new ForwardResolution("/WEB-INF/jsp/createGame.jsp");
	}

	public Resolution create() {
		Game game = GameManager.INSTANCE.addGame();
		game.setAutoSave(true);
		game.addPlayer(email1);
		game.addPlayer(email2);
		game.addPlayer(email3);
		game.addPlayer(email4);
		game.start();
		EmailSender.sendTurn(game.getCurrentPlayer(), "Your Turn", game);

		return new ForwardResolution("/WEB-INF/jsp/createGameDone.jsp");
	}

}
