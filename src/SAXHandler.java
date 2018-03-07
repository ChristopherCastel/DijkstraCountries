import java.util.HashMap;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class SAXHandler extends DefaultHandler {

  private Graph graph = new Graph();

  HashMap<String, Aeroport> iataAeroports = new HashMap<>();

  boolean isAirport;
  boolean isLatitude;
  boolean isLongitude;

  Aeroport aeroport = new Aeroport();

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes) {

    if (qName.equals("airport")) {
      aeroport.setIata(attributes.getValue(0));
      isAirport = true;
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

      graph.ajouterVol(new Vol(distance, aeroportSrc, aeroportDest));
    }
  }

  @Override
  public void endElement(String uri, String localName, String qName) {
    if (qName.equals("airport")) {
      iataAeroports.put(aeroport.getIata(), aeroport);
      graph.ajouterAeroport(aeroport);
      isAirport = false;
    }
  }

  @Override
  public void characters(char ch[], int start, int length) {

    if (isLatitude) {
      aeroport.setLatitude(Double.valueOf(new String(ch, start, length)));
      isLatitude = false;
    } else if (isLongitude) {
      aeroport.setLongitude(Double.valueOf(new String(ch, start, length)));
      isLongitude = false;
    }
  }

  public Graph getGraph() {
    return graph;
  }
}
