package modell;


public class Dolgozo implements Comparable<Dolgozo> {

  private int empID;
  private String nev;
  private int depId;
  private String reszlegNev;
  private String munkakor;
  private int fizetes;
  private int minFizetes;
  private int maxFizetés;

  public Dolgozo(int empID, String nev, int depId, String reszlegNev, String munkakor, int fizetes, int minFizetes, int maxFizetés) {
    this.empID = empID;
    this.nev = nev;
    this.depId = depId;
    this.reszlegNev = reszlegNev;
    this.munkakor = munkakor;
    this.fizetes = fizetes;
    this.minFizetes = minFizetes;
    this.maxFizetés = maxFizetés;
  }

  public int getEmpID() {
    return empID;
  }

  public String getNev() {
    return nev;
  }

  public int getDepId() {
    return depId;
  }

  public String getReszlegNev() {
    return reszlegNev;
  }

  public String getMunkakor() {
    return munkakor;
  }

  public int getFizetes() {
    return fizetes;
  }

  public int getMinFizetes() {
    return minFizetes;
  }

  public int getMaxFizetés() {
    return maxFizetés;
  }

  @Override
  public String toString() {
    return nev;
  }

  @Override
  public int compareTo(Dolgozo masik) {
    return this.nev.compareTo(masik.nev);
  }
}
