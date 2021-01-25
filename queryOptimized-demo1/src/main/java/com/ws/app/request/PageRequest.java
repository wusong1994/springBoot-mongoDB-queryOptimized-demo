package com.ws.app.request;

/**
 * 分页请求
 */
public class PageRequest {
    /**
     * 当前页码
     */
    private int pageNum;
    /**
     * 每页数量
     */
    private int pageSize;

    public int getPageNum() {
        return pageNum > 0 ? pageNum : 1;
    }
    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }
    public int getPageSize() {
        return pageSize > 0 ? pageSize : 10;
    }
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}