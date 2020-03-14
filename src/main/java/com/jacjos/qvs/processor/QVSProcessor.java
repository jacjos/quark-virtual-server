package com.jacjos.qvs.processor;

import com.jacjos.qvs.common.ResponseDTO;

public interface QVSProcessor {

    ResponseDTO execute(QVSProcessDTO processDTO) throws Exception;
}
