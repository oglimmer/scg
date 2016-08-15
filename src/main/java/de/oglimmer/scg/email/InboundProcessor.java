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
public class InboundProcessor {
	public static final InboundProcessor INSTANCE = new InboundProcessor();

	private StoreManager storeManager;

	private volatile Thread postboxWatchThread;

	private InboundProcessor() {
		switch (ScgProperties.INSTANCE.getMessageHandler()) {
		case "mbox":
			storeManager = new StoreManagerMbox();
			break;
		case "imap":
			storeManager = new StoreManagerImap();
			break;
		default:
			throw new RuntimeException("Unsupported messsageHandler : " + ScgProperties.INSTANCE.getMessageHandler());
		}
		log.debug("InboundProcessor uses {}", storeManager.getClass().getName());
	}

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

		void ensureConnectionFreshness() throws MessagingException;
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
			log.debug("Using mbox file at {}", ScgProperties.INSTANCE.getMboxFile());
			return store.getFolder(ScgProperties.INSTANCE.getMboxFile());
		}

		@Override
		public void ensureConnectionFreshness() throws MessagingException {

		}

	}

	class StoreManagerImap implements StoreManager {

		private String host = ScgProperties.INSTANCE.getImapHost();
		private String username = ScgProperties.INSTANCE.getImapUser();
		private String password = ScgProperties.INSTANCE.getImapPassword();

		private Session session;
		@Getter
		private Store store;

		private int counter = 0;

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

		/**
		 * gmail allows only 10 getFolders(), then you have to re-open the store
		 * 
		 * @throws MessagingException
		 */
		@Override
		public void ensureConnectionFreshness() throws MessagingException {
			if (counter++ == 10) {
				counter = 0;
				storeManager.openStore();
			}
		}
	}

	class PostboxWatcher implements Runnable {

		private FolderProcessor folderProcessor = new FolderProcessor();

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
			storeManager.ensureConnectionFreshness();
			folderProcessor.processInbox(storeManager.getFolder());
			sleep();
		}

		private void handleGeneralFailure(Exception e) {
			log.error("Failed to process imap", e);
			storeManager.openStoreNeverFail();
			sleep();
		}

	}

	static class NoCarriageReturnException extends Exception {

		private static final long serialVersionUID = 1L;

	}

}