package com.jacjos.qvs.common;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

public class ResponseDTO {

    private String responseBody;
    private MultivaluedMap<String, String> responseHeaders;
    private int status;
    private Response response;

    @Override
    public String toString() {
        return "ResponseDTO{" +
                "responseBody='" + responseBody + '\'' +
                ", responseHeaders=" + responseHeaders +
                ", status=" + status +
                ", response=" + response +
                '}';
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public MultivaluedMap<String, String> getResponseHeaders() {
        return responseHeaders;
    }

    public void setResponseHeaders(MultivaluedMap<String, String> responseHeaders) {
        this.responseHeaders = responseHeaders;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }
}
