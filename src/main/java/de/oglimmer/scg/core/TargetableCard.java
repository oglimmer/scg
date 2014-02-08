package de.oglimmer.scg.core;

public abstract class TargetableCard extends Card {

	private static final long serialVersionUID = 1L;

	protected TargetableCard(int no) {
		super(no);
	}

	@Override
	final public boolean playImpl(Integer param1, Integer param2) {
		if (isEffective(param1)) {
			Player targetPlayer = getOwner().getGame().getPlayer(param1);
			return playOnTarget(targetPlayer, param2);
		} else {
			return true;
		}
	}

	public abstract boolean playOnTarget(Player otherPlayer, Integer param2);

	/**
	 * True if a card is currently effective on a players. Do not use this to check if a player can be targeted by this
	 * card.
	 * 
	 * @param game
	 *            reference to the game
	 * @param player
	 *            reference to the owner
	 * @param param1
	 *            no of the target player
	 * @return True if the card has an effect on the target player otherwise false
	 */
	public boolean isEffective(Integer param1) {
		Player targetPlayer = getOwner().getGame().getPlayer(param1);
		return targetPlayer != getOwner();
	}

	/**
	 * Owner may only be targeted if all others are protected/dead
	 */
	@Override
	public boolean isTargetValid(Integer targetPlayerNo) {
		Player targetPlayer = getOwner().getGame().getPlayer(targetPlayerNo);
		if (targetPlayer == getOwner().getGame().getCurrentPlayer()) {
			return isAllOthersProtected();
		} else {
			return targetPlayer.isTargetable();
		}
	}

	protected boolean isAllOthersProtected() {
		for (Player p : getOwner().getGame().getPlayers()) {
			if (p.isTargetable() && p != getOwner().getGame().getCurrentPlayer()) {
				return false;
			}
		}
		return true;
	}
}
