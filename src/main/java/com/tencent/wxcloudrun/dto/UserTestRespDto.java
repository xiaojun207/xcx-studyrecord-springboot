package com.tencent.wxcloudrun.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tencent.wxcloudrun.model.UserTest;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class UserTestRespDto implements Serializable {

    private Integer uid;
    private Integer pId;
    private Double r;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime t;

    public UserTestRespDto(UserTest userTest){
        this.uid = userTest.getUid();
        this.pId = userTest.getProjectId();
        this.r = userTest.getResult();
        this.t = userTest.getCreatedAt();
    }
}
