package com.dotop.smartwater.project.third.server.meter.config;

import com.dotop.smartwater.project.third.server.meter.rest.service.third.ThirdWebService;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.ws.Endpoint;

/**
 *
 * http://localhost:47789/meter-server/services/third?wsdl
 */
@Configuration
public class CxfConfig {

    @Autowired
    private Bus bus;

    @Autowired
    private ThirdWebService thirdWebService;

    //    @SuppressWarnings("all")
    //    @Bean
    //    public ServletRegistrationBean dispatcherServlet() {
    //        return new ServletRegistrationBean(new CXFServlet(), "/soap/*");
    //    }

    @Bean
    public Endpoint endpoint() {
        EndpointImpl endpoint = new EndpointImpl(bus, thirdWebService);
        endpoint.publish("/third");
        return endpoint;
    }

}

