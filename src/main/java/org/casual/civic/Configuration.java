package org.casual.civic;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.DispatcherServlet;

@EnableTransactionManagement(proxyTargetClass = true)
@EnableJpaRepositories("org.casual.civic.*")
@ComponentScan(basePackages = { "org.casual.civic.*" })
@EnableAutoConfiguration
public class Configuration {
	
	@Bean
    public EmbeddedServletContainerFactory servletContainer() {
        TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory();
        tomcat.setContextPath(getContextPath());
        return tomcat;
    }
	
	private String getContextPath() {
        return "/civic-core";
    }
	
	@Bean   
    public DispatcherServlet dispatcherServlet() {
        DispatcherServlet ds = new DispatcherServlet();
        ds.setThrowExceptionIfNoHandlerFound(true);
        return ds;
    }

}
