import java.io.File;
import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;

public class Graph {

  private Map<String, Aeroport> aeroports = new HashMap<>();
  private Set<Vol> vols = new HashSet<>();

  // dijkstra
  public void calculerItineraireMiniminantDistance(String iataSrc, String iataDest, String path) {
    Set<Aeroport> def = new HashSet<>(aeroports.size());
    Aeroport src = aeroports.get(iataSrc);
    Aeroport dest = aeroports.get(iataDest);
    if (src == null || dest == null) {
      throw new AucunCheminException();
    }
    src.setCout(0);
    Comparator<Aeroport> comparator = Comparator.comparingDouble(Aeroport::getCout);
    Queue<Aeroport> pq = new PriorityQueue<>(comparator);
    pq.add(src);
    while (!pq.isEmpty()) {
      Aeroport aeroportCourant = pq.remove();
      if (aeroportCourant.equals(dest)) {
        // afficherRoute(src, dest);
        sauvegarderChemins(src, dest, path);
        resetAeroports();
        return;
      }
      // pas traiter deux fois le même aeroport
      if (def.contains(aeroportCourant)) {
        continue;
      }
      def.add(aeroportCourant);
      // mises à jours des couts
      for (Vol vol : aeroportCourant.getVolsSortants()) {
        Aeroport destination = vol.getDestination();
        double nouveauCout = aeroportCourant.getCout() + vol.getDistance();
        if (destination.getCout() > nouveauCout) {
          destination.setCout(nouveauCout);
          destination.setVol(vol);
          pq.add(destination);
        }
      }
    }
    throw new AucunCheminException();
  }

  // bfs
  public void calculerItineraireMinimisantNombreVol(String iataSrc, String iataDest, String path) {
    Deque<Aeroport> file = new ArrayDeque<>(aeroports.size());
    Aeroport src = aeroports.get(iataSrc);
    Aeroport dest = aeroports.get(iataDest);
    file.add(src);
    ajouterConnexions(dest, file);
    // afficherRoute(src, dest);
    sauvegarderChemins(src, dest, path);
    resetAeroports();
  }

  private void ajouterConnexions(Aeroport dest, Deque<Aeroport> file) {
    if (file.isEmpty()) {
      return;
    }
    Aeroport src = file.removeFirst();
    if (src.equals(dest)) {
      return;
    }
    Set<Vol> vols = src.getVolsSortants();
    if (vols == null) {
      return;
    }
    for (Vol sortant : vols) {
      Aeroport correspondance = sortant.getDestination();
      if (correspondance.getVol() != null) {
        continue;
      }
      correspondance.setVol(sortant);
      file.addLast(correspondance);
    }
    ajouterConnexions(dest, file);
  }

  private void sauvegarderChemins(Aeroport src, Aeroport dest, String path) {
    if (dest.getVol() == null) {
      throw new AucunCheminException();
    }
    Aeroport courant = dest;
    System.out.println(dest.getCout());
    Deque<Aeroport> trajet = new ArrayDeque<>();
    while (courant != src) {
      trajet.push(courant);
      courant = courant.getVol().getOrigine();
    }
    try {
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.newDocument();

      Element rootElement = doc.createElement("trajet");
      Attr a1 = doc.createAttribute("depart");
      a1.setValue(src.getNom());
      Attr a2 = doc.createAttribute("destination");
      a2.setValue(dest.getNom());
      Attr a3 = doc.createAttribute("distance");
      a3.setValue(String.valueOf(dest.getCout()));
      rootElement.setAttributeNode(a1);
      rootElement.setAttributeNode(a2);
      rootElement.setAttributeNode(a3);
      doc.appendChild(rootElement);
      int numero = 1;
      while (!trajet.isEmpty()) {
        Aeroport aeroport = trajet.pop();
        Element vol = doc.createElement("vol");
        rootElement.appendChild(vol);

        // attributs vol
        Attr vol1 = doc.createAttribute("compagnie");
        vol1.setValue(aeroport.getVol().getCompagnie());
        Attr vol2 = doc.createAttribute("nombreKm");
        vol2.setValue(String.valueOf(aeroport.getVol().getDistance()));
        Attr vol3 = doc.createAttribute("numero");
        vol3.setValue(String.valueOf(numero++));
        vol.setAttributeNode(vol1);
        vol.setAttributeNode(vol2);
        vol.setAttributeNode(vol3);

        // ajout source
        remplirNodeAeroport(doc, aeroport.getVol().getOrigine(), vol, "source");
        remplirNodeAeroport(doc, aeroport.getVol().getDestination(), vol, "destination");

        // enregistrer dans un fichier
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        DOMImplementation domImpl = doc.getImplementation();
        DocumentType doctype = domImpl.createDocumentType("doctype", "DOCTYPE", "output.dtd");
        transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, doctype.getSystemId());
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(path));
        transformer.transform(source, result);
      }
    } catch (Exception ex) {
      System.err.println("Sauvegarde impossible !");
      ex.printStackTrace();
    }
  }

  private void remplirNodeAeroport(Document doc, Aeroport aeroport, Element vol, String nomNode) {
    Element node = doc.createElement(nomNode);
    node.appendChild(doc.createTextNode(aeroport.getNom()));
    // attributs destination
    Attr a1 = doc.createAttribute("iataCode");
    a1.setValue(aeroport.getIata());
    Attr a2 = doc.createAttribute("pays");
    a2.setValue(aeroport.getPays());
    Attr a3 = doc.createAttribute("ville");
    a3.setValue(aeroport.getVille());
    node.setAttributeNode(a1);
    node.setAttributeNode(a2);
    node.setAttributeNode(a3);
    vol.appendChild(node);
  }

  private void resetAeroports() {
    for (Aeroport aeroport : aeroports.values()) {
      aeroport.setCout(Double.MAX_VALUE);
      aeroport.setVol(null);
    }
  }

  public void ajouterVol(Vol vol) {
    vols.add(vol);
  }

  public void ajouterAeroport(String iata, Aeroport aeroport) {
    aeroports.put(iata, aeroport);
  }
}
