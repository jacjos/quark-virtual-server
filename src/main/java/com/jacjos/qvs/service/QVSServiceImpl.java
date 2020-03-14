package com.jacjos.qvs.service;

import com.jacjos.qvs.common.RequestDTO;
import com.jacjos.qvs.common.ResponseDTO;
import com.jacjos.qvs.common.Util;
import com.jacjos.qvs.processor.QVSProcessDTO;
import com.jacjos.qvs.processor.QVSProcessResolver;
import com.jacjos.qvs.processor.QVSProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

@ApplicationScoped
public class QVSServiceImpl implements QVSService {

    Logger log = LoggerFactory.getLogger(QVSServiceImpl.class);

    @Inject QVSProcessResolver processResolver;
    @Inject BeanManager beanManager;

    @Override
    public ResponseDTO processVirtualRequest(RequestDTO requestDTO) {

        QVSProcessDTO processDTO = processResolver.resolve(requestDTO);
        log.debug("Resolved Processor = {}", processDTO.getProcessor());

        ResponseDTO responseDTO;
        QVSProcessor processor = Util.getBean(processDTO.getProcessor(), beanManager, QVSProcessor.class);

        try{
            return processor.execute(processDTO);
        } catch (Exception e){
            log.error("Exception caught while executing processor",e);
            responseDTO = new ResponseDTO();
            responseDTO.setResponseBody("QVS Server Error");
            responseDTO.setStatus(500);
        }
        return responseDTO;
    }
}
