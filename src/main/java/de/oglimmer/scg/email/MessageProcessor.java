package de.oglimmer.scg.email;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMultipart;

import lombok.extern.slf4j.Slf4j;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;

import de.oglimmer.scg.email.InboundProcessor.NoCarriageReturnException;

@Slf4j
public class MessageProcessor {

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
		String playerId = text.substring(text.indexOf(PLAYER_START) + PLAYER_START.length(), text.indexOf(PLAYER_END));
		return playerId;
	}

	private String getGameId(String text) {
		String gameId = text.substring(text.indexOf(GAME_START) + GAME_START.length(), text.indexOf(GAME_END));
		return gameId;
	}

	private boolean hasValidStartEndTag(String text) {
		return text.indexOf(GAME_START) > 0 && text.indexOf(PLAYER_START) > 0;
	}
}