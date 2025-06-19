package htd.bean;

import java.util.List;


/**
 * 自定义分页实体类
 * 分页使用的
 */
public class ResultModelBean {
    // 商品列表
    private List<SkuBean> mSkuList;
    // 商品总数
    private Long mRecordCount;
    // 总页数
    private Long mPageCount;
    // 当前页
    private long mCurPage;

    public List<SkuBean> getSkuList() {
        return mSkuList;
    }

    public void setSkuList(List<SkuBean> skuList) {
        this.mSkuList = skuList;
    }

    public Long getRecordCount() {
        return mRecordCount;
    }

    public void setRecordCount(Long recordCount) {
        this.mRecordCount = recordCount;
    }

    public Long getPageCount() {
        return mPageCount;
    }

    public void setPageCount(Long pageCount) {
        this.mPageCount = pageCount;
    }

    public long getCurPage() {
        return mCurPage;
    }

    public void setCurPage(long curPage) {
        this.mCurPage = curPage;
    }
}
