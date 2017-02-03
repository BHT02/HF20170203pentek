package nezet;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import modell.*;


public class FoAblak extends JFrame implements ActionListener, ListSelectionListener {

  private JComboBox cbReszlegLista;
  private JList lDolgozoLista = new JList(new DefaultListModel());
  private JTextField tfDolgozoKeres = new JTextField("Keresendő dolgozó", 12);
  private JScrollPane spDolgozoLista = new JScrollPane(lDolgozoLista);
  private AdatBazisKezeles modell;
  DefaultListModel dlm = new DefaultListModel();

  public FoAblak(AdatBazisKezeles modell) {
    this.modell=modell;
    System.out.println("eddig jó");
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setTitle("HR Fizetés emelés");
    setSize(700, 500);
    setLocationRelativeTo(this);
    setIconImage(Toolkit.getDefaultToolkit().getImage("image1.jpg"));

    JLabel lbKitolto = new JLabel();
    lbKitolto.setPreferredSize(new Dimension(100, 10));
    cbReszlegLista = reszlegListaBetoltes();
    JPanel pn = new JPanel();
    pn.add(new JLabel("Részlegek Lista: "));
    pn.add(cbReszlegLista);
    pn.add(lbKitolto);
    pn.add(new JLabel("Dolgozó keresés: "));
    tfDolgozoKeres.setText("");
    tfDolgozoKeres.getDocument().addDocumentListener(new MyDocumentListener());
    tfDolgozoKeres.getDocument().putProperty("name", "Text Area");
    // ide jön majd valami
    
    
    
    pn.add(tfDolgozoKeres);
    add(pn, BorderLayout.PAGE_START);
    add(spDolgozoLista);
    setVisible(true);
    cbReszlegLista.addActionListener(this);
    lDolgozoLista.addListSelectionListener(this);
    reszlegListaBetoltes();
    lDolgozoLista.setModel(dolgozoListaBetoltes(-1));
    lDolgozoLista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

  }

  private JComboBox reszlegListaBetoltes() {
    JComboBox cbReszlegLista = new JComboBox();
    ArrayList<Reszleg> reszlegek=modell.lekerdezReszleg();
    cbReszlegLista.addItem(new Reszleg("Mindegyik", -1));
    for (Reszleg reszleg : reszlegek) {
      cbReszlegLista.addItem(reszleg);
    }
    return cbReszlegLista;
  }

  private DefaultListModel dolgozoListaBetoltes(int reszlegID) {

    dlm = new DefaultListModel();
    ArrayList<Dolgozo> dolgozok = modell.lekerdezDolgozokListajaAdottReszleghez(reszlegID);
    for (Dolgozo dolgozo : dolgozok) {
      dlm.addElement(dolgozo);

    }
    return dlm;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == cbReszlegLista) {
      Reszleg reszleg = (Reszleg) ((JComboBox) e.getSource()).getSelectedItem();
      lDolgozoLista.setModel(dolgozoListaBetoltes(reszleg.getReszlegId()));
    }

  }

  @Override
  public void valueChanged(ListSelectionEvent e) {
    if (!e.getValueIsAdjusting()) {
      Dolgozo dolgozo = (Dolgozo) ((JList) e.getSource()).getSelectedValue();
      if(dolgozo != null){
        int dolgozoInndex = dlm.indexOf(dolgozo);
        new AdatBekeres(this, dolgozo, modell);
        dlm.setElementAt(dolgozo, dolgozoInndex);
      }
    }
  }

      class MyDocumentListener implements DocumentListener {

    final String newline = "\n";

    public void insertUpdate(DocumentEvent e) {
      updateLog(e);
    }

    public void removeUpdate(DocumentEvent e) {
      updateLog(e);
    }

    public void changedUpdate(DocumentEvent e) {
      //Plain text components don't fire these events.
    }

    public void updateLog(DocumentEvent e) {
      if (tfDolgozoKeres.getText().length() > 0) {
        String keres = tfDolgozoKeres.getText().toLowerCase();
        DefaultListModel dlmSzukitett = new DefaultListModel();
        for (int i = 0; i < dlm.getSize(); i++) {
          if (dlm.getElementAt(i).toString().toLowerCase().contains(keres)) {
            dlmSzukitett.addElement(dlm.getElementAt(i));
          }
        }
        if (dlmSzukitett.size() == 0) {
          dlmSzukitett.addElement("Nincs találat");
        }
        lDolgozoLista.setModel(dlmSzukitett);
        //System.out.println(dlm);
      } else {
        lDolgozoLista.setModel(dlm);
      }
    }
  }
      
}
