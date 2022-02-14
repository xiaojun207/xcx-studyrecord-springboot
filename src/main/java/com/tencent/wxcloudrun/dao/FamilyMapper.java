package com.tencent.wxcloudrun.dao;

import com.tencent.wxcloudrun.model.Family;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FamilyMapper {

  List<Family> findAll(@Param("headUid") Integer headUid);

}
