package de.oglimmer.scg.web.action;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import de.oglimmer.scg.core.Card;
import de.oglimmer.scg.core.CardStack;

@Data
@EqualsAndHashCode(callSuper = false)
public class LandingActionBean extends BaseAction {

	public Card getCard(int no) {
		return Card.get(no);
	}

	public int getCardCount(int no) {
		return CardStack.NUMBER_OF_CARDS[no - 1];
	}

	@DefaultHandler
	public Resolution show() {
		return new ForwardResolution("/WEB-INF/jsp/landing.jsp");
	}

}
