package com.aquare;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author dengtao aquare@163.com
 * @createAt 2022/2/23
 */
@SpringBootApplication
@MapperScan(basePackages = "com.aquare.dao") // 数据连接对象扫描目录
public class App 
{
    public static void main( String[] args )
    {
        SpringApplication.run(com.aquare.App.class, args);
    }
}
