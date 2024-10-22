package com.hospital.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hospital.pojo.Arrange;
import org.apache.ibatis.annotations.Param;

import java.util.List;

//这个Mapper接口用于与排班相关的数据操作，提供了一个根据时间和科室查询排班信息的方法。由于继承了BaseMapper，可以方便地进行其他常见的数据库操作。

public interface ArrangeMapper extends BaseMapper<Arrange> {

    /**
     * 根据日期查询排班信息
     */
    List<Arrange> findByTime(@Param("ar_time") String arTime, @Param("d_section") String dSection);

}
