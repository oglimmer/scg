package de.oglimmer.scg.web;

import java.io.IOException;
import java.util.Properties;

import lombok.SneakyThrows;

public enum Theme {
	INSTANCE;

	private Properties prop = new Properties();

	@SneakyThrows(value = IOException.class)
	private Theme() {
		String themeName = ScgProperties.INSTANCE.getTheme();
		prop.load(this.getClass().getResourceAsStream("/" + themeName + ".theme"));
	}

	public String getName(int no) {
		return prop.getProperty("card" + no + ".name");
	}

	public String getDesc(int no) {
		return prop.getProperty("card" + no + ".desc");
	}

}
