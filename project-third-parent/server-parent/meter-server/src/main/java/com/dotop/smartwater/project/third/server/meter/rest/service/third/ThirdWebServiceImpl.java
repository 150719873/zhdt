package com.dotop.smartwater.project.third.server.meter.rest.service.third;

import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

import javax.jws.WebService;

@WebService(serviceName = "ThirdWebService",
        targetNamespace = "http://third.service.rest.meter.server.third.project.smartwater.dotop.com/",
        endpointInterface = "com.dotop.smartwater.project.third.server.meter.rest.service.third.ThirdWebService")
@Component
public class ThirdWebServiceImpl implements ThirdWebService {

    @Override
    public String getUser(String userId) {
        return userId + "-" + new DateTime().toString();
    }


}

