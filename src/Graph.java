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
    Map<Aeroport, Double> def = new HashMap<>();
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
          def.put(aeroportCourant, aeroportCourant.getCout());
          destination.setVol(vol);
        }
      }
      if (def.containsKey(dest)) {
        break;
      }
    }
    // afficherRoute(src, dest);
  }

  private void afficherRoute(Aeroport src, Aeroport dest) {
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
  public void calculerItineraireMinimisantNombreVol(String iataSrc, String iataDest, String path) {

    Deque<Aeroport> file = new ArrayDeque<>();

    Aeroport src = aeroports.get(iataSrc);
    Aeroport dest = aeroports.get(iataDest);

    file.add(src);
    ajouterConnexions(dest, file);

    afficherRoute(src, dest);
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

  public void ajouterVol(Vol vol) {
    vols.add(vol);
  }

  public void ajouterAeroport(String iata, Aeroport aeroport) {
    aeroports.put(iata, aeroport);
  }
}
