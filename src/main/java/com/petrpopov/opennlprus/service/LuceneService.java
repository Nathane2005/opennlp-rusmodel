package com.petrpopov.opennlprus.service;

import com.petrpopov.opennlprus.support.ParseMessage;
import com.petrpopov.opennlprus.support.WebMessage;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.ru.RussianAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.IOContext;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
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

    @Value("${lucene_location}")
    private String LUCENE_INDEX_LOCATION;

    private volatile Directory dirIndex;
    private volatile IndexWriterConfig config;
    private volatile Analyzer analyzer;
    private volatile IndexWriter writer;

    private Logger logger = Logger.getLogger(LuceneService.class);

    @PostConstruct
    public void init() throws IOException {

        dirIndex = new RAMDirectory(new SimpleFSDirectory(new File(LUCENE_INDEX_LOCATION)), IOContext.DEFAULT);

        analyzer = new RussianAnalyzer(Version.LUCENE_46);
        config = new IndexWriterConfig(Version.LUCENE_46, analyzer);
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE); // Rewrite old index
        config.setMaxBufferedDeleteTerms(1);
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

        doc.add(new Field("id", url+number.toString(), Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.WITH_POSITIONS_OFFSETS));
        doc.add(new Field("url", url, Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.WITH_POSITIONS_OFFSETS));

        FieldType type = new FieldType();
        type.setIndexed(true);
        type.setIndexOptions(FieldInfo.IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS);
        type.setStored(true);
        type.setStoreTermVectors(true);
        type.setTokenized(true);
        type.setStoreTermVectorOffsets(true);
        doc.add(new Field("text", text, type));
        //doc.add(new Field("text", text, Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.WITH_POSITIONS_OFFSETS));

        doc.add(new Field("ntext",text, Field.Store.YES, Field.Index.ANALYZED));


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
            logger.info("Found document: " + doc.get("text"));

            res.add(doc.get("url"));
        }

        return res;
    }

    public synchronized List<ParseMessage> searchGeo(String q) throws IOException, InvalidTokenOffsetsException {

        List<ParseMessage> res = new ArrayList<ParseMessage>();

        IndexReader reader = null;
        try {
            reader = DirectoryReader.open(getDirIndex());
        } catch (IOException e) {
            return res;
        }
        IndexSearcher is = new IndexSearcher(reader);


        QueryParser parser = new QueryParser(Version.LUCENE_46, "text", getAnalyzer());
        parser.setDefaultOperator(QueryParser.Operator.AND);

        Query query = null;
        try {
            query = parser.parse(q);
        } catch (ParseException e) {
            return res;
        }

        QueryScorer scorer = new QueryScorer(query, "text");
        SimpleHTMLFormatter formatter = new SimpleHTMLFormatter("<START:location>","<END>");
        Highlighter highlighter = new Highlighter(formatter, scorer);
        highlighter.setTextFragmenter(new SimpleSpanFragmenter(scorer, Integer.MAX_VALUE));

        TopScoreDocCollector collector = TopScoreDocCollector.create(100000, true);

        is.search(query, collector);


        TopDocs hits = collector.topDocs();
        ScoreDoc[] scoreDocs = hits.scoreDocs;
        for (ScoreDoc scoreDoc : scoreDocs) {
            Document doc = is.doc(scoreDoc.doc);
            //logger.info("Found document: " + doc.get("text"));


            int id = scoreDoc.doc;
            String text = doc.get("ntext");
            TokenStream tokenStream = TokenSources.getAnyTokenStream(is.getIndexReader(), id, "ntext", getAnalyzer());
            TextFragment[] frag = highlighter.getBestTextFragments(tokenStream, text, false, 40);

            for (int j = 0; j < frag.length; j++) {
                if ((frag[j] != null) && (frag[j].getScore() > 0)) {

                    String docText = frag[j].toString();
                    logger.info("Found document: " + docText);

                    ParseMessage parseMessage = new ParseMessage(doc.get("url"), Integer.parseInt(doc.get("number")), docText);
                    parseMessage.setOriginalText(doc.get("text"));
                    res.add(parseMessage);
                }
            }
        }
        logger.info(scoreDocs.length);

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
