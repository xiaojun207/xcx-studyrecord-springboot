package com.tencent.wxcloudrun.dao;

import com.tencent.wxcloudrun.model.Project;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProjectMapper {

  List<Project> findAll();

}
