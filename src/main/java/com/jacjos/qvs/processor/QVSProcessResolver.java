package com.jacjos.qvs.processor;

import com.jacjos.qvs.common.RequestDTO;

public interface QVSProcessResolver {

    QVSProcessDTO resolve(RequestDTO requestDTO);
}
