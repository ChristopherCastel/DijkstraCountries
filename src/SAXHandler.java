import java.util.HashMap;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class SAXHandler extends DefaultHandler {

  private Graph graph = new Graph();

  private HashMap<String, Aeroport> iataAeroports = new HashMap<>();

  private boolean isLatitude;
  private boolean isLongitude;

  private String iata = null;
  private double lat;
  private double lon;

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes) {

    if (qName.equals("airport")) {
      iata = attributes.getValue(0);
    } else if (qName.equals("latitude")) {
      isLatitude = true;
    } else if (qName.equals("longitude")) {
      isLongitude = true;
    } else if (qName.equals("route")) {
      String iataSrc = attributes.getValue(1);
      String iataDest = attributes.getValue(2);
      Aeroport aeroportSrc = iataAeroports.get(iataSrc);
      Aeroport aeroportDest = iataAeroports.get(iataDest);

      double lat1 = aeroportSrc.getLatitude();
      double lon1 = aeroportSrc.getLongitude();
      double lat2 = aeroportDest.getLatitude();
      double lon2 = aeroportDest.getLongitude();

      double distance = Util.distance(lat1, lon1, lat2, lon2);

      // System.out.println(aeroportSrc);
      System.out.println("S = " + aeroportSrc + "D = " + aeroportDest);
      graph.ajouterVol(new Vol(distance, aeroportSrc, aeroportDest));
    }
  }

  @Override
  public void endElement(String uri, String localName, String qName) {
    if (qName.equals("airport")) {
      Aeroport ajout = new Aeroport(iata, lat, lon);
      iataAeroports.put(iata, ajout);
      graph.ajouterAeroport(iata, ajout);
    }
  }

  @Override
  public void characters(char ch[], int start, int length) {

    if (isLatitude) {
      lat = Double.valueOf(new String(ch, start, length));
      isLatitude = false;
    } else if (isLongitude) {
      lon = Double.valueOf(new String(ch, start, length));
      isLongitude = false;
    }
  }

  public Graph getGraph() {
    return graph;
  }
}
