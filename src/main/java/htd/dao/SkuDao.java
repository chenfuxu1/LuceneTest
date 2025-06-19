package htd.dao;

import htd.bean.SkuBean;

import java.util.List;

public interface SkuDao {
    /**
     * 查询所有的 SkuBean 数据
     * @return
     **/
    public List<SkuBean> querySkuList();
}
