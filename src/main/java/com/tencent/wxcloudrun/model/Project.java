package com.tencent.wxcloudrun.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("Project")
public class Project implements Serializable {

    private Integer id;

    private String name;
    private String icon;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
