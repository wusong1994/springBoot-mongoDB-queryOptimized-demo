package com.ws.app.request.mongo;

import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * @desc mongo节点上报分页查询
 * @author ws
 * @since 2021/1/14
 */
public class NodeReportInfoPageRequest implements Serializable {

    private static final long serialVersionUID = 2266336842598690895L;
    /**
     * 节点ID/设备ID
     */
    private String nodeId;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 当前页查询点，翻页需要传（include）
     */
    private String startId;

    /**
     * true 向前翻页：false向后翻页
     */
    private boolean pageUpStatus;

    /**
     * 集合名称
     */
    private String collectionName;

    /**
     * 分页大小
     */
    private int pageSize = 1000;

    /**
     * 排序方式 asc or desc, 默认升序
     */
    private String orderBy = NodeReportInfoPageRequest.ASC_ORDER;

    public final static String ASC_ORDER = "asc";

    public final static String DESC_ORDER = "desc";

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * 根据查询条件，推导出当前查询起点
     * 注意：翻页，此方法挺重要！！！！
     * @return
     */
    public String getCurrentStartId() {
        String currentStartId = null;
        if (StringUtils.isEmpty(startTime)) {
            //为空，设置开始值：时间取最小即可
            currentStartId = nodeId + ".0";
        } else {
            currentStartId = nodeId + "." + startTime.getTime() / 1000;
        }
        // 根据查询点startId和翻页方式调整当前查询起点
        if (!StringUtils.isEmpty(startId)) {
            if (pageUpStatus) {
               // 向前翻页 （跟向后翻页刚好相反）
                if (NodeReportInfoPageRequest.ASC_ORDER.equals(orderBy)) {

                } else {
                    currentStartId = startId;
                }
            } else {
                // 向后翻页
                if (NodeReportInfoPageRequest.ASC_ORDER.equals(orderBy)) {
                    // 升序； 下一页调整起始查询点
                    currentStartId = startId;
                } else {
                    // 降序，不需要调整起始查询点；要调整结束查询点

                }
            }
        }
        return currentStartId;
    }

    public void setStartId(String startId) {
        this.startId = startId;
    }

    /**
     * 根据查询条件，推导出当前查询结束点
     * 注意：翻页，此方法挺重要！！！！
     * @return
     */
    public String getCurrentEndId() {
        String currentEndId = null;
        if (StringUtils.isEmpty(endTime)) {
            //为空，设置结束值：取当前时间
            endTime = new Date();
        }
        currentEndId = nodeId + "." + endTime.getTime() / 1000;
        // 根据查询点startId和翻页方式调整当前查询结束点
        if (!StringUtils.isEmpty(startId)) {
            if (pageUpStatus) {
                // 向前翻页 （跟向后翻页刚好相反）
                if (NodeReportInfoPageRequest.ASC_ORDER.equals(orderBy)) {
                    currentEndId = startId;
                } else {

                }
            } else {
                // 向后翻页
                if (NodeReportInfoPageRequest.ASC_ORDER.equals(orderBy)) {
                    // 升序；不需要调整结束查询点
                } else {
                    // 降序，不需要调整起始查询点；要调整结束查询点
                    currentEndId = startId;
                }
            }
        }
        return currentEndId;
    }

    public boolean isPageUpStatus() {
        return pageUpStatus;
    }

    public void setPageUpStatus(boolean pageUpStatus) {
        this.pageUpStatus = pageUpStatus;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
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

    public String getStartId() {
        return startId;
    }



    @Override
    public String toString() {
        return "NodeReportInfoPageRequest{" +
                "nodeId='" + nodeId + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", pageSize=" + pageSize +
                ", startId='" + startId + '\'' +
                ", pageUpStatus=" + pageUpStatus +
                ", collectionName='" + collectionName + '\'' +
                ", orderBy='" + orderBy + '\'' +
                '}';
    }
}
