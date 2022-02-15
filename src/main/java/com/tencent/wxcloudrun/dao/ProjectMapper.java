package com.tencent.wxcloudrun.dao;

import com.tencent.wxcloudrun.model.Project;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ProjectMapper {

  @Select("SELECT `id`, `name`, `icon`, `createdAt`, `updatedAt` FROM Project order by id desc")
  List<Project> findAll();

}
