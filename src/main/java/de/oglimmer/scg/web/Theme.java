package de.oglimmer.scg.web;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import lombok.SneakyThrows;

public enum Theme {
	INSTANCE;

	private Properties prop = new Properties();

	@SneakyThrows(value = IOException.class)
	private Theme() {
		loadTheme();
	}

	private void loadTheme() throws IOException {
		String themeName = ScgProperties.INSTANCE.getTheme();
		try (InputStream is = this.getClass().getResourceAsStream("/" + themeName + ".theme")) {
			prop.load(is);
		}
	}

	public String getName(int no) {
		return prop.getProperty("card" + no + ".name");
	}

	public String getDesc(int no) {
		return prop.getProperty("card" + no + ".desc");
	}

}
