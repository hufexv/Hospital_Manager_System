package com.hospital.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hospital.pojo.Bed;

public interface BedMapper extends BaseMapper<Bed> {//BaseMapper接口为使用MyBatis-Plus的开发者提供了简洁、高效的数据访问层的基础，通过继承此接口，开发者可以快速实现常见的数据操作，极大地提高了开发效率。
    /**
     * 统计今天住院人数
     */
    int bedPeople(String bStart);
}
