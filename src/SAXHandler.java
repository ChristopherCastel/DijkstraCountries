import java.util.HashMap;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class SAXHandler extends DefaultHandler {

  private Graph graph = new Graph();

  private HashMap<String, Aeroport> iataAeroports = new HashMap<>();
  private HashMap<String, String> iataAirlines = new HashMap<>();

  private boolean isLatitude;
  private boolean isLongitude;
  private boolean isCompagnie;

  private String iataCompagnie = null;
  private String nomCompagnie = null;

  private String iataAeroport = null;
  private String nomAeroport = null;
  private String pays = null;
  private String ville = null;
  private double lat;
  private double lon;

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes) {

    if (qName.equals("airport")) {
      iataAeroport = attributes.getValue(0);
      nomAeroport = attributes.getValue(1);
      ville = attributes.getValue(2);
      pays = attributes.getValue(3);
    } else if (qName.equals("latitude")) {
      isLatitude = true;
    } else if (qName.equals("longitude")) {
      isLongitude = true;
    } else if (qName.equals("route")) {
      String compagnie = iataAirlines.get(attributes.getValue(0));
      String iataSrc = attributes.getValue(1);
      String iataDest = attributes.getValue(2);
      Aeroport aeroportSrc = iataAeroports.get(iataSrc);
      Aeroport aeroportDest = iataAeroports.get(iataDest);

      double lat1 = aeroportSrc.getLatitude();
      double lon1 = aeroportSrc.getLongitude();
      double lat2 = aeroportDest.getLatitude();
      double lon2 = aeroportDest.getLongitude();
      double distance = Util.distance(lat1, lon1, lat2, lon2);

      graph.ajouterVol(new Vol(distance, compagnie, aeroportSrc, aeroportDest));
    } else if (qName.equals("airline")) {
      iataCompagnie = attributes.getValue(0);
      isCompagnie = true;
    }
  }

  @Override
  public void endElement(String uri, String localName, String qName) {
    if (qName.equals("airport")) {
      Aeroport ajout = new Aeroport(iataAeroport, nomAeroport, pays, ville, lat, lon);
      iataAeroports.put(iataAeroport, ajout);
      graph.ajouterAeroport(iataAeroport, ajout);
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
    } else if (isCompagnie) {
      nomCompagnie = new String(ch, start, length);
      iataAirlines.put(iataCompagnie, nomCompagnie);
      isCompagnie = false;
    }
  }

  public Graph getGraph() {
    return graph;
  }
}
