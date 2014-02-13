package de.oglimmer.scg.core;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import lombok.Data;

@Data
public class CardStack implements Serializable {

	public static final int[] NUMBER_OF_CARDS = { 5, 2, 2, 2, 2, 1, 1, 1 };

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
		for (int cardIndex = 0; cardIndex < NUMBER_OF_CARDS.length; cardIndex++) {
			int amountOfCardsPerGame = NUMBER_OF_CARDS[cardIndex];
			for (int i = 0; i < amountOfCardsPerGame; i++) {
				add(Card.get(cardIndex + 1));
			}
		}
		shuffle();
	}

}
