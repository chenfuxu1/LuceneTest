package htd.test;


import htd.utils.Sout;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cjk.CJKAnalyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * 该类用来测试分词器的功能
 */
public class TestAnalyzer {
    private static final String TAG = "TestAnalyzer";
    private static final String INDEX_LOCATION = "D:\\F\\LuceneIndex";

    public static void main(String[] args) throws Exception {
        // TestWhiteSpaceAnalyzer();
        // TestSimpleAnalyzer();
        // TestCJKAnalyzer();
        TestIKAnalyzer();
    }

    /**
     * 测试 WhitespaceAnalyzer 分词器的功能
     * 该分词器只能去除空格
     */
    public static void TestWhiteSpaceAnalyzer() throws IOException {
        // 1.创建分词器，分析文档，对文档进行分词
        Analyzer analyzer = new WhitespaceAnalyzer();
        // 2.创建 Directory 对象，指明索引库的位置
        Directory directory = FSDirectory.open(Paths.get(INDEX_LOCATION));
        // 3.创建 IndexWriterConfig 对象，写入索引库需要的配置
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
        // 4.创建 IndexWriter 写入对象
        IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);
        // 5.写入索引库，通过 IndexWriter 添加文档对象 document
        Document document = new Document();
        document.add(new TextField("name", "vivo x23 8GB+128GB 幻夜蓝", Field.Store.YES));
        indexWriter.addDocument(document);
        // 6.关闭流，释放资源
        indexWriter.close();
        Sout.d(TAG, "WhitespaceAnalyzer 写入文件成功");
    }


    /**
     * 测试 SimpleAnalyzer 分词器
     * 特点：将所有大写字母转为小写，过滤掉数字，不支持中文
     */
    public static void TestSimpleAnalyzer() throws Exception {
        // 1.创建分词器, 分析文档，对文档进行分词
        Analyzer analyzer = new SimpleAnalyzer();
        // 2.创建 Directory 对象,声明索引库的位置
        Directory directory = FSDirectory.open(Paths.get(INDEX_LOCATION));
        // 3.创建 IndexWriteConfig 对象，写入索引需要的配置
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        // 4.创建 IndexWriter 写入对象
        IndexWriter indexWriter = new IndexWriter(directory, config);
        // 5.写入到索引库，通过 IndexWriter 添加文档对象 document
        Document doc = new Document();
        doc.add(new TextField("name", "vivo，X23。 8GB+128GB； 幻夜蓝", Field.Store.YES));
        indexWriter.addDocument(doc);
        // 6.释放资源
        indexWriter.close();
        Sout.d(TAG, "SimpleAnalyzer 写入文件成功");
    }

    /**
     * 测试 CJKAnalyzer 分词器（中日韩分词器）
     * 特点：支持中日韩文，对中文是二分法分词，去掉标点，去掉空格，这个分词器很烂
     * 二分法：这是测试语句 -> 切分为：这是 是测 测试 试语 语句
     */
    public static void TestCJKAnalyzer() throws Exception {
        // 1.创建分词器,分析文档，对文档进行分词
        Analyzer analyzer = new CJKAnalyzer();
        // 2.创建 Directory 对象,声明索引库的位置
        Directory directory = FSDirectory.open(Paths.get(INDEX_LOCATION));
        // 3. 创建 IndexWriteConfig 对象，写入索引需要的配置
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        // 4.创建 IndexWriter 写入对象
        IndexWriter indexWriter = new IndexWriter(directory, config);
        // 5.写入到索引库，通过 IndexWriter 添加文档对象 document
        Document doc = new Document();
        doc.add(new TextField("name", "vivo，X23。 8GB+128GB； 这是测试语句", Field.Store.YES));
        indexWriter.addDocument(doc);
        // 6.释放资源
        indexWriter.close();
        Sout.d(TAG, "CJKAnalyzer 写入文件成功");
    }

    /**
     * 使用第三方分词器，中文分词器
     * 特点：支持中文语义分析，提供停用词典，提供扩展词典
     * 提供程序员扩展使用
     */
    public static void TestIKAnalyzer() throws Exception{
        // 1.创建分词器,分析文档，对文档进行分词
        Analyzer analyzer = new IKAnalyzer();
        // 2.创建Directory对象,声明索引库的位置
        Directory directory = FSDirectory.open(Paths.get(INDEX_LOCATION));
        // 3.创建 IndexWriteConfig 对象，写入索引需要的配置
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        // 4.创建 IndexWriter 写入对象
        IndexWriter indexWriter = new IndexWriter(directory, config);
        // 5.写入到索引库，通过 IndexWriter 添加文档对象 document
        Document doc = new Document();
        doc.add(new TextField("name", "vivo X23 8GB+128GB 幻夜蓝,水滴屏全面屏,游戏极人南极人手机.移动联通电信全网通4G手机", Field.Store.YES));
        indexWriter.addDocument(doc);
        // 6.释放资源
        indexWriter.close();
    }
}
