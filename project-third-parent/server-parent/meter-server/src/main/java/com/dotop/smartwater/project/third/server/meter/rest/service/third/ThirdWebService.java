package com.dotop.smartwater.project.third.server.meter.rest.service.third;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
public interface ThirdWebService {

    @WebMethod
    String getUser(@WebParam(name = "userId") String userId);

}
