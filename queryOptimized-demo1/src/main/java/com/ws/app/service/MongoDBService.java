package com.ws.app.service;


import com.ws.app.dto.mongo.NodeReportInfoPageDTO;
import com.ws.app.request.mongo.NodeLatestReportInfoRequest;
import com.ws.app.request.mongo.NodeReportInfoAggregateRequest;
import com.ws.app.request.mongo.NodeReportInfoPageRequest;

import java.util.Map;

/**
 * @desc mongoDB 存储、查询，统一操作服务类
 * @author ws
 * @since 2021/1/13
 */
public interface MongoDBService {

    /**
     * 存储Map对象到mongoDB集合中： _id = nodeId + "." + 时间（秒）
     * @param saveMap
     * @param collectionName
     */
    void putMapObj(Map<String, Object> saveMap, String collectionName);

    /**
     * 根据主键获取集合记录
     * @param id
     * @param collectionName
     * @return
     */
    Map<String,Object> findById(String id, String collectionName);

    /**
     * 根据dcp节点，集合名称获取最新上报记录
     * @param nodeLatestReportInfoRequest
     * @return
     */
    Map<String,Object> getLatestDcpNodeReportedInfoByNodeId(NodeLatestReportInfoRequest nodeLatestReportInfoRequest);

    /**
     * 根据dcp节点，集合名称分页获取上报信息
     * @param reportInfoPageRequest
     * @return
     */
    NodeReportInfoPageDTO getDcpNodeReportedInfoPage(NodeReportInfoPageRequest reportInfoPageRequest);

    /**
     * 根据dcp节点、集合获取属性最高、最低、均值
     * @param aggregateRequest
     * @return
     */
    Map<String,Object> getDcpNodeReportedInfoAggregationData(NodeReportInfoAggregateRequest aggregateRequest);
}
