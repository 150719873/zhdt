package com.dotop.smartwater.project.third.module.core.utils.webservice;

import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.net.ssl.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.List;

/**
 *
 */
public class WebServiceUtils {

    private static final Logger LOGGER = LogManager.getLogger(WebServiceUtils.class);

    public static Object[] client(String url, String method, List<Object> params) throws FrameworkRuntimeException {
        try {
            LOGGER.info(LogMsg.to("url", url, "method", method, "params", params));
            if (url.startsWith("https")) {
                HTTPSTrustManager.allowAllSSL();
            }
            JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
            Client client = dcf.createClient(url);
            if (url.startsWith("https")) {
                HTTPConduit conduit = (HTTPConduit) client.getConduit();
                TLSClientParameters tlsClientParameters = new TLSClientParameters();
                tlsClientParameters.setUseHttpsURLConnectionDefaultHostnameVerifier(true);
                tlsClientParameters.setUseHttpsURLConnectionDefaultSslSocketFactory(true);
                conduit.setTlsClientParameters(tlsClientParameters);
            }
            Object[] objects = client.invoke(method, params.toArray());
            return objects;
        } catch (Exception e) {
            LOGGER.error(LogMsg.to("url", url, "method", method, "params", params));
            LOGGER.error(e);
            throw new FrameworkRuntimeException(BaseExceptionConstants.REQUEST_ERROR, e);
        }
    }
}

class HTTPSTrustManager implements X509TrustManager {

    private static TrustManager[] trustManagers;
    private static final X509Certificate[] _AcceptedIssuers = new X509Certificate[]{};

    @Override
    public void checkClientTrusted(
            java.security.cert.X509Certificate[] x509Certificates, String s)
            throws java.security.cert.CertificateException {
        // To change body of implemented methods use File | Settings | File
        // Templates.
    }

    @Override
    public void checkServerTrusted(
            java.security.cert.X509Certificate[] x509Certificates, String s)
            throws java.security.cert.CertificateException {
        // To change body of implemented methods use File | Settings | File
        // Templates.
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return _AcceptedIssuers;
    }

    public static void allowAllSSL() {
        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {

            @Override
            public boolean verify(String arg0, SSLSession arg1) {
                // TODO Auto-generated method stub
                return true;
            }

        });

        SSLContext context = null;
        if (trustManagers == null) {
            trustManagers = new TrustManager[]{new HTTPSTrustManager()};
        }

        try {
            context = SSLContext.getInstance("TLS");
            context.init(null, trustManagers, new SecureRandom());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
    }
}
