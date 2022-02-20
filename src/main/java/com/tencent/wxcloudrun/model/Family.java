package com.tencent.wxcloudrun.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("Family")
public class Family implements Serializable {

    private Integer id;

    private String name;
    private Integer headUid;
    private Integer memberUid;
    private int status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
