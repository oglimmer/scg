package de.oglimmer.scg.web;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum Theme {
	INSTANCE;

	private Properties prop = new Properties();

	private Theme() {
		loadTheme();
	}

	private void loadTheme() {
		String themeName = ScgProperties.INSTANCE.getTheme();
		try (InputStream is = this.getClass().getResourceAsStream("/" + themeName + ".theme")) {
			if (is != null) {
				prop.load(is);
			} else {
				log.error("Failed to load theme=" + themeName);
			}
		} catch (IOException e) {
			log.error("Failed to load theme=" + themeName, e);
		}
	}

	public String getName(int no) {
		return prop.getProperty("card" + no + ".name");
	}

	public String getDesc(int no) {
		return prop.getProperty("card" + no + ".desc");
	}

}
