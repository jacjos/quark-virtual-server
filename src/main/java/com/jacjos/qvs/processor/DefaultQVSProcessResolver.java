package com.jacjos.qvs.processor;

import com.jacjos.qvs.common.RequestDTO;
import com.jacjos.qvs.common.Util;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.MediaType;
import java.util.Optional;

@ApplicationScoped
public class DefaultQVSProcessResolver implements QVSProcessResolver {

    @ConfigProperty(name = "qvs.proxy.services") String[] proxyServices;

    @Override
    public QVSProcessDTO resolve(RequestDTO requestDTO) {

        String svcIdentifier = MediaType.APPLICATION_JSON.equalsIgnoreCase(requestDTO.getContentType())
                ? Util.extractServiceIdentifierFromURI(requestDTO.getUri(), requestDTO.getHttpMethod())
                : Util.extractServiceIdentifierFromSoapXML(requestDTO.getRequestBody());

        QVSProcessDTO processDTO = new QVSProcessDTO();
        processDTO.setRequestDTO(requestDTO);
        processDTO.setServiceIdentifier(svcIdentifier);

        Optional<String> proxy = Util.getConfiguredProxy(proxyServices, svcIdentifier);
        if (proxy.isPresent()){
            processDTO.setProcessor("ProxyProcessor");
            processDTO.setProxy(proxy.get());
            return processDTO;
        }

        processDTO.setProcessor("DefaultProcessor");
        return processDTO;
    }
}
