package Kakao.kakaoeng.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import Kakao.kakaoeng.FileSaver;

public class ContextLoaderListener implements ServletContextListener {
	@Override
	public void contextDestroyed(ServletContextEvent event) {
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		ServletContext sc = event.getServletContext();
		FileSaver.setWebContentFolder(sc.getRealPath("/"));;
		ApplicationContext applicationContext = new GenericXmlApplicationContext("../beanConfig.xml");
		sc.setAttribute("applicationContext", applicationContext);
	}

}
