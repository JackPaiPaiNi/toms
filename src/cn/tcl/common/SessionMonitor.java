package cn.tcl.common;

import java.text.Format;
import java.text.SimpleDateFormat;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import org.apache.log4j.Logger;

public class SessionMonitor implements HttpSessionListener {
	private static Format format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static Logger logger = Logger.getLogger(SessionMonitor.class);
	public void sessionCreated(HttpSessionEvent arg0) {
		javax.servlet.http.HttpSession s = arg0.getSession();
		String curDate = format.format(new java.util.Date(System
				.currentTimeMillis()));
		StringBuffer buffer = new StringBuffer();
		buffer.append("[" + curDate + "]" + s.getId() + "::"
				+ s.getAttribute("loginId") + " created!MaxInactiveInterval:"
				+ s.getMaxInactiveInterval());
		logger.debug(new String(buffer));
	}

	public void sessionDestroyed(HttpSessionEvent arg0) {
		javax.servlet.http.HttpSession s = arg0.getSession();
		String curDate = format.format(new java.util.Date(System
				.currentTimeMillis()));
		StringBuffer buffer = new StringBuffer();

		buffer.append("[" + curDate + "]" + s.getId() + "::"
				+ s.getAttribute("loginId") + " destroye!");
		buffer.append("Creatation time:"
				+ format.format(new java.util.Date(s.getCreationTime())));
		buffer.append("Last Access:"
				+ format.format(new java.util.Date(s.getLastAccessedTime())));
		buffer.append("MaxInactiveInterval:" + s.getMaxInactiveInterval());

		logger.debug(new String(buffer));
		java.lang.Exception e = new java.lang.Exception("Session:" + s.getId()
				+ "::" + s.getAttribute("loginId") + " destoryed stack trace:");
		logger.debug("", e);
	}
}