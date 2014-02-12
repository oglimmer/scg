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

	public void init() {
		add(Card.get(8));
		add(Card.get(7));
		add(Card.get(6));
		add(Card.get(5));
		add(Card.get(5));
		add(Card.get(4));
		add(Card.get(4));
		add(Card.get(3));
		add(Card.get(3));
		add(Card.get(2));
		add(Card.get(2));
		add(Card.get(1));
		add(Card.get(1));
		add(Card.get(1));
		add(Card.get(1));
		add(Card.get(1));
		shuffle();
	}

}
