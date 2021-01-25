package com.ws.app.request.mongo;

import java.io.Serializable;
import java.util.Date;

/**
 * mongo 节点最新记录获取请求
 */
public class NodeLatestReportInfoRequest implements Serializable {

    private static final long serialVersionUID = -4917665898610140011L;
    /**
     * 节点ID/设备ID
     */
    private String nodeId;

    /**
     * 集合名称
     */
    private String collectionName;

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    /**
     * 获取查询起点集合主键
     * @return
     */
    public String getId() {
        // 取当前时间
        return nodeId + "." + (new Date()).getTime() / 1000;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }
}
