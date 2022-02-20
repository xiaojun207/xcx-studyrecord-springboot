package com.tencent.wxcloudrun.model;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class PreFamily implements Serializable {
    private Integer id;

    private Integer headUid;
    private Integer memberUid;
    private int status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
