package de.oglimmer.scg.generic;

import lombok.Value;
import lombok.Builder;

@Value
@Builder
public class ExpectedResult {
	private boolean playResult;
	private boolean playerADead;
	private boolean playerBDead;
	private int playerAHand;
	private Integer playerAOpen;
	private int playerBHand;
}