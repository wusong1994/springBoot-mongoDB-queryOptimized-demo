package com.ws.app.controller;

import com.ws.app.core.constants.MongoDBCollectionNameConstant;
import com.ws.app.core.exception.BaseBusinessException;
import com.ws.app.core.web.WebResponse;
import com.ws.app.dto.mongo.NodeReportInfoPageDTO;
import com.ws.app.request.mongo.NodeLatestReportInfoRequest;
import com.ws.app.request.mongo.NodeReportInfoAggregateRequest;
import com.ws.app.request.mongo.NodeReportInfoPageRequest;
import com.ws.app.service.MongoDBService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @desc 节点数据存储、获取控制类
 */
@RestController
@RequestMapping("/dcp/reportQuery")
public class DevicePutAndQueryController {

    private final static Logger log = LoggerFactory.getLogger(DevicePutAndQueryController.class);

    @Autowired
    MongoDBService mongoDBService;


    /**
     * 测试get
     * @return
     */
    @RequestMapping(value = "/dcpNodeReportedInfo/{id}", method = RequestMethod.GET)
    WebResponse testGet(@PathVariable("id") String id) {
        Map<String, Object> map = mongoDBService.findById(id, MongoDBCollectionNameConstant.dcpNodeReportedInfoTable);
        return new WebResponse(map);
    }

    /**
     * 通用接口：根据dcp节点、集合获取最新上报记录
     * @return
     */
    @RequestMapping(value = "/dcpNodeReportedInfo/latest", method = RequestMethod.POST)
    WebResponse getLatestDcpNodeReportedInfoByNodeId(@RequestBody NodeLatestReportInfoRequest nodeLatestReportInfoRequest) {
        if (StringUtils.isEmpty(nodeLatestReportInfoRequest.getNodeId())) {
            throw new BaseBusinessException("nodeId不能为空！");
        }
        if (StringUtils.isEmpty(nodeLatestReportInfoRequest.getCollectionName())) {
            throw new BaseBusinessException("collectionName不能为空！");
        }
        Map<String, Object> map = mongoDBService.getLatestDcpNodeReportedInfoByNodeId(nodeLatestReportInfoRequest);
        return new WebResponse(map);
    }

    /**
     * 通用接口：根据dcp节点、集合分页获取节点上报
     * @return
     */
    @RequestMapping(value = "/dcpNodeReportedInfo/page", method = RequestMethod.POST)
    WebResponse getDcpNodeReportedInfoPage(@RequestBody NodeReportInfoPageRequest reportInfoPageRequest) {
        log.info(reportInfoPageRequest.toString());
        log.info("StartId={}, EndId={}", reportInfoPageRequest.getCurrentStartId(), reportInfoPageRequest.getCurrentEndId());
        if (StringUtils.isEmpty(reportInfoPageRequest.getNodeId())) {
            throw new BaseBusinessException("nodeId不能为空！");
        }
        if (StringUtils.isEmpty(reportInfoPageRequest.getCollectionName())) {
            throw new BaseBusinessException("collectionName不能为空！");
        }
        if (StringUtils.isEmpty(reportInfoPageRequest.getStartId()) && reportInfoPageRequest.isPageUpStatus() ) {
            // startId为空， 不能向前翻页
            throw new BaseBusinessException("startId为空，不能向前翻页！");
        }
        NodeReportInfoPageDTO nodeReportInfoPageDTO = mongoDBService.getDcpNodeReportedInfoPage(reportInfoPageRequest);
        return new WebResponse(nodeReportInfoPageDTO);
    }

    /**
     * 聚合接口：
     * 通用接口：根据dcp节点、集合获取属性最高、最低、均值
     * @return
     */
    @RequestMapping(value = "/dcpNodeReportedInfo/aggregateData", method = RequestMethod.POST)
    WebResponse getDcpNodeReportedInfoAggregationData(@RequestBody NodeReportInfoAggregateRequest aggregateRequest) {
        if (StringUtils.isEmpty(aggregateRequest.getNodeId())) {
            throw new BaseBusinessException("nodeId不能为空！");
        }
        if (StringUtils.isEmpty(aggregateRequest.getCollectionName())) {
            throw new BaseBusinessException("collectionName不能为空！");
        }
        if (Objects.isNull(aggregateRequest.getKeyList()) || aggregateRequest.getKeyList().size() <= 0) {
            throw new BaseBusinessException("keyList数组不能为空！");
        }
        if (Objects.isNull(aggregateRequest.getAggregateWayList()) || aggregateRequest.getAggregateWayList().size() <= 0) {
            throw new BaseBusinessException("aggregateWayList数组不能为空！");
        }
        List<String> aggregateWayList = Arrays.asList(NodeReportInfoAggregateRequest.AGGREGATE_WAY_ARR);
        if (!aggregateWayList.containsAll(aggregateRequest.getAggregateWayList())) {
            throw new BaseBusinessException("存在未知的聚合方式！");
        }
        Map<String, Object> map = mongoDBService.getDcpNodeReportedInfoAggregationData(aggregateRequest);
        return new WebResponse(map);
    }
}
