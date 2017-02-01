
package nezet;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import modell.*;

/**
 *
 * @author Karoly
 */
public class AdatBekeres extends JDialog {

  private JButton btOK = new JButton("Mehet");


  public AdatBekeres(JFrame tulajdonos, Dolgozo dolgozo) {
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
    JSpinner sp=new JSpinner(new SpinnerNumberModel(dolgozo.getFizetes(), dolgozo.getMinFizetes(), dolgozo.getMaxFizetés(), 100));
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
        System.out.println("Dolgozo azonositoja :"+dolgozo.getEmpID()+" uj fizu: "+(int)sp.getModel().getValue() );
        try {
          AdatBazisKezeles.modositFizetés(dolgozo.getEmpID(), (int)sp.getModel().getValue());
          dolgozo.setFizetes((int)sp.getModel().getValue());
        } catch (SQLException ex) {
            ex.printStackTrace();
       //   Logger.getLogger(AdatBekeres.class.getName()).log(Level.SEVERE, null, ex);
        }
        JOptionPane.showMessageDialog((Component) e.getSource(), "Itt kéne adatbázisba írni", "Írás", JOptionPane.INFORMATION_MESSAGE);
        dispose();
      }
    });
    setVisible(true);
  }
}  
