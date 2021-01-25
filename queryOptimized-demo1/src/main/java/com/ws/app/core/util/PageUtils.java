package com.ws.app.core.util;
import com.github.pagehelper.PageInfo;
import com.ws.app.dto.PageResultDTO;

public class PageUtils {

    /**
     * 将分页信息封装到统一的接口
     * @param pageInfo
     * @return
     */
    public static PageResultDTO getPageResult(PageInfo<?> pageInfo) {
        PageResultDTO pageResult = new PageResultDTO();
        pageResult.setPageNum(pageInfo.getPageNum());
        pageResult.setPageSize(pageInfo.getPageSize());
        pageResult.setTotalSize(pageInfo.getTotal());
        pageResult.setTotalPages(pageInfo.getPages());
        pageResult.setContent(pageInfo.getList());
        return pageResult;
    }
}