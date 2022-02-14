package com.tencent.wxcloudrun.model;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Family implements Serializable {

    private Integer id;

    private String name;
    private Integer headUid;
    private Integer memberUid;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
