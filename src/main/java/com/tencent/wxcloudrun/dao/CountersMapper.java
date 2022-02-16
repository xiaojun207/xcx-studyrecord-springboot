package com.tencent.wxcloudrun.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tencent.wxcloudrun.model.Counter;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CountersMapper extends BaseMapper<Counter> {

  @Select("SELECT `id`, `count`, `createdAt`, `updatedAt` FROM Counters WHERE id = #{id}")
  Counter getCounter(@Param("id") Integer id);

  @Select("INSERT INTO `Counters`(`id`, `count`) VALUE(#{id}, #{count}) ON DUPLICATE KEY UPDATE count=#{count}")
  void upsertCount(Counter counter);

  @Select("DELETE FROM Counters where id = #{id} limit 1")
  void clearCount(@Param("id") Integer id);
}
