package com.ws.app.mapper;

import com.ws.app.core.mapper.BaseMapper;
import com.ws.app.core.po.SysUserPo;
import com.ws.app.dto.SysUserDTO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface SysUserMapper extends BaseMapper<SysUserPo> {

    @Select("select * from sys_user where user_id = #{userId}")
    SysUserPo findByUserId(Long userId);

    SysUserDTO selectByUserId(@Param("userId") Long userId);
}
