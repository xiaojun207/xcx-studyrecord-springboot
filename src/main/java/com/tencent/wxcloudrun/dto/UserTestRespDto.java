package com.tencent.wxcloudrun.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tencent.wxcloudrun.model.UserTest;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Data
public class UserTestRespDto implements Serializable {

    private Integer id;
    private Integer uid;
    private Integer pId;
    private Double r;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime t;
    private Long ts;

    public UserTestRespDto(UserTest userTest){
        this.id = userTest.getId();
        this.uid = userTest.getUid();
        this.pId = userTest.getProjectId();
        this.r = userTest.getResult();
        this.t = userTest.getCreatedAt();
        this.ts = userTest.getCreatedAt().toInstant(ZoneOffset.of("+8")).toEpochMilli();;
    }
}
