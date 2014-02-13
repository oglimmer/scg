package de.oglimmer.scg.web;

public class RedirectException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private String targetUrl;

	public RedirectException(String msg, String targetUrl) {
		super(msg);
		this.targetUrl = targetUrl;
	}

	public String getTargetUrl() {
		return targetUrl;
	}

}
