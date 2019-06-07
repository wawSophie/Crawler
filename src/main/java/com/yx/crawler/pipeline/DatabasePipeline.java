package com.yx.crawler.pipeline;

import com.yx.crawler.common.Page;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.yx.crawler.common.PoetryInfo;

/**
 * Author:Sophie
 * Created: 2019/6/6
 */
public class DatabasePipeline implements Pipeline {
    private final DataSource dataSource;

    public DatabasePipeline(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    @Override
    public void pipeline( Page page) {
        PoetryInfo poetryInfo=new PoetryInfo();
        String dynasty=(String) page.getDataSet().getData("dynasty");

       String author= (String) page.getDataSet().getData("author");
        String title=(String) page.getDataSet().getData("title");
        String content=(String) page.getDataSet().getData("content");
        //?占位符
        String sql ="insert into poetry_info(title, dynasty, author, content) VALUES (?,?,?,?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement=connection.prepareStatement(sql)
        ) {
            statement.setString(1,title);
            statement.setString(2,dynasty);
            statement.setString(3,author);
            statement.setString(4,content);
            statement.executeUpdate();//执行更新

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
