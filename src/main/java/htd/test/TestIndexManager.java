package htd.test;

import htd.bean.SkuBean;
import htd.dao.SkuDao;
import htd.dao.SkuDaoImpl;
import htd.utils.Sout;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.MMapDirectory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Project: LuceneTest
 * Create By: Chen.F.X
 * DateTime: 2025-06-16 23:13
 * <p>
 * Desc:
 */
public class TestIndexManager {
    private static final String TAG = "TestIndexManager";
    private static final String INDEX_LOCATION = "D:\\F\\LuceneIndex";

    public static void main(String[] args) throws Exception {
        // createIndexTest();
        // createIndexTest2();
        // updateIndexTest();
        // deleteIndexTest();
        // deleteAllIndexTest();
        // createIndexTest3();
        createIndexTest4();
    }

    /**
     * 创建索引库
     */
    public static void createIndexTest() throws IOException {
        // 1.采集数据
        SkuDao skuDao = new SkuDaoImpl();
        List<SkuBean> skuBeanList = skuDao.querySkuList();
        List<Document> documentList = new ArrayList<>();
        for (SkuBean skuBean : skuBeanList) {
            // 2.创建文档对象
            Document document = new Document();
            /**
             * 创建域对象，并且放入文档中
             * 参数 1：域名
             * 参数 2：域值
             * 参数 3：是否存储
             */
            document.add(new TextField("id", skuBean.getId(), Field.Store.YES));
            document.add(new TextField("name", skuBean.getName(), Field.Store.YES));
            document.add(new TextField("price", String.valueOf(skuBean.getPrice()), Field.Store.YES));
            document.add(new TextField("num", String.valueOf(skuBean.getNum()), Field.Store.YES));
            document.add(new TextField("image", skuBean.getImage(), Field.Store.YES));
            document.add(new TextField("categoryName", skuBean.getCategoryName(), Field.Store.YES));
            document.add(new TextField("spec", skuBean.getSpec(), Field.Store.YES));
            document.add(new TextField("saleNum", String.valueOf(skuBean.getSaleNum()), Field.Store.YES));
            // 将文档对象放入文档集合中
            documentList.add(document);
        }
        /**
         * 3.创建分词器对象
         * StandardAnalyzer 是标准分词器，对英文分词效果好
         * 对中文是单字分词，也就是一个字认为是一个词
         */
        Analyzer analyzer = new StandardAnalyzer();
        // 4.创建 Directory 目录对象，目录对象表示索引库的位置
        Directory directory = FSDirectory.open(Paths.get(INDEX_LOCATION));
        // 5.创建 IndexWriterConfig 对象，这个对象中指定切分词使用的分词器
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
        // 6.创建 IndexWriter 输出流对象，指定输出的位置和使用 config 初始化对象
        IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);
        // 7.写入文档到索引库
        for (Document document : documentList) {
            indexWriter.addDocument(document);
        }
        // 8.释放资源
        indexWriter.close();
        Sout.d(TAG, "写入完成");
    }

    /**
     * 创建索引库
     * 使用特定的 Field
     */
    public static void createIndexTest2() throws IOException {
        // 1.采集数据
        SkuDao skuDao = new SkuDaoImpl();
        List<SkuBean> skuBeanList = skuDao.querySkuList();
        List<Document> documentList = new ArrayList<>();
        for (SkuBean skuBean : skuBeanList) {
            // 2.创建文档对象
            Document document = new Document();
            /**
             * 创建域对象，并且放入文档中
             * 参数 1：域名
             * 参数 2：域值
             * 参数 3：是否存储
             */
            /**
             * 是否分词：否，因为主键分词后无意义
             * 是否索引：是，因为会根据主键 id 进行查询，就必须索引
             * 是否存储：是，因为主键 id 比较特殊，可以确定唯一的一条数据，所以存起来
             * 存起来，才能获取到 id 具体的内容显示出来
             * 因为是 N Y Y ，所以查表选用 StringField
             */
            document.add(new StringField("id", skuBean.getId(), Field.Store.YES));
            /**
             * 是否分词：是，因为名称字段需要查询，并且分词后有意义，所以需要分词
             * 是否索引：是，因为需要根据名称字段查询
             * 是否存储：是，需要展示
             * Y Y Y, 选用 TextField
             */
            document.add(new TextField("name", skuBean.getName(), Field.Store.YES));
            /**
             * 是否分词：是（lucene 底层规定，如果根据价格范围查询，必须分词）
             * 是否索引：是，会根据价格进行范围查询，所以必须索引
             * 是否存储：是，因为需要展示价格
             * Y Y Y
             * Y Y N -> 选用 IntPoint，不存储
             * N N Y -> 选用 StoredField，存储
             * 通过这两个配合，完成价格的索引分词
             */
            document.add(new IntPoint("price", skuBean.getPrice())); // 不存储
            document.add(new StoredField("price", skuBean.getPrice())); // 存储
            /**
             * 是否分词：否
             * 是否索引：是
             * 是否存储：是
             * 因为是 N Y Y ，所以查表选用 StringField
             */
            document.add(new StringField("num", String.valueOf(skuBean.getNum()), Field.Store.YES));
            /**
             * 是否分词：否，不需要索引也就不需要分词
             * 是否索引：否，不需要根据图片地址进行查询
             * 是否存储：是，因为页面需要展示商品图片
             * N N Y -> 选用 StoredField
             */
            document.add(new StoredField("image", skuBean.getImage()));
            /**
             * 是否分词：否，因为分类是专有的名词，是一个整体，不需要分词
             * 是否索引：是，因为需要根据分类查询
             * 是否存储：是，页面需要展示分类
             * N Y Y -> StringField
             */
            document.add(new StringField("categoryName", skuBean.getCategoryName(), Field.Store.YES));
            /**
             * 是否分词：否，因为品牌是专有名词，是一个整体
             * 是否索引：是，需要根据品牌进行查询
             * 是否存储：是，页面需要展示
             * N Y Y -> StringField
             */
            document.add(new StringField("brandName", skuBean.getBrandName(), Field.Store.YES));
            document.add(new StringField("spec", skuBean.getSpec(), Field.Store.YES));
            document.add(new StringField("saleNum", String.valueOf(skuBean.getSaleNum()), Field.Store.YES));
            // 将文档对象放入文档集合中
            documentList.add(document);
        }
        /**
         * 3.创建分词器对象
         * StandardAnalyzer 是标准分词器，对英文分词效果好
         * 对中文是单字分词，也就是一个字认为是一个词
         */
        Analyzer analyzer = new StandardAnalyzer();
        // 4.创建 Directory 目录对象，目录对象表示索引库的位置
        Directory directory = FSDirectory.open(Paths.get(INDEX_LOCATION));
        // 5.创建 IndexWriterConfig 对象，这个对象中指定切分词使用的分词器
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
        // 6.创建 IndexWriter 输出流对象，指定输出的位置和使用 config 初始化对象
        IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);
        // 7.写入文档到索引库
        for (Document document : documentList) {
            indexWriter.addDocument(document);
        }
        // 8.释放资源
        indexWriter.close();
        Sout.d(TAG, "写入完成");
    }

    /**
     * 更新索引
     * 更新索引会把当前的数据删除了
     * 然后再在最后的位置重新创建更新后的索引文档
     */
    public static void updateIndexTest() throws IOException {
        // 1.需要变更的文档内容
        Document document = new Document();
        document.add(new StringField("id", "100000003145", Field.Store.YES));
        document.add(new TextField("name", "路飞", Field.Store.YES));
        document.add(new IntPoint("price", 123)); // 不存储
        document.add(new StoredField("price", 123)); // 存储
        document.add(new StoredField("image", "https://www.baidu.com"));
        document.add(new StringField("categoryName", "娜美手机", Field.Store.YES));
        document.add(new StringField("brandName", "索隆手机", Field.Store.YES));
        document.add(new StringField("spec", "哈哈哈", Field.Store.YES));
        document.add(new StringField("saleNum", "123455", Field.Store.YES));
        /**
         * 2.创建分词器对象
         * StandardAnalyzer 标准分词器，对英文分词效果好，对中文是单字分词
         * 也就是一个字就认为是一个词
         */
        Analyzer analyzer = new StandardAnalyzer();
        // 3.创建 Directory 对象，表示索引库的位置
        Directory directory = FSDirectory.open(Paths.get(INDEX_LOCATION));
        // 4.创建 IndexWriterConfig 对象，这个对象指定切分词使用的分词器
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
        // 5.创建 IndexWriter 输出流对象，指定输出的位置和使用的 config 初始化对象
        IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);
        /**
         * 6.修改到索引库
         * 参数 1：修改条件, 修改 id = 100000003145 的文档内容
         * 参数 2：修改的文档内容
         */
        indexWriter.updateDocument(new Term("id", "100000003145"), document);
        // 7.释放资源
        indexWriter.close();
        Sout.d(TAG, "更新索引完成");
    }

    /**
     * 删除单个索引
     * 根据条件删除
     */
    public static void deleteIndexTest() throws IOException {
        /**
         * 1.创建分词器对象
         * StandardAnalyzer 标准分词器，对英文分词效果好，对中文是单字分词
         * 也就是一个字就认为是一个词
         */
        Analyzer analyzer = new StandardAnalyzer();
        // 2.创建 Directory 对象，表示索引库的位置
        Directory directory = FSDirectory.open(Paths.get(INDEX_LOCATION));
        // 3.创建 IndexWriterConfig 对象，这个对象指定切分词使用的分词器
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
        // 4.创建 IndexWriter 输出流对象，指定输出的位置和使用的 config 初始化对象
        IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);
        /**
         * 5.根据条件进行删除索引
         * 参数1：修改条件, 修改id = 100000003145 的文档内容
         * 参数2：修改的文档内容
         */
        indexWriter.deleteDocuments(new Term("id", "100000003145"));
        // 6.释放资源
        indexWriter.close();
        Sout.d(TAG, "删除索引完成");
    }

    /**
     * 删除所有索引
     */
    public static void deleteAllIndexTest() throws IOException {
        /**
         * 1.创建分词器对象
         * StandardAnalyzer 标准分词器，对英文分词效果好，对中文是单字分词
         * 也就是一个字就认为是一个词
         */
        Analyzer analyzer = new StandardAnalyzer();
        // 2.创建 Directory 对象，表示索引库的位置
        Directory directory = FSDirectory.open(Paths.get(INDEX_LOCATION));
        // 3.创建 IndexWriterConfig 对象，这个对象指定切分词使用的分词器
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
        // 4.创建 IndexWriter 输出流对象，指定输出的位置和使用的 config 初始化对象
        IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);
        /**
         * 5.删除所有数据
         */
        indexWriter.deleteAll();
        // 6.释放资源
        indexWriter.close();
        Sout.d(TAG, "删除所有数据成功");
    }

    /**
     * 创建索引库
     * 使用特定的 Field
     */
    public static void createIndexTest3() throws IOException {
        // 1.采集数据
        SkuDao skuDao = new SkuDaoImpl();
        List<SkuBean> skuBeanList = skuDao.querySkuList();
        List<Document> documentList = new ArrayList<>();
        long startTime = System.currentTimeMillis();
        for (SkuBean skuBean : skuBeanList) {
            // 2.创建文档对象
            Document document = new Document();
            /**
             * 创建域对象，并且放入文档中
             * 参数 1：域名
             * 参数 2：域值
             * 参数 3：是否存储
             */
            /**
             * 是否分词：否，因为主键分词后无意义
             * 是否索引：是，因为会根据主键 id 进行查询，就必须索引
             * 是否存储：是，因为主键 id 比较特殊，可以确定唯一的一条数据，所以存起来
             * 存起来，才能获取到 id 具体的内容显示出来
             * 因为是 N Y Y ，所以查表选用 StringField
             */
            document.add(new StringField("id", skuBean.getId(), Field.Store.YES));
            /**
             * 是否分词：是，因为名称字段需要查询，并且分词后有意义，所以需要分词
             * 是否索引：是，因为需要根据名称字段查询
             * 是否存储：是，需要展示
             * Y Y Y, 选用 TextField
             */
            document.add(new TextField("name", skuBean.getName(), Field.Store.YES));
            /**
             * 是否分词：是（lucene 底层规定，如果根据价格范围查询，必须分词）
             * 是否索引：是，会根据价格进行范围查询，所以必须索引
             * 是否存储：是，因为需要展示价格
             * Y Y Y
             * Y Y N -> 选用 IntPoint，不存储
             * N N Y -> 选用 StoredField，存储
             * 通过这两个配合，完成价格的索引分词
             */
            document.add(new IntPoint("price", skuBean.getPrice())); // 不存储
            document.add(new StoredField("price", skuBean.getPrice())); // 存储
            /**
             * 是否分词：否
             * 是否索引：是
             * 是否存储：是
             * 因为是 N Y Y ，所以查表选用 StringField
             */
            document.add(new StringField("num", String.valueOf(skuBean.getNum()), Field.Store.YES));
            /**
             * 是否分词：否，不需要索引也就不需要分词
             * 是否索引：否，不需要根据图片地址进行查询
             * 是否存储：是，因为页面需要展示商品图片
             * N N Y -> 选用 StoredField
             */
            document.add(new StoredField("image", skuBean.getImage()));
            /**
             * 是否分词：否，因为分类是专有的名词，是一个整体，不需要分词
             * 是否索引：是，因为需要根据分类查询
             * 是否存储：是，页面需要展示分类
             * N Y Y -> StringField
             */
            document.add(new StringField("categoryName", skuBean.getCategoryName(), Field.Store.YES));
            /**
             * 是否分词：否，因为品牌是专有名词，是一个整体
             * 是否索引：是，需要根据品牌进行查询
             * 是否存储：是，页面需要展示
             * N Y Y -> StringField
             */
            document.add(new StringField("brandName", skuBean.getBrandName(), Field.Store.YES));
            document.add(new StringField("spec", skuBean.getSpec(), Field.Store.YES));
            document.add(new StringField("saleNum", String.valueOf(skuBean.getSaleNum()), Field.Store.YES));
            // 将文档对象放入文档集合中
            documentList.add(document);
        }
        /**
         * 3.创建分词器对象
         * StandardAnalyzer 是标准分词器，对英文分词效果好
         * 对中文是单字分词，也就是一个字认为是一个词
         */
        Analyzer analyzer = new StandardAnalyzer();
        // 4.创建 Directory 目录对象，目录对象表示索引库的位置
        Directory directory = FSDirectory.open(Paths.get(INDEX_LOCATION));
        // 5.创建 IndexWriterConfig 对象，这个对象中指定切分词使用的分词器
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);

        // indexWriterConfig.setMaxBufferedDocs(500000);
        // 6.创建 IndexWriter 输出流对象，指定输出的位置和使用 config 初始化对象
        IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);
        indexWriter.forceMerge(10000);

        // 7.写入文档到索引库
        for (Document document : documentList) {
            indexWriter.addDocument(document);
        }
        // 8.释放资源
        indexWriter.close();
        long endTime = System.currentTimeMillis();
        Sout.d(TAG, "写入完成 cost time: " + (endTime - startTime) + " ms");
    }

    /**
     * 创建索引库
     * Directory directory = MMapDirectory.open(Paths.get(INDEX_LOCATION));
     */
    public static void createIndexTest4() throws IOException {
        // 1.采集数据
        SkuDao skuDao = new SkuDaoImpl();
        List<SkuBean> skuBeanList = skuDao.querySkuList();
        List<Document> documentList = new ArrayList<>();
        long startTime = System.currentTimeMillis();
        for (SkuBean skuBean : skuBeanList) {
            // 2.创建文档对象
            Document document = new Document();
            document.add(new StringField("id", skuBean.getId(), Field.Store.YES));
            document.add(new TextField("name", skuBean.getName(), Field.Store.YES));
            document.add(new IntPoint("price", skuBean.getPrice())); // 不存储
            document.add(new StoredField("price", skuBean.getPrice())); // 存储
            document.add(new StringField("num", String.valueOf(skuBean.getNum()), Field.Store.YES));
            document.add(new StoredField("image", skuBean.getImage()));
            document.add(new StringField("categoryName", skuBean.getCategoryName(), Field.Store.YES));
            document.add(new StringField("brandName", skuBean.getBrandName(), Field.Store.YES));
            document.add(new StringField("spec", skuBean.getSpec(), Field.Store.YES));
            document.add(new StringField("saleNum", String.valueOf(skuBean.getSaleNum()), Field.Store.YES));
            // 将文档对象放入文档集合中
            documentList.add(document);
        }
        /**
         * 3.创建分词器对象
         * StandardAnalyzer 是标准分词器，对英文分词效果好
         * 对中文是单字分词，也就是一个字认为是一个词
         */
        Analyzer analyzer = new StandardAnalyzer();
        // 4.创建 Directory 目录对象，目录对象表示索引库的位置
        Directory directory = MMapDirectory.open(Paths.get(INDEX_LOCATION));
        // 5.创建 IndexWriterConfig 对象，这个对象中指定切分词使用的分词器
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);

        // 6.创建 IndexWriter 输出流对象，指定输出的位置和使用 config 初始化对象
        IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);

        // 7.写入文档到索引库
        for (Document document : documentList) {
            indexWriter.addDocument(document);
        }
        // 8.释放资源
        indexWriter.close();
        long endTime = System.currentTimeMillis();
        Sout.d(TAG, "写入完成 cost time: " + (endTime - startTime) + " ms");
    }
}
