package com.jacjos.qvs.processor;

import com.jacjos.qvs.common.ResponseDTO;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@ApplicationScoped @Named("DefaultProcessor")
public class DefaultProcessor implements QVSProcessor {

    @ConfigProperty(name="qvs.response.path") String responseDir;
    @ConfigProperty(name="qvs.response.suffix.xml") String xmlResponseSuffix;
    @ConfigProperty(name="qvs.response.defaultfile.xml") String xmlDefaultResponse;
    @ConfigProperty(name="qvs.response.suffix.json") String jsonResponseSuffix;
    @ConfigProperty(name="qvs.response.defaultfile.json") String jsonDefaultResponse;

    @Override
    public ResponseDTO execute(QVSProcessDTO processDTO) throws Exception {

        Path responsePath = Paths.get(responseDir+processDTO.getServiceIdentifier()+(
                processDTO.isRestRequest()?jsonResponseSuffix:xmlResponseSuffix));
        if (!Files.exists(responsePath)){
            responsePath = Paths.get(responseDir+(processDTO.isRestRequest()?jsonDefaultResponse:xmlDefaultResponse));
        }

        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setResponseBody(new String(Files.readAllBytes(responsePath)));
        return responseDTO;
    }
}
