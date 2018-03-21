import java.util.HashSet;
import java.util.Set;

public class Aeroport {
  private String iata;
  private String nom;
  private String pays;
  private String ville;
  private double latitude;
  private double longitude;
  private Set<Vol> volsSortants = new HashSet<>();

  private double cout = Double.MAX_VALUE;
  private Vol vol;


  public Aeroport() {}

  public Aeroport(String iata, String nom, String pays, String ville, double lat, double lon) {
    this.iata = iata;
    this.nom = nom;
    this.pays = pays;
    this.ville = ville;
    this.latitude = lat;
    this.longitude = lon;
  }

  public String getIata() {
    return iata;
  }

  public double getLatitude() {
    return latitude;
  }

  public double getLongitude() {
    return longitude;
  }

  public void setIata(String iata) {
    this.iata = iata;
  }

  public String getNom() {
    return nom;
  }

  public void setNom(String nom) {
    this.nom = nom;
  }

  public String getPays() {
    return pays;
  }

  public void setPays(String pays) {
    this.pays = pays;
  }

  public String getVille() {
    return ville;
  }

  public void setVille(String ville) {
    this.ville = ville;
  }

  public void setLatitude(double latitude) {
    this.latitude = latitude;
  }

  public void setLongitude(double longitude) {
    this.longitude = longitude;
  }

  public void ajouterVolSortant(Vol vol) {
    volsSortants.add(vol);
  }

  public Set<Vol> getVolsSortants() {
    return volsSortants;
  }

  public double getCout() {
    return cout;
  }

  public void setCout(double cout) {
    this.cout = cout;
  }

  public Vol getVol() {
    return vol;
  }

  public void setVol(Vol vol) {
    this.vol = vol;
  }

  @Override
  public String toString() {
    return "Aeroport [iata=" + iata + ", nom=" + nom + ", pays=" + pays + ", ville=" + ville
        + ", latitude=" + latitude + ", longitude=" + longitude + ", cout=" + cout + ", vol=" + vol
        + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((iata == null) ? 0 : iata.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Aeroport other = (Aeroport) obj;
    if (iata == null) {
      if (other.iata != null)
        return false;
    } else if (!iata.equals(other.iata))
      return false;
    return true;
  }

}
