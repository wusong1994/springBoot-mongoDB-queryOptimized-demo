package com.ws.app.service.impl;

import cn.hutool.core.date.DateUtil;
import com.ws.app.dto.mongo.NodeReportInfoPageDTO;
import com.ws.app.request.mongo.NodeLatestReportInfoRequest;
import com.ws.app.request.mongo.NodeReportInfoAggregateRequest;
import com.ws.app.request.mongo.NodeReportInfoPageRequest;
import com.ws.app.service.MongoDBService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @desc mongoDB 存储、查询，统一操作服务实现类
 * @author ws
 * @since 2021/1/13
 */
@Service
public class MongoDBServiceImpl implements MongoDBService {

    private final static Logger log = LoggerFactory.getLogger(MongoDBServiceImpl.class);

    // 聚合方式
    private final String aggregateAliasFormat = "%s_%s";

    public final static String AGGREGATE_WAY_MAX  = "max";
    public final static String AGGREGATE_WAY_MIN  = "min";
    public final static String AGGREGATE_WAY_AVG  = "avg";

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 存储Map对象到mongoDB中： _id = nodeId + "." + 时间（秒）
     * @param saveMap
     * @param collectionName
     */
    @Override
    public void putMapObj(Map<String, Object> saveMap, String collectionName) {
        // 统一设置入库时间
        saveMap.put("createTime", new Date());
        // 插入记录
        //mongoTemplate.save(data);
        // 一次性插入一整个列表，而不用进行遍历操作，效率相对较高
        mongoTemplate.insert(saveMap, collectionName);
    }

    @Override
    public Map<String, Object> findById(String id, String collectionName) {
        return mongoTemplate.findById(id, Map.class, collectionName);
    }

    /**
     * 根据dcp节点获取最新上报记录
     * @param nodeLatestReportInfoRequest
     * @return
     */
    @Override
    public Map<String, Object> getLatestDcpNodeReportedInfoByNodeId(NodeLatestReportInfoRequest nodeLatestReportInfoRequest) {
        // 拼接完整ID: _id = nodeId + "." + 时间（秒）
        // String _id = nodeId + "." + (new Date()).getTime() / 1000;
        Criteria criteria = Criteria.where("_id").lte(nodeLatestReportInfoRequest.getId());
        // 取最新的记录
        Query query = new Query().addCriteria(criteria)
                .with(Sort.by(
                Sort.Order.desc("_id")
        )).limit(1);
        Map<String, Object> returnMap = mongoTemplate.findOne(query, Map.class, nodeLatestReportInfoRequest.getCollectionName());
        // 填充测量时间：时间转换
        if (returnMap != null) {
            String _id = (String) returnMap.get("_id");
            returnMap.put("measureDate", getMeasureDateById(_id));
        }
        return returnMap;
    }

    /**
     * 根据主键，集合名称分页获取上报信息
     * @param reportInfoPageRequest
     * @return
     */
    @Override
    public NodeReportInfoPageDTO getDcpNodeReportedInfoPage(NodeReportInfoPageRequest reportInfoPageRequest) {
        Query query = new Query();
        Criteria criteria = Criteria.where("_id")
                .gte(reportInfoPageRequest.getCurrentStartId())
                .lte(reportInfoPageRequest.getCurrentEndId());
        query.addCriteria(criteria);
        if (reportInfoPageRequest.getOrderBy().equals(NodeReportInfoPageRequest.ASC_ORDER)) {
            if (!reportInfoPageRequest.isPageUpStatus()) {
                // 正常向后翻，不用考虑过多
                // 升序
                query.with(Sort.by(Sort.Order.asc("_id")));
            } else {
                // 向前翻特殊处理：需要跟正常后翻的排序方式相反， 结果返回（倒序）时需要还得手动排序（正序）
                query.with(Sort.by(Sort.Order.desc("_id")));
            }

        } else {
            if (!reportInfoPageRequest.isPageUpStatus()) {
                // 正常向后翻，不用考虑过多
                // 降序
                query.with(Sort.by(Sort.Order.desc("_id")));
            } else {
                // 向前翻特殊处理：需要跟正常后翻的排序方式相反， 结果返回（正序）时需要还得手动排序（倒序）
                query.with(Sort.by(Sort.Order.asc("_id")));
            }
        }
        // 分页大小
        query.limit(reportInfoPageRequest.getPageSize());
        List<Map> mapList = mongoTemplate.find(query, Map.class, reportInfoPageRequest.getCollectionName());
        NodeReportInfoPageDTO nodeReportInfoPageDTO = new NodeReportInfoPageDTO();
        nodeReportInfoPageDTO.setDataList(mapList);
        int listSize = mapList.size();
        nodeReportInfoPageDTO.setRecordTotal(listSize);
        if (listSize > 0) {
            if (reportInfoPageRequest.isPageUpStatus()) {
                // 向前翻特殊处理： 返回结果需要反转
                Collections.reverse(mapList);
            }
            Map nextMap = mapList.get(listSize - 1);
            // 返回下次查询起点 （优化设计：用主键方式查询分页，可以避免数据量过大、查询过慢的问题）
            String nextKey = nextMap.get("_id").toString();
            Map preMap = mapList.get(0);
            String preKey = preMap.get("_id").toString();
            // 需要根据 排序方式： 设置前一个点、后一个点
            if (NodeReportInfoPageRequest.ASC_ORDER.equals(reportInfoPageRequest.getOrderBy())) {
                // 向后翻：下次查询点：时间戳加一秒
                nodeReportInfoPageDTO.setNextStartId(getQueryNextStartId(nextKey, 1L));
                // 向前翻：下次查询点：时间戳减一秒
                nodeReportInfoPageDTO.setPrevStartId(getQueryNextStartId(preKey, -1L));
            } else {
                // 降序，NextStartId 跟 PrevStartId反转
                nodeReportInfoPageDTO.setNextStartId(getQueryNextStartId(nextKey, -1L));
                nodeReportInfoPageDTO.setPrevStartId(getQueryNextStartId(preKey, 1L));
            }
            //填充测量时间：时间转换
            mapList.parallelStream().forEach(map -> {
                String _id = (String) map.get("_id");
                map.put("measureDate", getMeasureDateById(_id));
            });
            //没数据返回即结束翻页
        }
        return nodeReportInfoPageDTO;
    }

    /**
     * 根据dcp节点、集合获取属性最高、最低、均值
     * @param aggregateRequest
     * @return
     */
    @Override
    public Map<String, Object> getDcpNodeReportedInfoAggregationData(NodeReportInfoAggregateRequest aggregateRequest) {

        String currentStartId = null;
        String currentEndId = null;
        if (StringUtils.isEmpty(aggregateRequest.getStartTime())) {
            //为空，设置开始值：时间取最小即可
            currentStartId = aggregateRequest.getNodeId() + ".0";
        } else {
            currentStartId = aggregateRequest.getNodeId() + "." + aggregateRequest.getStartTime().getTime() / 1000;
        }
        if (StringUtils.isEmpty(aggregateRequest.getEndTime())) {
            //为空，设置结束值：取当前时间
            currentEndId = aggregateRequest.getNodeId() + "." + (new Date()).getTime() / 1000;
        } else {
            currentEndId = aggregateRequest.getNodeId() + "." + aggregateRequest.getEndTime().getTime() / 1000;
        }
        // 设置返回属性列表
        List<String> outputAttrNameList = new ArrayList<>();
        GroupOperation groupOperation = Aggregation.group("nodeId"); //分组， 设置一个不存在的字段即可统计该查询记录下的全部数据
        // 根据属性名、集合函数，返回别名 -> 动态拼接需要分组聚合统计的属性
        // 例如下面：注释部分
        /*groupOperation = groupOperation.max("temp").as("max_temp");
        groupOperation = groupOperation.min("temp").as("min_temp");
        groupOperation = groupOperation.avg("temp").as("avg_temp");
        String[] names = {"max_temp", "min_temp", "avg_temp"};*/
        for (String item : aggregateRequest.getKeyList()) {
            // 该属性需要进行何种聚合统计
            for (int i = 0; i< NodeReportInfoAggregateRequest.AGGREGATE_WAY_ARR.length; i++) {
                String aggregateWay = NodeReportInfoAggregateRequest.AGGREGATE_WAY_ARR[i];
                if (aggregateRequest.getAggregateWayList().contains(aggregateWay)) {
                    // 该属性按设置的所有聚合方式进行聚合统计
                    String attrAlias = String.format(aggregateAliasFormat, aggregateWay, item);
                    outputAttrNameList.add(attrAlias);
                    switch (aggregateWay) {
                        case MongoDBServiceImpl.AGGREGATE_WAY_AVG :
                            groupOperation = groupOperation.avg(item).as(attrAlias);
                            break;
                        case MongoDBServiceImpl.AGGREGATE_WAY_MAX :
                            groupOperation = groupOperation.max(item).as(attrAlias);
                            break;
                        case MongoDBServiceImpl.AGGREGATE_WAY_MIN :
                            groupOperation = groupOperation.min(item).as(attrAlias);
                            break;
                        default:
                    }
                }
            }
        }
        Aggregation aggregation = Aggregation.newAggregation(Aggregation.match(Criteria.where("_id")
                .gt(currentStartId)
                .lte(currentEndId)),
                groupOperation,
                Aggregation.project(outputAttrNameList.toArray(new String[outputAttrNameList.size()])).and("nodeId").previousOperation() // 用nodeId字段 替换调返回的 _id字段
                );
        AggregationResults<Map> results = mongoTemplate.aggregate(aggregation, aggregateRequest.getCollectionName(), Map.class);
        int returnSize = results.getMappedResults().size();
        log.info("results size= {}", returnSize);
        Map returnMap = null;
        if (returnSize > 0) {
            returnMap = results.getMappedResults().get(0);
        } else {
            returnMap = new HashMap();
        }
        returnMap.put("nodeId", aggregateRequest.getNodeId());
        return returnMap;
    }

    /**
     * 返回翻页下一个查询起点
     * @param idKey _id主键
     * @param addNum 加秒数
     * @return
     */
    private String getQueryNextStartId(String idKey, Long addNum) {
        String[] keyArr = idKey.split("\\.");
        String nodeId = keyArr[0];
        String timeStampStr = keyArr[1];
        String nextStartId = nodeId + "." + (Long.valueOf(timeStampStr) + addNum);
        log.info("nodeId={},timeStampStr={}, nextStartId={}", nodeId, timeStampStr, nextStartId);
        return nextStartId;
    }

    /**
     * 根据_id返回上报时间（测量时间）
     * @param idKey
     * @return String
     */
    private String getMeasureDateStrById(String idKey) {
        Date measureDate = getMeasureDateById(idKey);
        return DateUtil.format(measureDate, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 根据_id返回上报时间（测量时间）
     * @param idKey
     * @return Date
     */
    private Date getMeasureDateById(String idKey) {
        String[] idArr = idKey.split("\\.");
        Date measureDate = new Date(Long.valueOf(idArr[1]) * 1000L);
        return measureDate;
    }
}
