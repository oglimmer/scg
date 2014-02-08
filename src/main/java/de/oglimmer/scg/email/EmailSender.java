package de.oglimmer.scg.email;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.mail.internet.InternetAddress;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

import de.oglimmer.scg.core.Game;
import de.oglimmer.scg.core.Player;
import de.oglimmer.scg.printer.PrinterGame;
import de.oglimmer.scg.printer.PrinterGameHtml;
import de.oglimmer.scg.web.ScgProperties;

@Slf4j
public class EmailSender {

	private static final String GAME_TITLE = "SimpleCardGame: ";
	private static ExecutorService exec;

	public static void start() {
		exec = new ThreadPoolExecutor(1, 1, 1L, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>());
	}

	public static void close() {
		exec.shutdownNow();
	}

	private static String printGameReplyData(Player player) {
		StringBuilder buff = new StringBuilder();
		buff.append(PrinterGame.CR);
		buff.append(PrinterGame.CR);
		buff.append("Play via Browser: " + ScgProperties.INSTANCE.getHttpHost() + "/Select.action?gid="
				+ player.getGame().getId() + "&pid=" + player.getId());
		buff.append(PrinterGame.CR);
		buff.append("NEED HELP? " + ScgProperties.INSTANCE.getHttpHost());
		buff.append(PrinterGame.CR);
		buff.append(PrinterGame.CR);
		buff.append("DO NOT CHANGE THIS:");
		buff.append(PrinterGame.CR);
		buff.append("##game:").append(player.getGame().getId()).append(":game##");
		buff.append(PrinterGame.CR);
		buff.append("##player:").append(player.getId()).append(":player##");
		return buff.toString();
	}

	public static void sendTurn(Player player, String subject, Game game) {
		if (!ScgProperties.INSTANCE.getSmtpEnabled()) {
			return;
		}
		try {
			final HtmlEmail simpleEmail = new HtmlEmail();

			prepare(player, subject, simpleEmail);

			PrinterGame pgHtml = new PrinterGameHtml(game);
			pgHtml.printTable();
			String body = pgHtml.toString() + printGameReplyData(player);
			body = body.replace("\r\n", "<br/>");

			simpleEmail.setHtmlMsg("<html><body>" + body + "</body></html>");

			PrinterGame pgPlain = new PrinterGame(game);
			pgPlain.printTable();
			simpleEmail.setTextMsg(pgPlain.toString() + printGameReplyData(player));

			simpleEmail.buildMimeMessage();
			if (exec != null) {
				exec.execute(new Runnable() {

					@Override
					public void run() {
						try {
							simpleEmail.sendMimeMessage();
						} catch (EmailException e) {
							log.error("Failed to send password email", e);
						}
					}
				});
				log.debug("Send email to {}", player.getEmail());
			}
		} catch (EmailException | UnsupportedEncodingException e) {
			log.error("Failed to send password email", e);
		}
	}

	public static void send(Player player, String subject, String msg, boolean addReplyInfo) {
		if (!ScgProperties.INSTANCE.getSmtpEnabled()) {
			return;
		}
		try {
			final HtmlEmail simpleEmail = new HtmlEmail();

			prepare(player, subject, simpleEmail);

			simpleEmail.addPart(msg + (addReplyInfo ? printGameReplyData(player) : ""), "text/plain");

			simpleEmail.buildMimeMessage();

			if (exec != null) {
				exec.execute(new Runnable() {

					@Override
					public void run() {
						try {
							simpleEmail.sendMimeMessage();
						} catch (EmailException e) {
							log.error("Failed to send password email", e);
						}
					}
				});
				log.debug("Send email to {}", player.getEmail());
			}
		} catch (EmailException | UnsupportedEncodingException e) {
			log.error("Failed to send password email", e);
		}
	}

	private static void prepare(Player player, String subject, HtmlEmail simpleEmail)
			throws UnsupportedEncodingException, EmailException {
		simpleEmail.setHostName(ScgProperties.INSTANCE.getSmtpHost());
		simpleEmail.setSmtpPort(ScgProperties.INSTANCE.getSmtpPort());
		simpleEmail.setAuthenticator(new DefaultAuthenticator(ScgProperties.INSTANCE.getSmtpUser(),
				ScgProperties.INSTANCE.getSmtpPassword()));
		simpleEmail.setSSLOnConnect(ScgProperties.INSTANCE.getSmtpSSL());

		Collection<InternetAddress> col = new ArrayList<>();
		col.add(new InternetAddress(ScgProperties.INSTANCE.getSmtpReplyToEmail(), ScgProperties.INSTANCE
				.getSmtpReplyToName()));
		simpleEmail.setReplyTo(col);
		simpleEmail.setFrom(ScgProperties.INSTANCE.getSmtpFrom());
		simpleEmail.setSubject(GAME_TITLE + subject);
		simpleEmail.addTo(player.getEmail());
	}

}
