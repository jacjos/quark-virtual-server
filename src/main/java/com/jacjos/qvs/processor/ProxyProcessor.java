package com.jacjos.qvs.processor;

import com.jacjos.qvs.common.QVSException;
import com.jacjos.qvs.common.ResponseDTO;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.util.concurrent.TimeUnit;

@ApplicationScoped @Named("ProxyProcessor")
public class ProxyProcessor implements QVSProcessor {

    Logger log = LoggerFactory.getLogger(ProxyProcessor.class);

    @ConfigProperty(name = "qvs.proxy.connector.timeout.connect") int connectTimeout;
    @ConfigProperty(name = "qvs.proxy.connector.timeout.read") int readTimeout;

    @Override
    public ResponseDTO execute(QVSProcessDTO processDTO) throws QVSException {
        log.debug("Enter: ProxyProcessor.execute");

        Response rsp;
        String contentType = processDTO.getRequestDTO().getContentType() == null
                ? MediaType.TEXT_XML
                : processDTO.getRequestDTO().getContentType();
        String proxyUrl = getConfigProperty("qvs.proxy."+processDTO.getProxy()+".url")
                + (processDTO.getRequestDTO().getUri() == null ? "": processDTO.getRequestDTO().getUri());

        log.info("Forwarding request to Proxy URL : {}", proxyUrl);

        WebTarget target = getRestWebTarget(proxyUrl);
        Invocation.Builder invocationBuilder = target.request(contentType);
        processDTO.getRequestDTO().getReqHeaders().getRequestHeaders().entrySet().stream()
                .filter(entry -> entry.getKey().startsWith("X-"))
                .forEach(entry -> invocationBuilder.header(entry.getKey(), entry.getValue().get(0)));

        if (HttpMethod.GET.equalsIgnoreCase(processDTO.getRequestDTO().getHttpMethod())){
            rsp = invocationBuilder.get();
        } else{
            rsp = invocationBuilder.post(Entity.entity(processDTO.getRequestDTO().getRequestBody(), contentType));
        }

        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setStatus(rsp.getStatus());
        responseDTO.setResponseBody(rsp.readEntity(String.class));
        responseDTO.setResponseHeaders(rsp.getStringHeaders());

        log.info("Downstream Response :: {}", responseDTO.getResponseBody());
        return responseDTO;
    }

    private String getConfigProperty(String key){
        return ConfigProvider.getConfig().getValue(key, String.class);
    }

    private WebTarget getRestWebTarget(String url) throws QVSException{

        ClientBuilder clientBuilder = ClientBuilder.newBuilder()
                .connectTimeout(connectTimeout, TimeUnit.SECONDS)
                .readTimeout(readTimeout, TimeUnit.SECONDS);

        if (url.startsWith("https:")){

            String keyStore = getConfigProperty("qvs.proxy.connector.default.keystore");
            String keyStorePass = getConfigProperty("qvs.proxy.connector.default.keystore.pass");
            String trustStore = getConfigProperty("qvs.proxy.connector.default.truststore");
            String trustStorePass = getConfigProperty("qvs.proxy.connector.default.truststore.pass");
            clientBuilder.sslContext(getSSLContext(keyStore,keyStorePass,trustStore,trustStorePass))
                    .hostnameVerifier(getHostnameVerifier());
        }

        Client restClient = clientBuilder.build();
        
        return restClient.target(url);
    }

    private SSLContext getSSLContext(
            String keystoreFilePath,
            String keystorePassword,
            String truststoreFilePath,
            String truststorePassword) throws QVSException {

        try(FileInputStream keyFis = new FileInputStream(keystoreFilePath);
            FileInputStream trustFis = new FileInputStream(truststoreFilePath)) {

            KeyStore keystore = KeyStore.getInstance("JKS");
            keystore.load(keyFis, keystorePassword.toCharArray());

            KeyStore truststore = KeyStore.getInstance("JKS");
            truststore.load(trustFis, truststorePassword.toCharArray());

            SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(keystore,keystorePassword.toCharArray());
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(truststore);
            sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
            return sslContext;
        } catch (Exception e) {
            throw new QVSException("Exception while instantiating SSL Context",e);
        }

    }

    private HostnameVerifier getHostnameVerifier(){
        return new NoopHostnameVerifier();
    }

}
