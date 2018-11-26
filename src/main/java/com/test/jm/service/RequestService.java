package com.test.jm.service;

import com.test.jm.domain.HttpClientResult;
import com.test.jm.dto.test.ApiDTO;
import com.test.jm.keys.RequestType;
import com.test.jm.util.RequestUtils;
import org.springframework.stereotype.Service;


@Service
public class RequestService {

    public HttpClientResult request(ApiDTO apiDTO) throws Exception {
        HttpClientResult result = new HttpClientResult();
        switch (apiDTO.getMethod().toUpperCase()){
            case RequestType.GET :
                result = RequestUtils.doGet(apiDTO.getUrl(),apiDTO.getHeaders(),apiDTO.getCookies(),apiDTO.getBody(), apiDTO.getParamstype());
                break;
            case RequestType.POST :
                result = RequestUtils.doPost(apiDTO.getUrl(),apiDTO.getHeaders(),apiDTO.getCookies(), apiDTO.getBody(), apiDTO.getParamstype());
                break;
            default:
                break;
        }
        return result;
    }




}
