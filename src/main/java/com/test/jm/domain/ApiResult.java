package com.test.jm.domain;

import com.test.jm.dto.test.ApiDTO;

import java.util.List;

public class ApiResult {
    private String status;
    private String msg;
    private List<ApiDTO> apiDTOList;

    public ApiResult() {
    }

    public ApiResult(String status, String msg, List<ApiDTO> apiDTOList) {
        this.status = status;
        this.msg = msg;
        this.apiDTOList = apiDTOList;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<ApiDTO> getApiDTOList() {
        return apiDTOList;
    }

    public void setApiDTOList(List<ApiDTO> apiDTOList) {
        this.apiDTOList = apiDTOList;
    }

    @Override
    public String toString() {
        return "ApiResult{" +
                "status='" + status + '\'' +
                ", msg='" + msg + '\'' +
                ", apiDTOList=" + apiDTOList +
                '}';
    }
}
