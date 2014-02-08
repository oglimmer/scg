package de.oglimmer.scg.core;

import java.io.Serializable;

import lombok.Data;

@Data
public class AssociatedCard implements Serializable {

	private static final long serialVersionUID = 1L;

	private Card card;
	private Type type;

	public AssociatedCard(Card card, Type type) {
		this.card = card;
		this.type = type;
	}

}
