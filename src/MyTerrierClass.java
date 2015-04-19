
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.terrier.indexing.Collection;
import org.terrier.indexing.SimpleFileCollection;
import org.terrier.indexing.TaggedDocument;
import org.terrier.indexing.tokenisation.TokenStream;
import org.terrier.indexing.tokenisation.Tokeniser;
import org.terrier.realtime.MemoryIndexer;
import org.terrier.structures.indexing.Indexer;
import org.terrier.structures.indexing.classical.BasicIndexer;
import org.terrier.structures.indexing.classical.BlockIndexer;
import org.terrier.structures.indexing.singlepass.BasicSinglePassIndexer;
import org.terrier.structures.indexing.singlepass.BlockSinglePassIndexer;
import org.terrier.structures.indexing.singlepass.NoDuplicatesSinglePassIndexing;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import org.terrier.matching.ResultSet;
import org.terrier.querying.Manager;
import org.terrier.querying.SearchRequest;
import org.terrier.structures.BitIndexPointer;
import org.terrier.structures.Index;
import org.terrier.structures.Lexicon;
import org.terrier.structures.LexiconEntry;
import org.terrier.structures.MetaIndex;
import org.terrier.structures.bit.BitPostingIndex;
import org.terrier.structures.bit.InvertedIndex;
import org.terrier.structures.postings.IterablePosting;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author manan
 */
public class MyTerrierClass {
    
    public static void main(String[] args) throws InterruptedException, IOException
    {
        
        System.gc();
        System.setProperty("terrier.home", "/home/manan/Downloads/terrier-4.0");
        System.setProperty("terrier.etc","/home/manan/Downloads/terrier-4.0/etc");
        System.setProperty("terrier.setup","/home/manan/Downloads/terrier-4.0/etc/terrier.properties");
        
        bw = new BufferedWriter(new FileWriter("/home/manan/terrier_trials/myOutput.txt", false));
        bw.write("#######OUTPUT#######");
        
        List<String> cl = new ArrayList<>();
        cl.add("/home/manan/wiki-small/");
        SimpleFileCollection sf = new SimpleFileCollection(cl,true);
        
        print("sf list size b4 indexing:"+sf.getFileList().size());
        
        Indexer indexer = new BlockIndexer("/home/manan/terrier_trials/", ".trial");
        indexer.index(new Collection[]{sf});
        
        print("sf list size after indexing:"+sf.getFileList().size());
        
        Index index = Index.createIndex();
        
        Manager queryingManager = new Manager(index);
 
        String query = "name quick";
        SearchRequest srq = queryingManager.newSearchRequest("queryID0", query);
        srq.addMatchingModel("Matching", "PL2");
        queryingManager.runPreProcessing(srq);
        queryingManager.runMatching(srq);
        queryingManager.runPostProcessing(srq);
        queryingManager.runPostFilters(srq);
        ResultSet rs = srq.getResultSet();
        for(int each : rs.getDocids())
            System.out.println("docids: "+ each);
        
//        Index index = Index.createIndex();
//        Lexicon<String> lex = index.getLexicon();
//        print("lex number of entries: "+lex.numberOfEntries());
//        String myTerm = "name";
//        LexiconEntry le = lex.getLexiconEntry(myTerm);
//        if(le!=null)
//        {
//            print(myTerm+" in number of docs: "+le.getDocumentFrequency());
//            print(myTerm+" occurance times: "+le.getFrequency());
//        }
//        else
//        {
//            print(myTerm+" not found");
//        }
        
//        Index index = Index.createIndex();
//        BitPostingIndex inv = (BitPostingIndex) index.getInvertedIndex();
//        MetaIndex meta = index.getMetaIndex();
//        Lexicon<String> lex = index.getLexicon();
//        LexiconEntry le = lex.getLexiconEntry( "name" );
//        IterablePosting postings = inv.getPostings((BitIndexPointer) le);
//        while (postings.next() != IterablePosting.EOL) {
//                String docno = meta.getItem("docno", postings.getId());
//                System.out.println(docno + " with frequency " + postings.getFrequency());
//        }
            
        bw.close();
    }
    
    static BufferedWriter bw;
    
    private static void print(String x) throws IOException
    {
        bw.write("\n"+x);
        bw.flush();
    }
    
}
