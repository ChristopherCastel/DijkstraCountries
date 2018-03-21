import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
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
    Set<Aeroport> def = new HashSet<>(aeroports.size());
    src.setCout(0);
    Comparator<Aeroport> comparator = Comparator.comparingDouble(Aeroport::getCout);
    Queue<Aeroport> pq = new PriorityQueue<>(comparator);
    pq.add(src);
    while (!pq.isEmpty()) {
      Aeroport aeroportCourant = pq.remove();
      if (def.contains(aeroportCourant)) {
        continue;
      }
      if (aeroportCourant.equals(dest)) {
        resetAeroports();
        return;
      }
      // pas traiter deux fois le même aeroport
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
    if (dest.getVol() == null) {
      throw new AucunCheminException();
    }
    afficherRoute(src, dest);
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

  private void afficherRoute(Aeroport src, Aeroport dest) {
    Aeroport courant = dest;
    System.out.println(dest.getCout());
    Deque<Aeroport> trajet = new ArrayDeque<>();
    while (courant != src) {
      trajet.push(courant);
      courant = courant.getVol().getOrigine();
    }
    while (!trajet.isEmpty()) {
      System.out.println(trajet.pop());
    }
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
