package com.jacjos.qvs.service;

import com.jacjos.qvs.common.RequestDTO;
import com.jacjos.qvs.common.ResponseDTO;

public interface QVSService {

    ResponseDTO processVirtualRequest(RequestDTO requestDTO);
}
