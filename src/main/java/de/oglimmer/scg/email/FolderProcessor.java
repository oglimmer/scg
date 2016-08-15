package de.oglimmer.scg.email;

import java.io.IOException;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FolderProcessor {

	private MessageProcessor messageProcessor = new MessageProcessor();

	void processInbox(Folder folder) throws MessagingException, IOException {
		folder.open(Folder.READ_WRITE);
		Message[] message = getMessages(folder);
		processMessages(message);
		folder.expunge();
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