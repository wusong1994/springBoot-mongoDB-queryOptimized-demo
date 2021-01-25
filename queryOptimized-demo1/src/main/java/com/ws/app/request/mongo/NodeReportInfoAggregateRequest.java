package com.ws.app.request.mongo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @desc 节点上报数据聚合请求
 * @author ws
 * @since 2021/1/14
 */
public class NodeReportInfoAggregateRequest implements Serializable {
    private static final long serialVersionUID = 2774628087784386158L;

    /**
     * 节点ID/设备ID
     */
    private String nodeId;

    /**
     * 集合名称
     */
    private String collectionName;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 属性字段标识符
     */
    private List<String> keyList;

    /**
     * 聚合方式 max, min, avg
     */
    private List<String> aggregateWayList;

    //聚合方式常量
    public final static String[] AGGREGATE_WAY_ARR = {"max", "min", "avg"};

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public List<String> getKeyList() {
        return keyList;
    }

    public void setKeyList(List<String> keyList) {
        this.keyList = keyList;
    }

    public List<String> getAggregateWayList() {
        return aggregateWayList;
    }

    public void setAggregateWayList(List<String> aggregateWayList) {
        this.aggregateWayList = aggregateWayList;
    }
}
