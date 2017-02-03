package nezet;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.PopupMenu;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import modell.*;


public class FoAblak extends JFrame 
  implements ActionListener, ListSelectionListener, MouseListener, KeyListener {

  private JComboBox cbReszlegLista;
  JLabel lbTalalat=new JLabel("Nincs találat");
  private JList lDolgozoLista = new JList(new DefaultListModel());
  private JTextField tfDolgozoKeres = new JTextField("Keresendő dolgozó", 12);
  private JScrollPane spDolgozoLista = new JScrollPane(lDolgozoLista);
  private AdatBazisKezeles modell;
  DefaultListModel dlm = new DefaultListModel();

  public FoAblak(AdatBazisKezeles modell) {
    this.modell=modell;
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setTitle("HR Fizetésemelés");
    setSize(700, 500);
    setLocationRelativeTo(this);
    setIconImage(Toolkit.getDefaultToolkit().getImage("image1.jpg"));
    
    cbReszlegLista = reszlegListaBetoltes();  
    
    JPanel pnReszlegek=new JPanel(new GridLayout(2, 1));
    pnReszlegek.add(new JLabel("Részlegek: "));
    pnReszlegek.add(cbReszlegLista);
    
    JPanel pnDolgozoKereses=new JPanel(new GridLayout(2, 1));
    pnDolgozoKereses.add(new JLabel("Dolgozó keresés: "));
    tfDolgozoKeres.setText("");
    tfDolgozoKeres.getDocument().addDocumentListener(new MyDocumentListener());
    tfDolgozoKeres.getDocument().putProperty("name", "Text Area");    
    pnDolgozoKereses.add(tfDolgozoKeres);    
    
    lbTalalat.setVisible(false);
    lbTalalat.setForeground(Color.blue);
    lbTalalat.setHorizontalAlignment(SwingConstants.CENTER);
    
    JPanel pn = new JPanel(new GridLayout(1, 3));
    pn.add(panelKeszit(pnReszlegek));
    pn.add(panelKeszit(pnDolgozoKereses));
    pn.add(lbTalalat);
    add(pn, BorderLayout.PAGE_START);    
    add(new JPanel(), BorderLayout.LINE_START);
    add(new JPanel(), BorderLayout.LINE_END);
    add(new JPanel(), BorderLayout.PAGE_END);
    
    add(spDolgozoLista);
    setVisible(true);
    cbReszlegLista.addActionListener(this);
    //lDolgozoLista.addListSelectionListener(this);
    lDolgozoLista.addMouseListener(this);
    lDolgozoLista.addKeyListener(this);
    reszlegListaBetoltes();
    lDolgozoLista.setModel(dolgozoListaBetoltes(-1));
    lDolgozoLista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    lDolgozoLista.setSelectedIndex(0);
    lDolgozoLista.requestFocus();
  }
  
  private JPanel panelKeszit(JPanel pnReszlegek) {
    JPanel ujPn=new JPanel();
    ujPn.add(new JPanel(), BorderLayout.PAGE_START);
    ujPn.add(new JPanel(), BorderLayout.PAGE_END);
    ujPn.add(new JPanel(), BorderLayout.LINE_START);
    ujPn.add(new JPanel(), BorderLayout.LINE_START);
    ujPn.add(pnReszlegek, BorderLayout.CENTER);
    return ujPn;
  }

  private JComboBox reszlegListaBetoltes() {
    JComboBox cbReszlegLista = new JComboBox();
    ArrayList<Reszleg> reszlegek=modell.lekerdezReszleg();
    cbReszlegLista.addItem(new Reszleg("Összes dolgozó", -1));
    for (Reszleg reszleg : reszlegek)
      cbReszlegLista.addItem(reszleg);
    return cbReszlegLista;
  }

  private DefaultListModel dolgozoListaBetoltes(int reszlegID) {
    dlm = new DefaultListModel();
    ArrayList<Dolgozo> dolgozok = modell.lekerdezDolgozokListajaAdottReszleghez(reszlegID);
    for (Dolgozo dolgozo : dolgozok)
      dlm.addElement(dolgozo);
    return dlm;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == cbReszlegLista) {
      Reszleg reszleg = (Reszleg) ((JComboBox) e.getSource()).getSelectedItem();
      lDolgozoLista.setModel(dolgozoListaBetoltes(reszleg.getReszlegId()));
      tfDolgozoKeres.setText("");
      lbTalalat.setVisible(false);
      lDolgozoLista.setSelectedIndex(0); 
      lDolgozoLista.requestFocus();
    }
  }

  public void valueChanged(ListSelectionEvent e) {
//    if (!e.getValueIsAdjusting()) {
//      if(((JList)e.getSource()).getSelectedValue() instanceof Dolgozo){
//        Dolgozo dolgozo = (Dolgozo) ((JList) e.getSource()).getSelectedValue();
//        if(dolgozo != null){
//          int dolgozoInndex = dlm.indexOf(dolgozo);
//          new AdatBekeres(this, dolgozo, modell);
//          dlm.setElementAt(dolgozo, dolgozoInndex);
//        }
//      }
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
        if (dlmSzukitett.size() == 0)
          //dlmSzukitett.addElement("Nincs találat");
          lbTalalat.setVisible(true);
        else
          lbTalalat.setVisible(false);
        lDolgozoLista.setModel(dlmSzukitett);
        //System.out.println(dlm);
      } 
      else {
        lbTalalat.setVisible(false);
        lDolgozoLista.setModel(dlm);
      }
    }
  }
  
   @Override
  public void mouseClicked(MouseEvent e) {
    //if (!e.getValueIsAdjusting()) { //ez mit csinal? 
    if (e.getSource()==lDolgozoLista) { //Ide kell try-catch (exception e) Gergely??? Miert? 
      Dolgozo dolgozo = (Dolgozo) ((JList) e.getSource()).getSelectedValue();
      DefaultListModel dlm = (DefaultListModel) lDolgozoLista.getModel();
      if(dolgozo != null){
        int dolgozoIndex = dlm.indexOf(dolgozo);
        new AdatBekeres(this, dolgozo, modell);
        //new AdatBekeres((JFrame) SwingUtilities.getRoot((JList) e.getSource()), dolgozo, modell);
        dlm.setElementAt(dolgozo, dolgozoIndex);
      }
    }
  }
  
  @Override
  public void mousePressed(MouseEvent e) {
    ;
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    ;
  }

  @Override
  public void mouseEntered(MouseEvent e) {
    ;
  }

  @Override
  public void mouseExited(MouseEvent e) {
    ;
  }

  @Override
  public void keyTyped(KeyEvent e) {
    ;
  }

  @Override
  public void keyPressed(KeyEvent e) {
    ;
  }

  @Override
  public void keyReleased(KeyEvent e) {
    if (e.getSource()==lDolgozoLista)
      if (e.getKeyCode()==KeyEvent.VK_ENTER)  {
        Dolgozo dolgozo = (Dolgozo) ((JList) e.getSource()).getSelectedValue();
        DefaultListModel dlm = (DefaultListModel) lDolgozoLista.getModel();
        if(dolgozo != null){
          int dolgozoInndex = dlm.indexOf(dolgozo);
          //System.out.println(dolgozo.getNev() + " " + dolgozo.getMunkakor());
          new AdatBekeres((JFrame) SwingUtilities.getRoot((JList) e.getSource()), dolgozo, modell);
          dlm.setElementAt(dolgozo, dolgozoInndex);
        }
    }
  }
      
}
