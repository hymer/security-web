package com.hymer.core;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * session listener.
 * @author hymer
 *
 */
public class SessionListener implements HttpSessionListener {

	@Override
	public void sessionCreated(HttpSessionEvent sessionEvent) {
//		log.info("session created.");
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent sessionEvent) {
//		log.info("session destroyed.");
		SessionContext.deleteSession(sessionEvent.getSession());
	}

}
