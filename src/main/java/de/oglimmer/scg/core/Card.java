package de.oglimmer.scg.core;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import de.oglimmer.scg.web.Theme;

@Data
@EqualsAndHashCode(of = { "no" })
public abstract class Card implements Serializable {

	private static final long serialVersionUID = 1L;

	@SneakyThrows(value = { InstantiationException.class, IllegalAccessException.class, ClassNotFoundException.class })
	public static Card get(int no) {
		return (Card) Class.forName("de.oglimmer.scg.core.Card" + no).newInstance();
	}

	private int no;

	private Player owner;

	protected Card(int no) {
		this.no = no;
	}

	public abstract boolean playImpl(Integer param1, Integer param2);

	final public boolean play(Integer param1, Integer param2) {
		return playImpl(param1, param2);
	}

	public String getName() {
		return Theme.INSTANCE.getName(no);
	}

	public String getDescription() {
		return Theme.INSTANCE.getDesc(no);
	}

	public int getNo() {
		return no;
	}

	public void discard(Runnable onSuccess) {
		owner.getCardHand().removeCard(this, true);
		onSuccess.run();
	}

	/**
	 * True if this card is currently playable by player
	 * 
	 * @param player
	 *            owner of the card
	 * @return
	 */
	public boolean isPlayable() {
		return true;
	}

	public ResultPattern getPattern() {
		return ResultPattern.CARD_ONLY;
	}

	/**
	 * True if the target player param1 is at the moment valid for this card. The card may ignore param1 at all, in this
	 * case it must return true.
	 * 
	 * @param game
	 * @param param1
	 * @return
	 */
	public boolean isTargetValid(Integer param1) {
		return true;
	}

}
