package de.oglimmer.scg.core;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import lombok.Data;

@Data
public class CardStack implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<Card> cards = new LinkedList<>();

	public void add(Card card) {
		cards.add(card);
	}

	public void shuffle() {
		Collections.shuffle(cards);
	}

	public Card takeTop() {
		return cards.remove(0);
	}

}
