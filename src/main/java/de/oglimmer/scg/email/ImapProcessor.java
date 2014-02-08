package de.oglimmer.scg.email;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMultipart;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;

import de.oglimmer.scg.web.ScgProperties;

@Slf4j
public enum ImapProcessor {
	INSTANCE;

	private Session session;
	private Store store;
	private boolean running;
	private Thread postboxWatchThread;

	@Getter
	private String host = ScgProperties.INSTANCE.getImapHost();

	@Getter
	private String username = ScgProperties.INSTANCE.getImapUser();

	@Getter
	private String password = ScgProperties.INSTANCE.getImapPassword();

	private ImapProcessor() {
		running = true;
	}

	@SneakyThrows(value = { MessagingException.class })
	public void init() {
		openStore();
		if (postboxWatchThread == null) {
			postboxWatchThread = new Thread(new PostboxWatcher());
			postboxWatchThread.start();
		}
	}

	public void stop() {
		running = false;
		if (postboxWatchThread != null) {
			postboxWatchThread.interrupt();
		}
	}

	private void openStore() throws MessagingException {
		initSession();

		if (store != null) {
			store.close();
		}

		store = session.getStore("imap");
		store.connect(getHost(), getUsername(), getPassword());

	}

	private void reopenStore() {
		try {
			openStore();
		} catch (MessagingException e) {
			log.error("Failed to open imap-store", e);
		}
	}

	private void sleep() {
		try {
			TimeUnit.SECONDS.sleep(15);
		} catch (InterruptedException e1) {
			// ignore
		}
	}

	private void closeStore() {
		try {
			store.close();
		} catch (MessagingException e) {
			log.error("Failed to close imap-store", e);
		}
	}

	private void initSession() {
		if (session == null) {
			// configure the jvm to use the jsse security.
			// java.security.Security
			// .addProvider(new com.sun.net.ssl.internal.ssl.Provider());

			// create the properties for the Session
			Properties props = new Properties();

			// set this session up to use SSL for IMAP connections
			props.setProperty("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			// don't fallback to normal IMAP connections on failure.
			props.setProperty("mail.imap.socketFactory.fallback", "false");
			// use the simap port for imap/ssl connections.
			props.setProperty("mail.imap.socketFactory.port", "993");

			// note that you can also use the defult imap port (including the
			// port specified by mail.imap.port) for your SSL port configuration.
			// however, specifying mail.imap.socketFactory.port means that,
			// if you decide to use fallback, you can try your SSL connection
			// on the SSL port, and if it fails, you can fallback to the normal
			// IMAP port.

			session = Session.getInstance(props, null);
		}

	}

	class PostboxWatcher implements Runnable {
		private int counter = 0;
		private FolderProcessor folderProcessor = new FolderProcessor();

		public void run() {
			log.info("ImapProcessor started");

			while (running) {
				try {
					ensureConnectionFreshness();
					folderProcessor.processInbox();
					sleep();
				} catch (Exception e) {
					log.error("Failed to process imap", e);
					reopenStore();
					sleep();
				}
			}

			closeStore();
			log.info("ImapProcessor ended");
		}

		/**
		 * gmail allows only 15 getFolders(), then you have to re-open the store
		 * 
		 * @throws MessagingException
		 */
		private void ensureConnectionFreshness() throws MessagingException {
			if (counter++ == 10) {
				counter = 0;
				openStore();
			}
		}

	}

	class FolderProcessor {

		private MessageProcessor messageProcessor = new MessageProcessor();

		void processInbox() throws MessagingException, IOException {
			Folder folder = openInboxReadWrite();
			Message[] message = getMessages(folder);
			processMessages(message);
			folder.expunge();
		}

		private Folder openInboxReadWrite() throws MessagingException {
			Folder folder = store.getFolder("INBOX");
			folder.open(Folder.READ_WRITE);
			return folder;
		}

		private Message[] getMessages(Folder folder) throws MessagingException {
			Message[] message = folder.getMessages();
			log.trace("Found {} messages", message.length);
			return message;
		}

		private void processMessages(Message[] messages) throws MessagingException, IOException {
			for (Message message : messages) {
				processUnseenMessage(message);
			}
		}

		private void processUnseenMessage(Message m) throws MessagingException, IOException {
			if (!m.getFlags().contains(Flags.Flag.SEEN)) {
				boolean processed = messageProcessor.processMessage(m);
				if (processed) {
					m.setFlag(Flags.Flag.DELETED, true);
				} else {
					m.setFlag(Flags.Flag.SEEN, true);
				}
			}
		}
	}

	class MessageProcessor {

		private static final String PLAYER_END = ":player##";
		private static final String GAME_END = ":game##";
		private static final String PLAYER_START = "##player:";
		private static final String GAME_START = "##game:";

		boolean processMessage(Message message) throws IOException, MessagingException {
			Object content = message.getContent();
			boolean processed = processContent(content);
			if (!processed) {
				log.warn("Failed to process " + content);
			}
			return processed;
		}

		private boolean processContent(Object content) throws IOException, MessagingException {
			boolean processed = false;
			if (content instanceof String) {
				processed = processReply((String) content);
			} else if (content instanceof MimeMultipart) {
				processed = processMessageMultiPart((MimeMultipart) content);
			} else if (content instanceof InputStream) {
				processed = processMessageInputStream((InputStream) content);
			}
			return processed;
		}

		private boolean processMessageInputStream(InputStream text) throws IOException, MessagingException {
			return processReply(CharStreams.toString(new InputStreamReader(text, Charsets.UTF_8)));
		}

		private boolean processMessageMultiPart(MimeMultipart mimeMultipart) throws IOException, MessagingException {
			boolean processed = false;
			for (int i = 0; i < mimeMultipart.getCount() && !processed; i++) {
				BodyPart bodyPort = mimeMultipart.getBodyPart(i);
				Object content = bodyPort.getContent();
				processed = processContent(content);
			}
			return processed;
		}

		private boolean processReply(String text) {
			try {
				return processReplyText(text);
			} catch (NoCarriageReturnException e) {
				return false;
			}
		}

		private boolean processReplyText(String text) throws NoCarriageReturnException {
			if (hasValidStartEndTag(text)) {
				processValidReply(text);
				return true;
			}
			return false;
		}

		private void processValidReply(String text) throws NoCarriageReturnException {
			String gameId = getGameId(text);
			String playerId = getPlayerId(text);
			String firstRow = getFirstRow(text);

			log.debug("game:{}, player:{}, msg:{}", gameId, playerId, firstRow);

			new EmailTurn(gameId, playerId, firstRow).process();
		}

		private String getFirstRow(String text) throws NoCarriageReturnException {
			String firstRow;
			if (text.indexOf("\r") > -1) {
				firstRow = text.substring(0, text.indexOf("\r"));
			} else if (text.indexOf("\n") > -1) {
				firstRow = text.substring(0, text.indexOf("\n"));
			} else {
				throw new NoCarriageReturnException();
			}
			return firstRow;
		}

		private String getPlayerId(String text) {
			String playerId = text.substring(text.indexOf(PLAYER_START) + PLAYER_START.length(),
					text.indexOf(PLAYER_END));
			return playerId;
		}

		private String getGameId(String text) {
			String gameId = text.substring(text.indexOf(GAME_START) + PLAYER_START.length(), text.indexOf(GAME_END));
			return gameId;
		}

		private boolean hasValidStartEndTag(String text) {
			return text.indexOf(GAME_START) > 0 && text.indexOf(PLAYER_START) > 0;
		}
	}

	class NoCarriageReturnException extends Exception {

		private static final long serialVersionUID = 1L;

	}

}