package com.hezhu.educenter.mapper;

import com.hezhu.educenter.entity.UcenterMember;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 会员表 Mapper 接口
 * </p>
 *
 * @author testjava
 * @since 2021-03-09
 */
public interface UcenterMemberMapper extends BaseMapper<UcenterMember> {
    //6.后台统计：查询某一天的注册人数
    Integer countRegisterDay(String day);
}
