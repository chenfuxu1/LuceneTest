package htd.dao;


import htd.bean.SkuBean;
import htd.utils.Sout;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SkuDaoImpl implements SkuDao {
    private static final String TAG = "SkuDaoImpl";

    @Override
    public List<SkuBean> querySkuList() {
        // 数据库链接
        Connection connection = null;
        // 预编译 statement
        PreparedStatement preparedStatement = null;
        // 结果集
        ResultSet resultSet = null;
        // 商品列表
        List<SkuBean> list = new ArrayList<SkuBean>();
        long startTime = System.currentTimeMillis();
        try {
            // 加载数据库驱动
            Class.forName("com.mysql.jdbc.Driver");
            // 连接数据库
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/lucene", "root", "12345");
            String sql = "SELECT * FROM tb_sku";
            // 创建preparedStatement
            preparedStatement = connection.prepareStatement(sql);
            // 获取结果集
            resultSet = preparedStatement.executeQuery();
            // 结果集解析
            while (resultSet.next()) {
                SkuBean sku = new SkuBean();
                sku.setId(resultSet.getString("id"));
                sku.setName(resultSet.getString("name"));
                sku.setSpec(resultSet.getString("spec"));
                sku.setBrandName(resultSet.getString("brand_name"));
                sku.setCategoryName(resultSet.getString("category_name"));
                sku.setImage(resultSet.getString("image"));
                sku.setNum(resultSet.getInt("num"));
                sku.setPrice(resultSet.getInt("price"));
                sku.setSaleNum(resultSet.getInt("sale_num"));
                list.add(sku);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Sout.d(TAG, " list.size() = " + list.size());
        // Sout.d(TAG, "cfx " + list.get(0));
        Sout.d(TAG, "cost time: " + (System.currentTimeMillis() - startTime) + " ms");
        return list;
    }
}
