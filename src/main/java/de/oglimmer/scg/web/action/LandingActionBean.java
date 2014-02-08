package de.oglimmer.scg.web.action;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

@Data
@EqualsAndHashCode(callSuper = false)
public class LandingActionBean extends BaseAction {

	@DefaultHandler
	public Resolution show() {
		return new ForwardResolution("/WEB-INF/jsp/landing.jsp");
	}

}
