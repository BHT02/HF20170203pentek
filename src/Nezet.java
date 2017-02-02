
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

  class Dolgozo implements Comparable<Dolgozo>{
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
  
  class Reszleg implements Comparable<Reszleg>{
    private String reszlegNev;
    private int reszlegId;

    public Reszleg(String reszlegNev, int reszlegId) {
      this.reszlegNev = reszlegNev;
      this.reszlegId = reszlegId;
    }

    public String getReszlegNev() {
      return reszlegNev;
    }

    public int getReszlegId() {
      return reszlegId;
    }
    @Override
    public String toString() {
      return reszlegNev;
    }

    @Override
    public int compareTo(Reszleg masik) {
      return this.reszlegNev.compareTo(masik.getReszlegNev());
    }
  }

public class Nezet extends JFrame implements ActionListener, ListSelectionListener{
  
  private JComboBox cbReszlegLista;
  private JList lDolgozoLista = new JList(new DefaultListModel());
  private JTextField tfDolgozoKeres = new JTextField("Keresendő dolgozó",12);
  private JScrollPane spDolgozoLista = new JScrollPane(lDolgozoLista);

  public Nezet() {
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setTitle("HR Fizetés emelés");
    setSize(600, 600);
    setLocationRelativeTo(this);
    setIconImage(Toolkit.getDefaultToolkit().getImage("image1.jpg"));
    
    JLabel lbKitolto = new JLabel();
    lbKitolto.setPreferredSize(new Dimension(100, 10));
    cbReszlegLista=reszlegListaBetoltes();
    JPanel pn = new JPanel();
    pn.add(new JLabel("Részlegek Lista: "));
    pn.add(cbReszlegLista);
    pn.add(lbKitolto);
    pn.add(new JLabel("Dolgozó keresés: "));
    pn.add(tfDolgozoKeres);
    add(pn, BorderLayout.PAGE_START);
    add(spDolgozoLista);
    setVisible(true);
    cbReszlegLista.addActionListener(this);
    lDolgozoLista.addListSelectionListener(this);
    reszlegListaBetoltes();
    lDolgozoLista.setModel(dolgozoListaBetoltes());
    lDolgozoLista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    
  }
  
  private JComboBox reszlegListaBetoltes() {
    JComboBox cbReszlegLista = new JComboBox();
    cbReszlegLista.addItem(new Reszleg("Mindenki", -1));
    cbReszlegLista.addItem(new Reszleg("Részleg1",1));
    cbReszlegLista.addItem(new Reszleg("Részleg2",2));
    cbReszlegLista.addItem(new Reszleg("Részleg3",3));
    cbReszlegLista.addItem(new Reszleg("Részleg5",4));
    return cbReszlegLista;
  }

  private DefaultListModel dolgozoListaBetoltes() {
    
    DefaultListModel dlm=new DefaultListModel();
    dlm.addElement(new Dolgozo(1, "Gipsz Jakab", 1, "Részeleg 1", "Kovács", 1000, 500, 2000));
    dlm.addElement(new Dolgozo(2, "Gipsz Jakabné", 2, "Részeleg 2", "Monár", 2000, 100, 3000));
    dlm.addElement(new Dolgozo(1, "Nagy Béla", 3, "Részeleg 3", "Kőműves", 1500, 500, 2000));
    return dlm;
  }
  
  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getSource()==cbReszlegLista) {
      Reszleg reszleg = (Reszleg)((JComboBox)e.getSource()).getSelectedItem();
      System.out.println(reszleg.getReszlegNev()+" "+reszleg.getReszlegId());
    }

  }
  @Override
  public void valueChanged(ListSelectionEvent e) {
    if (!e.getValueIsAdjusting()) {
      Dolgozo dolgozo = (Dolgozo) ((JList)e.getSource()).getSelectedValue();
      System.out.println(dolgozo.getNev()+" "+dolgozo.getMunkakor());
      new Adatbekeres((JFrame)SwingUtilities.getRoot((JList)e.getSource()), dolgozo);
    }
  }
  
class Adatbekeres extends JDialog {

  private JButton btOK = new JButton("Mehet");


  public Adatbekeres(JFrame tulajdonos, Dolgozo dolgozo) {
    super(tulajdonos, "Adat bekérés", true);

    setLayout(new BorderLayout());
    JPanel pnButton=new JPanel();
    pnButton.add(btOK);
    add(pnButton, BorderLayout.PAGE_END);
    //setUndecorated(true);
    //setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    setSize(300, 300);

    setLocationRelativeTo(tulajdonos);
    JPanel pn = new JPanel(new GridLayout(5, 1));
    JLabel lbdolgozNev = new JLabel( "Dolgozó neve:             "+dolgozo.getNev());
    JLabel lbFizetes = new JLabel(   "Dolgozó fizetése:         "+dolgozo.getFizetes());
    JLabel lbMaxFizetes = new JLabel("Adható maximális fizrtés: "+dolgozo.getMaxFizetés());
    JLabel lbMinFizetes = new JLabel("Adható minimális fizetés: "+dolgozo.getMinFizetes());
    JSpinner sp=new JSpinner(new SpinnerNumberModel(dolgozo.getFizetes(), dolgozo.getFizetes(), dolgozo.getMaxFizetés(), 100));
    pn.add(lbdolgozNev);
    pn.add(lbFizetes);
    pn.add(lbMaxFizetes);
    pn.add(lbMinFizetes);
    pn.add(sp);
    add(pn);
    //pack();
    btOK.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        System.out.println("Most bezárás");
        dispose();

      }
    });
    setVisible(true);
  }
}  
  public static void main(String[] args) {
    new Nezet();
  }
}
