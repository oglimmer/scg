package de.oglimmer.scg.web;

import java.io.File;
import java.io.FilenameFilter;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import de.oglimmer.scg.email.EmailSender;
import de.oglimmer.scg.email.ImapProcessorObserver;

public class ScgServletContextListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		restoreSavedGames();
		ImapProcessorObserver.INSTANCE.hashCode();
		EmailSender.start();
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		ImapProcessorObserver.INSTANCE.stop();
		EmailSender.close();
	}

	private void restoreSavedGames() {
		File backupDir = new File(ScgProperties.INSTANCE.getBackupDir());
		File[] listFiles = backupDir.listFiles(new _FilenameFilter());
		for (File file : listFiles) {
			GameManager.INSTANCE.loadGame(file);
		}
	}

	class _FilenameFilter implements FilenameFilter {
		@Override
		public boolean accept(File dir, String name) {
			return name.endsWith(".scg");
		}
	};

}
