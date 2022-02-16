package com.tencent.wxcloudrun.controller;

import com.tencent.wxcloudrun.config.ApiResponse;
import com.tencent.wxcloudrun.service.ProjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * counter控制器
 */
@Slf4j
@RequestMapping("/project")
@RestController
public class ProjectController {
  @Resource
  ProjectService projectService;

  @GetMapping(value = "/list")
  ApiResponse list() {
    return ApiResponse.ok(projectService.findAll());
  }

}
