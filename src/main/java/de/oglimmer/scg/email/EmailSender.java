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
import de.oglimmer.scg.printer.PrinterGamePlan;
import de.oglimmer.scg.printer.PrinterGamePlanHtml;
import de.oglimmer.scg.web.ScgProperties;

@Slf4j
public class EmailSender {

	private static final String SUBJECT_GAME_TITLE = "SimpleCardGame: ";
	private static ExecutorService exec;

	public static void start() {
		exec = new ThreadPoolExecutor(1, 1, 1L, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>());
	}

	public static void close() {
		exec.shutdownNow();
	}

	private static String printGameReplyData(Player player) {
		StringBuilder buff = new StringBuilder();
		buff.append(PrinterGamePlan.CR);
		buff.append(PrinterGamePlan.CR);
		buff.append("Play via Browser: " + ScgProperties.INSTANCE.getHttpHost() + "/Select.action?gid="
				+ player.getGame().getId() + "&pid=" + player.getId());
		buff.append(PrinterGamePlan.CR);
		buff.append("NEED HELP? " + ScgProperties.INSTANCE.getHttpHost());
		buff.append(PrinterGamePlan.CR);
		buff.append(PrinterGamePlan.CR);
		buff.append("DO NOT CHANGE THIS:");
		buff.append(PrinterGamePlan.CR);
		buff.append("##game:").append(player.getGame().getId()).append(":game##");
		buff.append(PrinterGamePlan.CR);
		buff.append("##player:").append(player.getId()).append(":player##");
		return buff.toString();
	}

	public static void sendTurn(Player player, String subject, Game game) {
		if (!ScgProperties.INSTANCE.getSmtpEnabled()) {
			return;
		}
		try {
			sendTurnMessage(player, subject, game);
		} catch (EmailException | UnsupportedEncodingException e) {
			handleEmailSentException(e);
		}
	}

	private static void sendTurnMessage(Player player, String subject, Game game) throws UnsupportedEncodingException,
			EmailException {
		final HtmlEmail simpleEmail = buildEmail(player, subject, game);
		sendToSmtp(player, simpleEmail);
	}

	private static HtmlEmail buildEmail(Player player, String subject, Game game) throws UnsupportedEncodingException,
			EmailException {
		HtmlEmail simpleEmail = new HtmlEmail();
		fillHeaders(player, subject, simpleEmail);
		buildHtmlPart(player, game, simpleEmail);
		buildPlainPart(player, game, simpleEmail);
		simpleEmail.buildMimeMessage();
		return simpleEmail;
	}

	private static void buildPlainPart(Player player, Game game, HtmlEmail simpleEmail) throws EmailException {
		PrinterGamePlan pgPlain = new PrinterGamePlan(game);
		pgPlain.printTable();
		simpleEmail.setTextMsg(pgPlain.toString() + printGameReplyData(player));
	}

	private static void buildHtmlPart(Player player, Game game, HtmlEmail simpleEmail) throws EmailException {
		PrinterGamePlan pgHtml = new PrinterGamePlanHtml(game);
		pgHtml.printTable();
		String body = pgHtml.toString() + printGameReplyData(player);
		body = body.replace(PrinterGamePlan.CR, "<br/>");

		simpleEmail.setHtmlMsg("<html><body>" + body + "</body></html>");
	}

	public static void sendPlain(Player player, String subject, String msg) {
		if (!ScgProperties.INSTANCE.getSmtpEnabled()) {
			return;
		}
		try {
			sendPlainMessage(player, subject, msg);
		} catch (EmailException | UnsupportedEncodingException e) {
			handleEmailSentException(e);
		}
	}

	private static void sendPlainMessage(Player player, String subject, String msg)
			throws UnsupportedEncodingException, EmailException {
		HtmlEmail simpleEmail = buildEmail(player, subject, msg);
		sendToSmtp(player, simpleEmail);
	}

	private static HtmlEmail buildEmail(Player player, String subject, String msg) throws UnsupportedEncodingException,
			EmailException {
		final HtmlEmail simpleEmail = new HtmlEmail();
		fillHeaders(player, subject, simpleEmail);
		simpleEmail.addPart(msg, "text/plain");
		simpleEmail.buildMimeMessage();
		return simpleEmail;
	}

	private static void sendToSmtp(Player player, final HtmlEmail simpleEmail) {
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
	}

	private static void handleEmailSentException(Exception e) {
		log.error("Failed to send password email", e);
	}

	private static void fillHeaders(Player player, String subject, HtmlEmail simpleEmail)
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
		simpleEmail.setSubject(SUBJECT_GAME_TITLE + subject);
		simpleEmail.addTo(player.getEmail());
	}

}
