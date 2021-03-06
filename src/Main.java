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

      g.calculerItineraireMiniminantDistance("BRU", "PPT", "output.xml");
      g.calculerItineraireMinimisantNombreVol("BRU", "PPT", "output2.xml");

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
