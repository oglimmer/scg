package de.oglimmer.scg.core;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AssociatedCard implements Serializable {

	private static final long serialVersionUID = 1L;

	private Card card;
	private Type type;

}
