package com.ws.app.dto.mongo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class NodeReportInfoPageDTO implements Serializable {

    private static final long serialVersionUID = 6049007832482017896L;
    /**
     * 当前页记录总数
     */
    private int recordTotal;

    /**
     * 分页内容
     */
    List<Map> dataList;

    /**
     * 向前翻页：查询起点（include）
     */
    private String prevStartId;

    /**
     * 向后翻页：下次查询起点（include）
     */
    private String nextStartId;

    public int getRecordTotal() {
        return recordTotal;
    }

    public void setRecordTotal(int recordTotal) {
        this.recordTotal = recordTotal;
    }

    public List<Map> getDataList() {
        return dataList;
    }

    public void setDataList(List<Map> dataList) {
        this.dataList = dataList;
    }

    public String getNextStartId() {
        return nextStartId;
    }

    public void setNextStartId(String nextStartId) {
        this.nextStartId = nextStartId;
    }

    public String getPrevStartId() {
        return prevStartId;
    }

    public void setPrevStartId(String prevStartId) {
        this.prevStartId = prevStartId;
    }
}
