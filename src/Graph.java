import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

public class Graph {

  private Map<String, Aeroport> aeroports = new HashMap<>();
  private Set<Vol> vols = new HashSet<>();

  // dijkstra
  public void calculerItineraireMiniminantDistance(String iataSrc, String iataDest, String path) {
    Aeroport src = aeroports.get(iataSrc);
    Aeroport dest = aeroports.get(iataDest);
    if (src == null || dest == null) {
      throw new AucunCheminException();
    }
    src.setCout(0);
    Comparator<Aeroport> comparator = Comparator.comparingDouble(Aeroport::getCout);
    PriorityQueue<Aeroport> pq = new PriorityQueue<>(comparator);
    pq.addAll(aeroports.values());
    while (!pq.isEmpty()) {
      Aeroport aeroportCourant = pq.remove();
      Set<Vol> volsSortants = aeroportCourant.getVolsSortants();
      for (Vol vol : volsSortants) {
        Aeroport destination = vol.getDestination();
        if (destination.getCout() > aeroportCourant.getCout() + vol.getDistance()) {
          destination.setCout(aeroportCourant.getCout() + vol.getDistance());
          destination.setVol(vol);
        }
      }
    }
    Aeroport courant = dest;
    if (dest.getVol() == null) {
      throw new AucunCheminException();
    }
    Deque<Aeroport> trajet = new ArrayDeque<>();
    while (courant != src) {
      trajet.push(courant);
      courant = courant.getVol().getOrigine();
    }
    while (!trajet.isEmpty()) {
      System.out.println(trajet.pop());
    }
  }

  // bfs
  public void calculerItineraireMinimisantNombreVol(String iataSrc, String iataDest, String path) {}

  public void ajouterVol(Vol vol) {
    vols.add(vol);
  }

  public void ajouterAeroport(String iata, Aeroport aeroport) {
    aeroports.put(iata, aeroport);
  }
}
