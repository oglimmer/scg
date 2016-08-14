package de.oglimmer.scg.email;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;

import de.oglimmer.scg.web.ScgProperties;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum InboundProcessor {
	INSTANCE;

	private StoreManager storeManager = ScgProperties.INSTANCE.getMessageHandler().equalsIgnoreCase("mbox")
			? new StoreManagerMbox() : new StoreManagerImap();

	private Thread postboxWatchThread;

	@SneakyThrows(value = { MessagingException.class })
	public void startThreadInitInbound() {
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

	interface StoreManager {

		void openStore() throws MessagingException;

		void openStoreNeverFail();

		void closeStore();

		Folder getFolder() throws MessagingException;

	}

	class StoreManagerMbox implements StoreManager {

		private Session session;
		@Getter
		private Store store;

		StoreManagerMbox() {
			System.setProperty("mail.mbox.locktype", "java");
			session = Session.getDefaultInstance(new Properties());
		}

		@Override
		public void openStore() throws MessagingException {
			closeStore();
			store = session.getStore("mbox");
			store.connect();
		}

		@Override
		public void openStoreNeverFail() {
			try {
				openStore();
			} catch (MessagingException e) {
				log.error("Failed to open mbox-store", e);
			}
		}

		@Override
		public void closeStore() {
			try {
				if (store != null) {
					store.close();
				}
			} catch (MessagingException e) {
				log.error("Failed to close mbox-store", e);
			}
		}

		@Override
		public Folder getFolder() throws MessagingException {
			return store.getFolder(ScgProperties.INSTANCE.getMboxFile());
		}

	}

	class StoreManagerImap implements StoreManager {

		private String host = ScgProperties.INSTANCE.getImapHost();
		private String username = ScgProperties.INSTANCE.getImapUser();
		private String password = ScgProperties.INSTANCE.getImapPassword();

		private Session session;
		@Getter
		private Store store;

		StoreManagerImap() {
			initSession();
		}

		@Override
		public void openStore() throws MessagingException {
			closeStore();
			store = session.getStore("imap");
			store.connect(host, username, password);
		}

		@Override
		public void openStoreNeverFail() {
			try {
				openStore();
			} catch (MessagingException e) {
				log.error("Failed to open imap-store", e);
			}
		}

		@Override
		public void closeStore() {
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

		@Override
		public Folder getFolder() throws MessagingException {
			return store.getFolder("INBOX");
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