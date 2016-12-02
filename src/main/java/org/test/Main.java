package org.test;

import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Iterator;

public class Main {

  public static void main(String[] args) throws IOException {

    Reader reader = new InputStreamReader(Main.class.getClassLoader().getResourceAsStream("poi.csv"));

    CSVParser parser = CSVFormat.DEFAULT.parse(reader);
    Iterator<CSVRecord> iterator = parser.iterator();
    iterator.next();

    OrientGraph graph = new OrientGraph("remote:localhost/test", "admin", "admin");

    while(iterator.hasNext()){
      importRow(iterator.next(), graph);
    }

    graph.shutdown();
  }

  private static void importRow(CSVRecord next, OrientGraph graph) {
    String wkt = next.get(0);
    String name = next.get(2);
    String type = next.get(3);

    graph.command(new OCommandSQL("insert into POI set name = ?, type = ?, "
        + "location = ST_GeomFromText(?)")).execute(name, type, wkt);
    System.out.println("wkt = " + wkt);
  }
}
