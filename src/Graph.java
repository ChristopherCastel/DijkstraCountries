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
    Comparator<Aeroport> comparator = Comparator.comparingDouble(Aeroport::getCout);
    PriorityQueue<Aeroport> pq = new PriorityQueue<>(comparator);
    pq.addAll(aeroports.values());
    while (!pq.isEmpty()) {
      Aeroport aeroportCourant = pq.remove();
      Set<Vol> volsSortants = aeroportCourant.getVolsSortants();
      Vol v = null;
      for (Vol vol : volsSortants) {
        if (v == null || vol.getDistance() > v.getDistance()) {
          v = vol;
        }
        v.getDestination().setCout(aeroportCourant.getCout() + v.getDistance());
        if (v.getDestination().getVol() == null
            || v.getDestination().getCout() > aeroportCourant.getCout() + v.getDistance()) {
          v.getDestination().setVol(v);
        }
      }
    }
    Aeroport courant = dest;
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
