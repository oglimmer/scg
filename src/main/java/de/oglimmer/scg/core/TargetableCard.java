package de.oglimmer.scg.core;

public abstract class TargetableCard extends Card {

	private static final long serialVersionUID = 1L;

	protected TargetableCard(int no) {
		super(no);
	}

	@Override
	final public void play(Integer targetPlayerNo, Integer targetCardNo) {
		if (isEffective(targetPlayerNo)) {
			Player targetPlayer = getOwner().getGame().getPlayer(targetPlayerNo);
			playOnTarget(targetPlayer, targetCardNo);
		} else {
			addMessageNoEffect();
		}
	}

	private void addMessageNoEffect() {
		Messages.addTo("You have played " + getName() + " against your self, but this has no effect.", getOwner());
		Messages.addNotTo("Player " + getOwner().getDisplayName() + " played " + getName()
				+ " against himself, but this had no effect.", getOwner());
	}

	public abstract void playOnTarget(Player targetPlayer, Integer targetCardNo);

	/**
	 * True if a card is currently effective on a players. Do not use this to check if a player can be targeted by this
	 * card.
	 * 
	 * @param game
	 *            reference to the game
	 * @param player
	 *            reference to the owner
	 * @param targetPlayerNo
	 *            no of the target player
	 * @return True if the card has an effect on the target player otherwise false
	 */
	public boolean isEffective(Integer targetPlayerNo) {
		Player targetPlayer = getOwner().getGame().getPlayer(targetPlayerNo);
		return targetPlayer != getOwner();
	}

	/**
	 * Owner may only be targeted if all others are protected/dead
	 */
	@Override
	public boolean isTargetValid(Integer targetPlayerNo) {
		Player targetPlayer = getOwner().getGame().getPlayer(targetPlayerNo);
		if (targetPlayer.isCurrentPlayer()) {
			return isAllOtherPlayersProtected();
		} else {
			return targetPlayer.isTargetable();
		}
	}

	protected boolean isAllOtherPlayersProtected() {
		for (Player p : getOwner().getGame().getPlayers()) {
			if (p.isTargetable() && !p.isCurrentPlayer()) {
				return false;
			}
		}
		return true;
	}
}
