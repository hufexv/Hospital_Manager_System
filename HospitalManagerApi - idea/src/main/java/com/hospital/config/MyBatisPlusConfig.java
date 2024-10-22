package com.hospital.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
//import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
//import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//这个配置类用于注册MyBatis-Plus的分页和乐观锁插件，帮助简化数据库操作并增强功能。通过这些插件，开发者可以轻松实现分页查询和并发更新处理，提高应用的性能和数据一致性。
@Configuration
public class MyBatisPlusConfig {
    /**
     * 注册插件
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        // 添加分页插件
        PaginationInnerInterceptor pageInterceptor = new PaginationInnerInterceptor();
        // 设置数据库类型
        pageInterceptor.setDbType(DbType.MYSQL);

        interceptor.addInnerInterceptor(pageInterceptor);

        // 乐观锁插件
        OptimisticLockerInnerInterceptor optimisticLockerInnerInterceptor = new OptimisticLockerInnerInterceptor();
        interceptor.addInnerInterceptor(optimisticLockerInnerInterceptor);
        return interceptor;
    }
    /**
     * 配置分页插件
     */
//    @Bean
//    public PaginationInterceptor paginationInterceptor(){
//        return new PaginationInterceptor();
//    }
    /**
     * 配置乐观锁插件
     */
//    @Bean
//    public OptimisticLockerInterceptor optimisticLockerInterceptor(){
//        return new OptimisticLockerInterceptor();
//    }


}
