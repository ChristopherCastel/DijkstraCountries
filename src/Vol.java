public class Vol {
  private double distance;
  private Aeroport origine;
  private Aeroport destination;

  public Vol(double distance, Aeroport origine, Aeroport destination) {
    this.distance = distance;
    this.origine = origine;
    this.destination = destination;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    long temp;
    temp = Double.doubleToLongBits(distance);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    result = prime * result + ((destination == null) ? 0 : destination.hashCode());
    result = prime * result + ((origine == null) ? 0 : origine.hashCode());
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
    Vol other = (Vol) obj;
    if (Double.doubleToLongBits(distance) != Double.doubleToLongBits(other.distance))
      return false;
    if (destination == null) {
      if (other.destination != null)
        return false;
    } else if (!destination.equals(other.destination))
      return false;
    if (origine == null) {
      if (other.origine != null)
        return false;
    } else if (!origine.equals(other.origine))
      return false;
    return true;
  }
}
