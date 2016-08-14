package de.oglimmer.scg.web;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import lombok.Getter;
import de.oglimmer.scg.email.EmailSender;
import de.oglimmer.scg.email.InboundProcessorObserver;

public class ScgServletContextListener implements ServletContextListener {

	@Getter
	private static String longVersion;

	@Override
	public void contextInitialized(ServletContextEvent sc) {
		longVersion = new LongVersionBuilder().build(sc.getServletContext());
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
