package com.tencent.wxcloudrun.model;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Project implements Serializable {

    private Integer id;

    private String name;
    private String icon;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
