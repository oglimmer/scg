package de.oglimmer.scg.web.action;

import lombok.Getter;
import lombok.Setter;
import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.Before;
import de.oglimmer.scg.web.ScgServletContextListener;

public abstract class BaseAction implements ActionBean {

	@Getter
	@Setter
	private ActionBeanContext context;

	@Getter
	private String longVersion;

	@Before
	public void retrieveVersion() {
		longVersion = ScgServletContextListener.getLongVersion();
	}
}
