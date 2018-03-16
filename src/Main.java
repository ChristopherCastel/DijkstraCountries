import java.io.File;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class Main {
  public static void main(String[] args) {

    try {
      File inputFile = new File("flight.xml");
      // File inputFile = new File("test.xml");

      SAXParserFactory factory = SAXParserFactory.newInstance();
      SAXParser saxParser = factory.newSAXParser();
      SAXHandler userhandler = new SAXHandler();
      saxParser.parse(inputFile, userhandler);
      Graph g = userhandler.getGraph();

      // g.calculerItineraireMiniminantDistance("STR", "END", "output.xml");
      // g.calculerItineraireMinimisantNombreVol("STR", "END", "output2.xml");

      double start = 0;
      double total = 0;
      // g.calculerItineraireMiniminantDistance("BRU", "PPT", "output.xml");

      for (int i = 0; i < 10_000; i++) {
        start = System.nanoTime();
        g.calculerItineraireMinimisantNombreVol("BRU", "PPT", "output2.xml");
        total += (System.nanoTime() - start);
      }

      System.out.println("Moyenne : " + (total / 100) / 10E6);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
