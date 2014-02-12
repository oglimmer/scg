package de.oglimmer.scg.core;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

public class ScgMatcher {

	public static Matcher<Player> isDead() {
		return new BaseMatcher<Player>() {
			@Override
			public boolean matches(final Object item) {
				final Player player = (Player) item;
				return player.isDead();
			}

			@Override
			public void describeTo(final Description description) {
				description.appendText("isDead should return true");
			}
		};
	}

	public static Matcher<Player> isAlive() {
		return new BaseMatcher<Player>() {
			@Override
			public boolean matches(final Object item) {
				final Player player = (Player) item;
				return !player.isDead();
			}

			@Override
			public void describeTo(final Description description) {
				description.appendText("isDead should return false");
			}
		};
	}

}
