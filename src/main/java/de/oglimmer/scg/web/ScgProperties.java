package de.oglimmer.scg.web;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import lombok.SneakyThrows;

public enum ScgProperties {
	INSTANCE;

	private Properties prop = new Properties();

	@SneakyThrows(value = IOException.class)
	private ScgProperties() {
		if (System.getProperty("scg.properties") != null) {
			try (FileInputStream fis = new FileInputStream(System.getProperty("scg.properties"))) {
				prop.load(fis);
			}
		} else {
			prop.load(this.getClass().getResourceAsStream("/scg.properties"));
		}
	}

	public String getSmtpHost() {
		return prop.getProperty("smtp.host", null);
	}

	public int getSmtpPort() {
		return Integer.parseInt(prop.getProperty("smtp.port", "-1"));
	}

	public String getSmtpUser() {
		return prop.getProperty("smtp.user", null);
	}

	public String getSmtpPassword() {
		return prop.getProperty("smtp.password", null);
	}

	public boolean getSmtpSSL() {
		return Boolean.parseBoolean(prop.getProperty("smtp.ssl", "FALSE"));
	}

	public boolean getSmtpEnabled() {
		return Boolean.parseBoolean(prop.getProperty("smtp.enabled", "FALSE"));
	}

	public boolean getImapEnabled() {
		return Boolean.parseBoolean(prop.getProperty("imap.enabled", "FALSE"));
	}

	public String getSmtpFrom() {
		return prop.getProperty("smtp.from", null);
	}

	public String getSmtpReplyToEmail() {
		return prop.getProperty("smtp.reply.email", null);
	}

	public String getSmtpReplyToName() {
		return prop.getProperty("smtp.reply.name", null);
	}

	public String getImapHost() {
		return prop.getProperty("imap.host", null);
	}

	public String getImapUser() {
		return prop.getProperty("imap.user", null);
	}

	public String getImapPassword() {
		return prop.getProperty("imap.password", null);
	}

	public String getBackupDir() {
		return prop.getProperty("backup.dir", null);
	}

	public String getHttpHost() {
		return prop.getProperty("http.host", null);
	}

	public String getTheme() {
		return prop.getProperty("theme", null);
	}

}
