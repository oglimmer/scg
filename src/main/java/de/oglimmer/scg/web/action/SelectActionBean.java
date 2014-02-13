package de.oglimmer.scg.web.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
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
import de.oglimmer.scg.printer.PrinterPlayerPlan;
import de.oglimmer.scg.printer.PrinterPlayerPlanHtml;
import de.oglimmer.scg.web.GameManager;

@Data
@EqualsAndHashCode(callSuper = false)
public class SelectActionBean extends BaseAction {

	private String gid;
	private String pid;

	private Collection<PlayerWeb> players;
	private Collection<CardWeb> currentPlayersCards;

	private Map<String, Integer> usedCards;
	private Messages messages;

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
		Player player = game.getPlayer(pid);
		if (player.isDead()) {
			return getPlayerDeadResolution();
		} else {
			return showPlayer(player);
		}
	}

	private Resolution showPlayer(Player player) {
		buildResponse(player);
		if (isPlayersTurn(player)) {
			return getDisplayTurnResolution();
		} else {
			return getNoTurnResolution();
		}
	}

	private Resolution getNoTurnResolution() {
		return new ForwardResolution("/WEB-INF/jsp/noTurn.jsp");
	}

	private boolean isPlayersTurn(Player player) {
		return player.isCurrentPlayer();
	}

	private Resolution getDisplayTurnResolution() {
		return new ForwardResolution("/WEB-INF/jsp/select.jsp");
	}

	private Map<String, Integer> aggregateDataUsedCards(Game game) {
		Map<String, Integer> map = new HashMap<>();
		for (Card c : game.getStackOpen().getCards()) {
			Integer i = map.get(c.getName());
			if (i == null) {
				i = 0;
			}
			map.put(c.getName(), i + 1);
		}
		return map;
	}

	private void buildResponse(Player player) {
		Game game = player.getGame();
		messages = player.getMessages();

		undisclosedCards = game.getStackHidden().getCards().size();
		usedCards = aggregateDataUsedCards(game);

		currentPlayersCards = new ArrayList<>();
		for (AssociatedCard card : player.getCardHand().getAssociatedCards()) {
			currentPlayersCards.add(new CardWeb(card));
		}

		players = new ArrayList<>();
		for (Player pla : game.getPlayers()) {
			players.add(new PlayerWeb(pla));
		}
	}

	private Resolution getPlayerDeadResolution() {
		return new ForwardResolution("/WEB-INF/jsp/dead.jsp");
	}

	private Resolution getNoGameFoundResolution() {
		return new RedirectResolution(LandingActionBean.class);
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
