package de.oglimmer.scg.web;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import de.oglimmer.scg.email.EmailSender;
import de.oglimmer.scg.email.InboundProcessorObserver;
import de.oglimmer.utils.VersionFromManifest;
import lombok.Getter;

public class ScgServletContextListener implements ServletContextListener {

	@Getter
	private static String longVersion;

	@Override
	public void contextInitialized(ServletContextEvent sc) {
		VersionFromManifest vfm = new VersionFromManifest();
		vfm.initFromFile(sc.getServletContext().getRealPath("/META-INF/MANIFEST.MF"));
		longVersion = vfm.getLongVersion();
		GameManager.INSTANCE.restoreSavedGames();
		InboundProcessorObserver.INSTANCE.hashCode();
		EmailSender.start();
	}

	@Override
	public void contextDestroyed(ServletContextEvent sc) {
		InboundProcessorObserver.INSTANCE.stop();
		EmailSender.close();
	}

}
