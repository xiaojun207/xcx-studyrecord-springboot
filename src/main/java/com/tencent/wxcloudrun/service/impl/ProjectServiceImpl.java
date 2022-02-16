package com.tencent.wxcloudrun.service.impl;

import com.tencent.wxcloudrun.dao.ProjectMapper;
import com.tencent.wxcloudrun.model.Project;
import com.tencent.wxcloudrun.service.ProjectService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Resource
    ProjectMapper projectMapper;

    @Override
    public List<Project> findAll(){
        return projectMapper.findAll();
    }

}
