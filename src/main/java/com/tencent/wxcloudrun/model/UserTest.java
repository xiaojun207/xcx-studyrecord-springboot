package com.tencent.wxcloudrun.model;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class UserTest implements Serializable {

    private Integer id;

    private Integer uid;
    private Integer projectId;
    private String projectName;
    private Double result;
    private Double score;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
