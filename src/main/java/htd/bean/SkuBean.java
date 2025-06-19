package htd.bean;

public class SkuBean {
    // 商品主键 id
    private String mId;
    // 商品名称
    private String mName;
    // 价格
    private Integer mPrice;
    // 库存数量
    private Integer mNum;
    // 图片
    private String mImage;
    // 分类名称
    private String mCategoryName;
    // 品牌名称
    private String mBrandName;
    // 规格
    private String mSpec;
    // 销量
    private Integer mSaleNum;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public Integer getPrice() {
        return mPrice;
    }

    public void setPrice(Integer price) {
        this.mPrice = price;
    }

    public Integer getNum() {
        return mNum;
    }

    public void setNum(Integer num) {
        this.mNum = num;
    }

    public String getImage() {
        return mImage;
    }

    public void setImage(String image) {
        this.mImage = image;
    }

    public String getCategoryName() {
        return mCategoryName;
    }

    public void setCategoryName(String categoryName) {
        this.mCategoryName = categoryName;
    }

    public String getBrandName() {
        return mBrandName;
    }

    public void setBrandName(String brandName) {
        this.mBrandName = brandName;
    }

    public String getSpec() {
        return mSpec;
    }

    public void setSpec(String spec) {
        this.mSpec = spec;
    }

    public Integer getSaleNum() {
        return mSaleNum;
    }

    public void setSaleNum(Integer saleNum) {
        this.mSaleNum = saleNum;
    }

    @Override
    public String toString() {
        return "SkuBean{" +
                "mId='" + mId + '\'' +
                ", mName='" + mName + '\'' +
                ", mPrice=" + mPrice +
                ", mNum=" + mNum +
                ", mImage='" + mImage + '\'' +
                ", mCategoryName='" + mCategoryName + '\'' +
                ", mBrandName='" + mBrandName + '\'' +
                ", mSpec='" + mSpec + '\'' +
                ", mSaleNum=" + mSaleNum +
                '}';
    }
}
