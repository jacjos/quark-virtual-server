package com.jacjos.qvs;

import com.jacjos.qvs.common.RequestDTO;
import com.jacjos.qvs.common.ResponseDTO;
import com.jacjos.qvs.service.QVSService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

@Path("/")
public class QVSResource {

    private Logger log = LoggerFactory.getLogger(QVSResource.class);

    @Inject QVSService qvsService;

    @POST @Consumes({MediaType.TEXT_XML, MediaType.APPLICATION_XML}) @Produces(MediaType.TEXT_XML)
    public Response virtualizeXML(String reqBody, @HeaderParam("Content-Type") String contentType, @Context HttpHeaders headers){

        log.info("**XML REQUEST ** {}\n ***", reqBody);

        RequestDTO requestDTO = new RequestDTO();
        requestDTO.setRequestBody(reqBody);
        requestDTO.setReqHeaders(headers);
        return buildResponse(qvsService.processVirtualRequest(requestDTO));
    }

    @GET @POST @Path("/rest/{uri: .+}")
    @Consumes(MediaType.APPLICATION_JSON) @Produces(MediaType.APPLICATION_JSON)
    public Response virtualizeJson(String reqJson, @PathParam("uri") String uri, @Context HttpHeaders headers, @Context Request request){
        RequestDTO requestDTO = new RequestDTO();
        requestDTO.setHttpMethod(request.getMethod());
        requestDTO.setReqHeaders(headers);
        requestDTO.setUri(uri);
        requestDTO.setRequestBody(reqJson);
        requestDTO.setContentType(MediaType.APPLICATION_JSON);
        log.info("JSON requestDTO = {}", requestDTO);
        return buildResponse(qvsService.processVirtualRequest(requestDTO));
    }

    private  Response buildResponse(ResponseDTO responseDTO){
        Response.ResponseBuilder responseBuilder = responseDTO.getStatus() == 0
                                                                    ? Response.ok()
                                                                    : Response.status(responseDTO.getStatus());
        if (responseDTO.getResponseHeaders() != null){
            responseDTO.getResponseHeaders().entrySet().stream()
                    .filter(entry -> !"Transfer-Encoding".equalsIgnoreCase(entry.getKey()))
                    .forEach(entry -> responseBuilder.header(entry.getKey(),entry.getValue().get(0)));
        }
        return responseBuilder.entity(responseDTO.getResponseBody()).build();
    }
}