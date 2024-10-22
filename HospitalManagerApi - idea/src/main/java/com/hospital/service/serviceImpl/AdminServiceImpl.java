package com.hospital.service.serviceImpl;

import com.hospital.mapper.AdminMapper;
import com.hospital.pojo.Admin;
import com.hospital.service.AdminService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

//AdminServiceImpl类实现了管理员登录的业务逻辑，负责校验管理员身份。通过调用Mapper层的方法，连接数据库并执行查询，确保业务逻辑的清晰和分离。

@Service("AdminService")
public class AdminServiceImpl implements AdminService {

    @Resource
    private AdminMapper adminMapper;

    /**
     * 登录数据校验
     * */
    @Override
    public Admin login(int aId, String aPassword){
        Admin admin = this.adminMapper.selectById(aId);
        if (admin == null) {
            return null;
        } else {
            if ((admin.getAPassword()).equals(aPassword)) {
                return admin;
            }
        }
        return null;
    }



}
