package com.jacjos.qvs.processor;

import com.jacjos.qvs.common.RequestDTO;

public class QVSProcessDTO {

    private String serviceIdentifier;
    private String requestBody;
    private String processor;
    private String proxy;
    private RequestDTO requestDTO;
    private boolean isRestRequest;

    public String getServiceIdentifier() {
        return serviceIdentifier;
    }

    public void setServiceIdentifier(String serviceIdentifier) {
        this.serviceIdentifier = serviceIdentifier;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public String getProcessor() {
        return processor;
    }

    public void setProcessor(String processor) {
        this.processor = processor;
    }

    public String getProxy() {
        return proxy;
    }

    public void setProxy(String proxy) {
        this.proxy = proxy;
    }

    public RequestDTO getRequestDTO() {
        return requestDTO;
    }

    public void setRequestDTO(RequestDTO requestDTO) {
        this.requestDTO = requestDTO;
    }

    public boolean isRestRequest() {
        return isRestRequest;
    }

    public void setRestRequest(boolean restRequest) {
        isRestRequest = restRequest;
    }
}
