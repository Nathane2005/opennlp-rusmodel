package com.petrpopov.opennlprus.service;

import com.petrpopov.opennlprus.other.ParseMessage;
import com.petrpopov.opennlprus.other.WebMessage;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.ru.RussianAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: petrpopov
 * Date: 28.11.13
 * Time: 23:07
 */

@Component
public class LuceneService {

    private volatile Directory dirIndex;
    private volatile IndexWriterConfig config;
    private volatile Analyzer analyzer;
    private volatile IndexWriter writer;

    private Logger logger = Logger.getLogger(LuceneService.class);

    @PostConstruct
    public void init() throws IOException {

        dirIndex = new RAMDirectory();

        analyzer = new RussianAnalyzer(Version.LUCENE_46);
        config = new IndexWriterConfig(Version.LUCENE_46, analyzer);
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE); // Rewrite old index
    }

    public synchronized void addDocument(WebMessage message) throws IOException {

        addDocument(message.getUrl(), message.getText());
    }

    public synchronized void addDocument(ParseMessage message) throws IOException {

        addDocument(message.getMessageUrl().getUrl(), message.getMessageUrl().getNumber(), message.getText());
    }

    public synchronized void addDocument(String url, String text) throws IOException {

        Document doc = new Document();

        doc.add(new Field("url", url, Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.WITH_POSITIONS_OFFSETS));
        doc.add(new Field("text", text, Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.WITH_POSITIONS_OFFSETS));
        //doc.add(new TextField("url", url, Field.Store.YES));
        //doc.add(new TextField("text", text, Field.Store.YES));

        writer = getIndexWriter();
        writer.addDocument(doc);
        writer.commit();
        //writer.close();
    }

    public synchronized void addDocument(String url, Integer number, String text) throws IOException {
        Document doc = new Document();

        doc.add(new Field("url", url, Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.WITH_POSITIONS_OFFSETS));
        doc.add(new Field("text", text, Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.WITH_POSITIONS_OFFSETS));
        doc.add(new Field("number", number.toString(), Field.Store.YES,
                Field.Index.ANALYZED, Field.TermVector.WITH_POSITIONS_OFFSETS));

        writer = getIndexWriter();
        writer.addDocument(doc);
        writer.commit();
    }

    public boolean containsUrl(WebMessage message) throws IOException, ParseException {

        List<String> res = search(message.getUrl(), "url");

        return !res.isEmpty();
    }

    public synchronized void search(String q) throws IOException, ParseException {
        search(q, "text");
    }

    public synchronized List<String> search(String q, String field) throws IOException {

        List<String> res = new ArrayList<String>();

        IndexReader reader = null;
        try {
            reader = DirectoryReader.open(getDirIndex());
        } catch (IOException e) {
            return res;
        }
        IndexSearcher is = new IndexSearcher(reader);


        QueryParser parser = new QueryParser(Version.LUCENE_46, field, getAnalyzer());
        parser.setDefaultOperator(QueryParser.Operator.AND);

        Query query = null;
        try {
            query = parser.parse(q);
        } catch (ParseException e) {
            return res;
        }

        TopDocs docs = is.search(query, 10);

        for (ScoreDoc scoreDoc : docs.scoreDocs) {
            Document doc = is.doc(scoreDoc.doc);
            logger.debug("Found document: " + doc.get("url"));

            res.add(doc.get("url"));
        }

        return res;
    }

    private IndexWriter getIndexWriter() throws IOException {
        if( writer == null )
            writer = new IndexWriter(getDirIndex(), getConfig());
        return writer;
    }

    private IndexWriterConfig getConfig() {
        return config;
    }

    private Directory getDirIndex() {
        return dirIndex;
    }

    public Analyzer getAnalyzer() {
        return analyzer;
    }
}
