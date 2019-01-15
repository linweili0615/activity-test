package com.test.jm.domain;

import com.test.jm.dto.ApiDTO;

import java.util.List;

public class ApiResult {
    private String status;
    private String msg;
    private long total;
    private long pageSize;
    private long pageCount;
    private long pageNo;
    private List<ApiDTO> apiDTOList;

    public ApiResult() {
    }

    public ApiResult(String status, String msg, List<ApiDTO> apiDTOList) {
        this.status = status;
        this.msg = msg;
        this.apiDTOList = apiDTOList;
    }

    public ApiResult(String status, String msg, long total, long pageSize, long pageCount, long pageNo, List<ApiDTO> apiDTOList) {
        this.status = status;
        this.msg = msg;
        this.total = total;
        this.pageSize = pageSize;
        this.pageCount = pageCount;
        this.pageNo = pageNo;
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

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getPageSize() {
        return pageSize;
    }

    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
    }

    public long getPageCount() {
        return pageCount;
    }

    public void setPageCount(long pageCount) {
        this.pageCount = pageCount;
    }

    public long getPageNo() {
        return pageNo;
    }

    public void setPageNo(long pageNo) {
        this.pageNo = pageNo;
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
                ", total=" + total +
                ", pageSize=" + pageSize +
                ", pageCount=" + pageCount +
                ", pageNo=" + pageNo +
                ", apiDTOList=" + apiDTOList +
                '}';
    }
}
