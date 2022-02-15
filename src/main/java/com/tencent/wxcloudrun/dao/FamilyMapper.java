package com.tencent.wxcloudrun.dao;

import com.tencent.wxcloudrun.model.Family;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface FamilyMapper {

  @Select("SELECT `id`, `headUid`, `memberUid`, `createdAt`, `updatedAt` FROM Family where  memberUid=#{memberUid} order by id desc")
  Family findByUid(@Param("memberUid") Integer memberUid);

  @Select("SELECT `id`, `headUid`, `memberUid`, `createdAt`, `updatedAt` FROM Family where  headUid=#{headUid} order by id desc")
  List<Family> findAll(@Param("headUid") Integer headUid);

}
