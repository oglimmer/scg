package de.oglimmer.scg.web;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import lombok.SneakyThrows;

public enum ScgProperties {
	INSTANCE;

	private static final String SYSTEM_PROPERTY = "scg.properties";
	private Properties prop = new Properties();

	@SneakyThrows(value = IOException.class)
	private ScgProperties() {
		if (isAbsoluteFilepathAvail()) {
			loadFromAbsoluteFilename();
		} else {
			loadFromClasspath();
		}
	}

	private boolean isAbsoluteFilepathAvail() {
		return getFilenameProperty() != null;
	}

	private String getFilenameProperty() {
		return System.getProperty(SYSTEM_PROPERTY);
	}

	private void loadFromClasspath() throws IOException {
		prop.load(this.getClass().getResourceAsStream("/scg.properties"));
	}

	private void loadFromAbsoluteFilename() throws IOException, FileNotFoundException {
		try (InputStream is = new FileInputStream(getFilenameProperty())) {
			prop.load(is);
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
