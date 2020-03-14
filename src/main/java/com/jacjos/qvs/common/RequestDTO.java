package com.jacjos.qvs.common;

import javax.ws.rs.core.HttpHeaders;

public class RequestDTO {

    private String uri;
    private String requestBody;
    private String httpMethod;
    private HttpHeaders reqHeaders;
    private String contentType;

    @Override
    public String toString() {
        return "RequestDTO{" +
                "uri='" + uri + '\'' +
                ", requestBody='" + requestBody + '\'' +
                ", httpMethod='" + httpMethod + '\'' +
                ", reqHeaders=" + reqHeaders +
                ", contentType='" + contentType + '\'' +
                '}';
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public HttpHeaders getReqHeaders() {
        return reqHeaders;
    }

    public void setReqHeaders(HttpHeaders reqHeaders) {
        this.reqHeaders = reqHeaders;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
