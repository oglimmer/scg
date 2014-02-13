package de.oglimmer.scg.web.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import de.oglimmer.scg.core.AssociatedCard;
import de.oglimmer.scg.core.Card;
import de.oglimmer.scg.core.CardStack;
import de.oglimmer.scg.core.Game;
import de.oglimmer.scg.core.Messages;
import de.oglimmer.scg.core.Player;
import de.oglimmer.scg.printer.PrinterGamePlan;
import de.oglimmer.scg.printer.PrinterPlayerPlan;
import de.oglimmer.scg.printer.PrinterPlayerPlanHtml;
import de.oglimmer.scg.web.GameManager;

@Data
@EqualsAndHashCode(callSuper = false)
public class SelectActionBean extends BaseAction {

	private String gid;
	private String pid;

	private Collection<PlayerWeb> players;
	private Collection<CardWeb> callingPlayersCards;

	private Messages callingPlayersMessages;

	private Map<String, Integer> usedCards;
	private int undisclosedCards;

	public Card getCard(int no) {
		return Card.get(no);
	}

	public int getCardCount(int no) {
		return CardStack.NUMBER_OF_CARDS[no - 1];
	}

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
		Player callingPlayer = game.getPlayer(pid);
		return showPlayer(callingPlayer);
	}

	private Resolution showPlayer(Player callingPlayer) {
		buildResponse(callingPlayer);
		if (isPlayersTurn(callingPlayer)) {
			return getDisplayTurnResolution();
		} else {
			return getNoTurnResolution();
		}
	}

	private boolean isPlayersTurn(Player callingPlayer) {
		return callingPlayer.isCurrentPlayer();
	}

	private Resolution getNoTurnResolution() {
		return new ForwardResolution("/WEB-INF/jsp/noTurn.jsp");
	}

	private Resolution getDisplayTurnResolution() {
		return new ForwardResolution("/WEB-INF/jsp/select.jsp");
	}

	private Resolution getNoGameFoundResolution() {
		return new RedirectResolution(LandingActionBean.class);
	}

	private void buildResponse(Player callingPlayer) {
		Game game = callingPlayer.getGame();
		buildMessages(callingPlayer);
		buildCardStacks(game);
		buildCallingPlayersCards(callingPlayer);
		buildPlayerOverview(game);
	}

	private void buildPlayerOverview(Game game) {
		players = new ArrayList<>();
		for (Player player : game.getPlayers()) {
			players.add(new PlayerWeb(player));
		}
	}

	private void buildCallingPlayersCards(Player callingPlayer) {
		callingPlayersCards = new ArrayList<>();
		for (AssociatedCard card : callingPlayer.getCardHand().getAssociatedCards()) {
			callingPlayersCards.add(new CardWeb(card));
		}
	}

	private void buildCardStacks(Game game) {
		undisclosedCards = game.getStackHidden().getCards().size();
		PrinterGamePlan pgp = new PrinterGamePlan(game);
		usedCards = pgp.aggregateDataUsedCards();
	}

	private void buildMessages(Player callingPlayer) {
		callingPlayersMessages = callingPlayer.getMessages();
	}

	@AllArgsConstructor
	public class CardWeb {

		private AssociatedCard card;

		public int getNo() {
			return card.getCard().getNo();
		}

		public String getForm() {
			PrinterPlayerPlan ppp = new PrinterPlayerPlanHtml(card.getCard().getOwner());
			ppp.addCard(card);
			return ppp.toString();
		}
	}

	@AllArgsConstructor
	public class PlayerWeb {

		private Player player;

		public String getDisplayName() {
			Game game = GameManager.INSTANCE.getGame(gid);
			Player callingPlayer = game.getPlayer(pid);
			if (player == callingPlayer) {
				return "You are";
			}
			return player.getDisplayName() + " is";
		}

		public String getStatus() {
			PrinterPlayerPlan ppp = new PrinterPlayerPlan(player);
			ppp.addStatus();
			return ppp.toString();
		}

		public String getTurn() {
			return player.isCurrentPlayer() ? "(Turn owner)" : "";
		}

	}
}
