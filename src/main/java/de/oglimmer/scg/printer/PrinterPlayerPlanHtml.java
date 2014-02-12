package de.oglimmer.scg.printer;

import java.util.ArrayList;
import java.util.Collection;

import lombok.Value;
import de.oglimmer.scg.core.AssociatedCard;
import de.oglimmer.scg.core.Card;
import de.oglimmer.scg.core.Player;
import de.oglimmer.scg.core.Type;
import de.oglimmer.scg.web.ScgProperties;

public class PrinterPlayerPlanHtml extends PrinterPlayerPlan {

	public PrinterPlayerPlanHtml(Player player) {
		super(player);
	}

	@Override
	public void addPlayer() {
		if (!player.isCurrentPlayer()) {
			buff.append(player.getEmail()).append(" is ");
		} else {
			buff.append("You are ");
		}
		addOtherPlayer();
		buff.append(PrinterGamePlan.CR);
	}

	@Override
	public void addCurrentPlayersCards() {
		buff.append(PrinterGamePlan.CR).append("Your cards:");
		buff.append("<table><tr>");
		for (AssociatedCard ac : player.getGame().getTurn().getCurrentPlayer().getCardHand().getAssociatedCards()) {
			addCard(ac);
		}
		buff.append("</tr></table>");
	}

	private void addCard(AssociatedCard ac) {
		buff.append("<td style='vertical-align: top'><form action='").append(ScgProperties.INSTANCE.getHttpHost())
				.append("/Do.action'>");
		buff.append("<input type='hidden' name='gid' value='").append(player.getGame().getId()).append("'>");
		buff.append("<input type='hidden' name='pid' value='")
				.append(player.getGame().getTurn().getCurrentPlayer().getId()).append("'>");
		addCardMain(ac);
		buff.append("</form></td>");
		buff.append(PrinterGamePlan.CR);
	}

	private void addCardMain(AssociatedCard ac) {
		Card card = ac.getCard();
		Type type = ac.getType();
		buff.append("<input type='hidden' name='card' value='").append(type.toOrdinal()).append("'/>");
		buff.append("<img src='" + ScgProperties.INSTANCE.getHttpHost() + "/images/card" + card.getNo() + ".png'/>")
				.append("<br/>");
		if (card.isPlayable()) {
			buff.append("<input type='submit' value='Play " + card.getName() + " (" + type.toOrdinal() + ")'>").append(
					"<br/>");
			if (card.getPattern().isPlayer()) {
				buff.append("target ");
				addTargetPlayerSelect(card);
				buff.append("<br/>");
				if (card.getPattern().isTargetCard()) {
					buff.append("guess card ");
					addTargetCardSelect();
					buff.append("<br/>");
				}
			}
		}
	}

	private void addTargetPlayerSelect(Card card) {
		Collection<Val> playerCol = new ArrayList<>();
		for (Player pl : player.getGame().getPlayersSorted()) {
			if (!pl.isDead() && pl.isTargetable() && card.isTargetValid(pl.getNo())) {
				String text;
				if (player == pl) {
					text = "yourself";
				} else {
					text = pl.getEmail();
				}
				text += " (" + pl.getNo() + ")";
				playerCol.add(new Val(pl.getNo(), text));
			}
		}
		addSelect("player", playerCol.toArray(new Val[playerCol.size()]));
	}

	private void addTargetCardSelect() {
		Collection<Val> targetCardCol = new ArrayList<>();
		for (int i = 2; i <= 8; i++) {
			targetCardCol.add(new Val(i, Card.get(i).getName() + " (" + i + ")"));
		}
		addSelect("targetCard", targetCardCol.toArray(new Val[targetCardCol.size()]));
	}

	private void addSelect(String name, Val... vals) {
		buff.append("<select name='").append(name).append("'>");
		for (Val val : vals) {
			buff.append("<option value='").append(val.getValue()).append("'>");
			buff.append(val.getText());
			buff.append("</option>");
		}
		buff.append("</select>");
	}

	@Value
	private class Val {
		private int value;
		private String text;
	}
}
