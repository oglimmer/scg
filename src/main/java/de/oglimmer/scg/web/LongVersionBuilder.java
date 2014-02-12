package de.oglimmer.scg.web;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.util.Date;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import javax.servlet.ServletContext;

public class LongVersionBuilder {
	private String commit;
	private String version;
	private String creationDate;

	public String build(ServletContext servletContext) {
		processManifest(servletContext);
		return buildLongVersion();
	}

	private void processManifest(ServletContext servletContext) {
		try (InputStream is = new FileInputStream(servletContext.getRealPath("/META-INF/MANIFEST.MF"))) {
			readManifest(is);
		} catch (Exception e) {
			setDefaults();
		}
	}

	private String buildLongVersion() {
		return "V" + version + " [Commit#" + commit + "] build " + creationDate;
	}

	private void setDefaults() {
		commit = "?";
		creationDate = "?";
		version = "?";
	}

	private void readManifest(InputStream is) throws IOException {
		Manifest mf = new Manifest(is);
		Attributes attr = mf.getMainAttributes();
		commit = attr.getValue("SVN-Revision-No");
		version = attr.getValue("SCG-Version");
		long time = Long.parseLong(attr.getValue("Creation-Date"));
		creationDate = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(new Date(time));
	}
}