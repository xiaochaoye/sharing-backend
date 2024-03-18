package com.chao.share.testtools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author 超
 */
public class InsertUserLittle{

    public static void main(String[] args) {
        // 1. 加载MySQL Connector/J驱动程序
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("找不到MySQL驱动程序");
            e.printStackTrace();
            return;
        }

        // 2. 建立连接
        String url = "jdbc:mysql://localhost:3306/sharing?serverTimezone=Asia/Shanghai";
        String username = "root";
        String password = "123456";
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            System.out.println("成功连接到MySQL数据库");

            // 3. 执行查询
            try (Statement statement = connection.createStatement()) {
                String sql = "SELECT * FROM user";
                try (ResultSet resultSet = statement.executeQuery(sql)) {
                    while (resultSet.next()) {
                        System.out.println("列1: " + resultSet.getString(1) + ", 列2: " + resultSet.getString(2));
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("连接失败");
            e.printStackTrace();
        }
    }
}
