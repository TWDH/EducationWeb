package com.hezhu.mpdemo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hezhu.mpdemo.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
