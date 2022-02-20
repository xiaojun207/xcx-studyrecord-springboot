package com.tencent.wxcloudrun.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tencent.wxcloudrun.model.PreFamily;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface PreFamilyMapper extends BaseMapper<PreFamily> {

    @Select("SELECT `id`, `headUid`, `memberUid`, `status`, `createdAt`, `updatedAt` FROM PreFamily where  memberUid=#{memberUid} order by id desc limit 1")
    PreFamily findByUid(@Param("memberUid") Integer memberUid);

    @Select("SELECT `id`, `headUid`, `memberUid`, `status`, `createdAt`, `updatedAt` FROM PreFamily where  headUid=#{headUid} order by id desc")
    List<PreFamily> findByHeadUid(@Param("headUid") Integer headUid);

}
