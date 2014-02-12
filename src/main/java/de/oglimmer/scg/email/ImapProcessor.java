package de.oglimmer.scg.email;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import de.oglimmer.scg.web.ScgProperties;

@Slf4j
public enum ImapProcessor {
	INSTANCE;

	private StoreManager storeManager = new StoreManager();

	private Thread postboxWatchThread;

	@SneakyThrows(value = { MessagingException.class })
	public void startThreadInitImap() {
		storeManager.openStore();
		startThread();
	}

	@Synchronized
	private void startThread() {
		if (postboxWatchThread == null) {
			postboxWatchThread = new Thread(new PostboxWatcher());
			postboxWatchThread.start();
		}
	}

	@Synchronized
	public void stopThread() {
		if (postboxWatchThread != null) {
			postboxWatchThread.interrupt();
			postboxWatchThread = null;
		}
	}

	private void sleep() {
		try {
			TimeUnit.SECONDS.sleep(15);
		} catch (InterruptedException e1) {
			// ignore
		}
	}

	class StoreManager {

		private String host = ScgProperties.INSTANCE.getImapHost();
		private String username = ScgProperties.INSTANCE.getImapUser();
		private String password = ScgProperties.INSTANCE.getImapPassword();

		private Session session;
		@Getter
		private Store store;

		StoreManager() {
			initSession();
		}

		void openStore() throws MessagingException {
			closeStore();
			store = session.getStore("imap");
			store.connect(host, username, password);
		}

		void openStoreNeverFail() {
			try {
				openStore();
			} catch (MessagingException e) {
				log.error("Failed to open imap-store", e);
			}
		}

		void closeStore() {
			try {
				if (store != null) {
					store.close();
				}
			} catch (MessagingException e) {
				log.error("Failed to close imap-store", e);
			}
		}

		private void initSession() {
			if (session == null) {
				Properties props = new Properties();
				props.setProperty("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
				props.setProperty("mail.imap.socketFactory.fallback", "false");
				props.setProperty("mail.imap.socketFactory.port", "993");
				session = Session.getInstance(props, null);
			}
		}
	}

	class PostboxWatcher implements Runnable {
		private int counter = 0;
		private FolderProcessor folderProcessor = new FolderProcessor(storeManager);

		public void run() {
			log.info("ImapProcessor started");
			processLoop();
			storeManager.closeStore();
			log.info("ImapProcessor ended");
		}

		private void processLoop() {
			while (postboxWatchThread != null) {
				try {
					processLoopRun();
				} catch (Exception e) {
					handleGeneralFailure(e);
				}
			}
		}

		private void processLoopRun() throws MessagingException, IOException {
			ensureConnectionFreshness();
			folderProcessor.processInbox();
			sleep();
		}

		private void handleGeneralFailure(Exception e) {
			log.error("Failed to process imap", e);
			storeManager.openStoreNeverFail();
			sleep();
		}

		/**
		 * gmail allows only 15 getFolders(), then you have to re-open the store
		 * 
		 * @throws MessagingException
		 */
		private void ensureConnectionFreshness() throws MessagingException {
			if (counter++ == 10) {
				counter = 0;
				storeManager.openStore();
			}
		}

	}

	static class NoCarriageReturnException extends Exception {

		private static final long serialVersionUID = 1L;

	}

}