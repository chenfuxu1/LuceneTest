package htd.test;

import htd.utils.Sout;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * Project: LuceneTest
 * Create By: Chen.F.X
 * DateTime: 2025-06-17 23:26
 * <p>
 * Desc: 测试搜索过程
 *
 * 2025-06-17 23:42:21.770	main		TestSearch	获取查询的总数 totalHits: 24147
 * 2025-06-17 23:42:21.773	main		TestSearch	========================
 * 2025-06-17 23:42:21.773	main		TestSearch	id: 18182117877	name: 华为（HUAWEI） 华为 mate10 手机 亮黑色 4G+64G	price:
 * 76300	num: 10000	categoryName: 手机	brandName: null
 * 2025-06-17 23:42:21.773	main		TestSearch	========================
 * 2025-06-17 23:42:21.773	main		TestSearch	id: 18182117880	name: 华为（HUAWEI） 华为 mate10 手机 亮黑色 6G+128G	price:
 * 53100	num: 10000	categoryName: 手机	brandName: null
 * 2025-06-17 23:42:21.774	main		TestSearch	========================
 * 2025-06-17 23:42:21.774	main		TestSearch	id: 18182117882	name: 华为（HUAWEI） 华为 mate10 手机 亮黑色 6G+128G	price:
 * 87300	num: 10000	categoryName: 手机	brandName: null
 * 2025-06-17 23:42:21.774	main		TestSearch	========================
 * 2025-06-17 23:42:21.774	main		TestSearch	id: 18182117883	name: 华为（HUAWEI） 华为 mate10 手机 亮黑色 6G+128G	price:
 * 29100	num: 10000	categoryName: 手机	brandName: null
 * 2025-06-17 23:42:21.774	main		TestSearch	========================
 * 2025-06-17 23:42:21.774	main		TestSearch	id: 25387600556	name: 华为（HUAWEI） 华为 mate10 手机 亮黑色 6G+128G	price:
 * 32500	num: 10000	categoryName: 手机	brandName: null
 * 2025-06-17 23:42:21.774	main		TestSearch	========================
 * 2025-06-17 23:42:21.774	main		TestSearch	id: 39004962179	name: 华为（HUAWEI） 华为nova3 手机 亮黑色 6G+128G	price:
 * 88800	num: 10000	categoryName: 手机	brandName: null
 * 2025-06-17 23:42:21.774	main		TestSearch	========================
 * 2025-06-17 23:42:21.774	main		TestSearch	id: 39004962180	name: 华为（HUAWEI） 华为nova3 手机 亮黑色 6G+128G	price:
 * 80400	num: 10000	categoryName: 手机	brandName: null
 * 2025-06-17 23:42:21.774	main		TestSearch	========================
 * 2025-06-17 23:42:21.774	main		TestSearch	id: 39004962181	name: 华为（HUAWEI） 华为nova3 手机 亮黑色 6G+128G	price:
 * 67400	num: 10000	categoryName: 手机	brandName: null
 * 2025-06-17 23:42:21.774	main		TestSearch	========================
 * 2025-06-17 23:42:21.774	main		TestSearch	id: 39004962182	name: 华为（HUAWEI） 华为nova3 手机 亮黑色 6G+128G	price:
 * 28900	num: 10000	categoryName: 手机	brandName: null
 * 2025-06-17 23:42:21.774	main		TestSearch	========================
 * 2025-06-17 23:42:21.775	main		TestSearch	id: 39004962183	name: 华为（HUAWEI） 华为nova3 手机 亮黑色 6G+128G	price:
 * 1000	num: 10000	categoryName: 手机	brandName: null
 * 2025-06-17 23:42:21.775	main		TestSearch	search cost time: 161 ms
 */
public class TestSearch {
    private static final String TAG = "TestSearch";
    private static final String INDEX_LOCATION = "D:\\F\\LuceneIndex";

    public static void main(String[] args) throws Exception {
        // testQueryParse();
        // testRangeQuery();
        testHighIndexSearchTerm();
    }

    private static void testQueryParse() throws ParseException, IOException {
        long start = System.currentTimeMillis();
        /**
         * 1.创建分词器，对搜素的关键词进行分词使用
         * 注意：分词器要和创建索引的时候使用的分词器一模一样
         * 因为不同分词器的分词效果是不一致的
         */
        Analyzer analyzer = new StandardAnalyzer();
        /**
         * 2.创建查询的对象
         * 参数 1：默认查询域,这里默认按 name 进行查询
         * 参数 2：使用的分词器
         */
        QueryParser queryParser = new QueryParser("name", analyzer);
        // 3.设置搜索的关键词
        Query query = queryParser.parse("华为手机"); // 表示会从默认域的 name 中查询
        // Query query = queryParser.parse("brandName:华为手机"); // 表示会从 brandName 域中查询
        // 3.2 查询 华为 AND 手机，会取两者的交集，即既有华为又有手机的结果
        // Query query = queryParser.parse("华为 AND 手机");

        // 3.3 查询 华为 OR 手机，会取两者的并集，和默认什么都不加的结果一样
        // Query query = queryParser.parse("华为 OR 手机");

        // 4.创建 Directory 目录对象，指定索引库的位置
        Directory directory = FSDirectory.open(Paths.get("D:\\F\\LuceneIndex"));

        // 5.创建输入流对象
        IndexReader indexReader = DirectoryReader.open(directory);

        // 6.创建搜索对象
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        /**
         * 7.执行搜素，并返回结果
         * 参数 1：查询 query
         * 参数 2：返回多少条数据
         */
        TopDocs topDocs = indexSearcher.search(query, 10);

        // 获取到查询的结果集总数
        long totalHits = topDocs.totalHits;
        Sout.d(TAG, "获取查询的总数 totalHits: " + totalHits);

        // 8.获取结果集
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;

        // 9.遍历结果集
        if (scoreDocs != null) {
            for (ScoreDoc scoreDoc : scoreDocs) {
                // 可以获取 lucene 为文档分配的 id, 文档的唯一标识符
                int documentId = scoreDoc.doc;
                // 通过文档 id，找到文档，通过流对象，读出文档
                Document document = indexSearcher.doc(documentId);
                Sout.d(TAG, "========================");
                // 通过域名，获取域值
                Sout.d(TAG,"id: " + document.get("id") +
                        "\tname: " + document.get("name") +
                        "\tprice: " + document.get("price") +
                        "\tnum: " + document.get("num") +
                        "\tcategoryName: " + document.get("categoryName") +
                        "\tbrandName: " + document.get("brandName"));
            }
        }
        Sout.d(TAG, "search cost time: " + (System.currentTimeMillis() - start) + " ms");
        // 10.释放资源，关闭流
        indexReader.close();
    }

    private static void testRangeQuery() throws ParseException, IOException {
        long start = System.currentTimeMillis();
        /**
         * 1.创建分词器，对搜素的关键词进行分词使用
         * 注意：分词器要和创建索引的时候使用的分词器一模一样
         * 因为不同分词器的分词效果是不一致的
         */
        Analyzer analyzer = new StandardAnalyzer();
        Query query = IntPoint.newRangeQuery("price", 100, 1000);


        // 4.创建 Directory 目录对象，指定索引库的位置
        Directory directory = FSDirectory.open(Paths.get("D:\\F\\LuceneIndex"));

        // 5.创建输入流对象
        IndexReader indexReader = DirectoryReader.open(directory);

        // 6.创建搜索对象
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        /**
         * 7.执行搜素，并返回结果
         * 参数 1：查询 query
         * 参数 2：返回多少条数据
         */
        TopDocs topDocs = indexSearcher.search(query, 10);

        // 获取到查询的结果集总数
        long totalHits = topDocs.totalHits;
        Sout.d(TAG, "获取查询的总数 totalHits: " + totalHits);

        // 8.获取结果集
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;

        // 9.遍历结果集
        if (scoreDocs != null) {
            for (ScoreDoc scoreDoc : scoreDocs) {
                // 可以获取 lucene 为文档分配的 id, 文档的唯一标识符
                int documentId = scoreDoc.doc;
                // 通过文档 id，找到文档，通过流对象，读出文档
                Document document = indexSearcher.doc(documentId);
                Sout.d(TAG, "========================");
                // 通过域名，获取域值
                Sout.d(TAG,"id: " + document.get("id") +
                        "\tname: " + document.get("name") +
                        "\tprice: " + document.get("price") +
                        "\tnum: " + document.get("num") +
                        "\tcategoryName: " + document.get("categoryName") +
                        "\tbrandName: " + document.get("brandName"));
            }
        }
        Sout.d(TAG, "search cost time: " + (System.currentTimeMillis() - start) + " ms" + " scoreDocs.length: " + scoreDocs.length);
        // 10.释放资源，关闭流
        indexReader.close();
    }

    /**
     * 高级搜索
     * 组合查询
     * 查询价格大于等于 100, 小于等于 1000, 并且名称中包含华为手机关键字的商品
     * BooleanClause.Occur.MUST 必须 相当于 and, 并且
     * BooleanClause.Occur.MUST_NOT 不必须 相当于 not, 非
     * BooleanClause.Occur.SHOULD 应该 相当于 or, 或者
     * 注意: 如果逻辑条件中, 只有 MUST_NOT, 或者多个逻辑条件都是 MUST_NOT, 无效, 查询不出任何数据
     * 这是对数据的保护，防止你把所有的数据查询出来了
     */
    public static void testHighIndexSearchTerm() throws Exception {
        long start = System.currentTimeMillis();
        // 1.创建查询对象，查询 price 在 100 到 1000 之间
        // 查询语句 1
        Query query1 = IntPoint.newRangeQuery("price", 100, 1000);

        StandardAnalyzer standardAnalyzer = new StandardAnalyzer();
        QueryParser queryParser = new QueryParser("name", standardAnalyzer);
        // 查询语句 2
        Query query2 = queryParser.parse("华为手机");

        // 创建组合查询对象
        BooleanQuery.Builder builder = new BooleanQuery.Builder();
        builder.add(new BooleanClause(query1, BooleanClause.Occur.MUST));
        builder.add(new BooleanClause(query2, BooleanClause.Occur.MUST));
        Query query = builder.build();

        // 2.创建 Directory 目录对象，指定索引库的位置
        Directory directory = FSDirectory.open(Paths.get(INDEX_LOCATION));
        // 3.创建输入流对象
        IndexReader indexReader = DirectoryReader.open(directory);
        // 4.创建搜索对象
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        /**
         * 5.执行搜素，并返回结果
         * 参数 1：查询 query
         * 参数 2：返回多少条数据
         */
        TopDocs topDocs = indexSearcher.search(query, 100000);
        // 获取到查询的结果集总数
        long totalHits = topDocs.totalHits;
        Sout.d(TAG, "获取查询的总数：" + totalHits);
        // 6.获取结果集
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        // 7.遍历结果集
        if (scoreDocs != null) {
            for (ScoreDoc scoreDoc : scoreDocs) {
                // 可以获取lucene为文档分配的id, 文档的唯一标识符
                int documentId = scoreDoc.doc;
                // 通过文档id，找到文档，通过流对象，读出文档
                Document document = indexSearcher.doc(documentId);
                Sout.d(TAG, "========================");
                // 通过域名，获取域值
                Sout.d(TAG, "id: " + document.get("id") +
                        "\tname: " + document.get("name") +
                        "\tprice: " + document.get("price") +
                        "\tnum: " + document.get("num") +
                        "\tcategoryName: " + document.get("categoryName") +
                        "\tbrandName: " + document.get("brandName"));
            }
        }
        Sout.d(TAG, "search cost time: " + (System.currentTimeMillis() - start) + " ms" + " scoreDocs.length: " + scoreDocs.length);
        // 8.释放资源，关闭流
        indexReader.close();
    }
}
