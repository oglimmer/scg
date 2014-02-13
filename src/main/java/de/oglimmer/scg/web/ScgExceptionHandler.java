package de.oglimmer.scg.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.stripes.config.Configuration;
import net.sourceforge.stripes.exception.ExceptionHandler;

public class ScgExceptionHandler implements ExceptionHandler {

	@Override
	public void init(Configuration configuration) throws Exception {
	}

	@Override
	public void handle(Throwable throwable, HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (throwable instanceof RedirectException) {
			RedirectException e = (RedirectException) throwable;
			response.sendRedirect(e.getTargetUrl());
		} else {
			response.sendRedirect("Landing.action");
		}
	}

}
