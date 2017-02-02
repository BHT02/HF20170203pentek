
package nezet;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
  public AdatBekeres(JFrame tulajdonos, Dolgozo dolgozo, AdatBazisKezeles modell) {
    super(tulajdonos, "Adat bekérés", true);
    setLayout(new BorderLayout());
    JPanel pnButton=new JPanel();
    pnButton.add(btOK);
    add(pnButton, BorderLayout.PAGE_END);
    //setUndecorated(true);
    //setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    setSize(300, 300);

    setLocationRelativeTo(tulajdonos);
    int minFizetes = modell.lekerdezMinFizetes(dolgozo.getMunkakor());
    int maxFizetes = modell.lekerdezMaxFizetés(dolgozo.getMunkakor());
    int aktFizetes = dolgozo.getFizetes();
    int emeles5szazalek = Math.round(aktFizetes*1.05F);
    int csokkentes5szazalek =  Math.round(aktFizetes*0.95F);
    int adhatoMax = maxFizetes>emeles5szazalek?emeles5szazalek:maxFizetes;
    int adhatoMin = minFizetes<csokkentes5szazalek?csokkentes5szazalek:minFizetes;
    
    System.out.println("aktfizu: "+aktFizetes+", 5% plusz: "+emeles5szazalek+
            ", 5% minusz: "+csokkentes5szazalek+", maxfizu: "+maxFizetes+", min fizu: "+minFizetes+
            ", adhatoMax: "+adhatoMax+", adhatoMin:. "+adhatoMin);
    
    JPanel pn = new JPanel(new GridLayout(5, 1));
    JLabel lbdolgozNev = new JLabel( "Dolgozó neve:             "+dolgozo.getNev());
    JLabel lbFizetes = new JLabel(   "Dolgozó fizetése:         "+dolgozo.getFizetes());
    JLabel lbMaxFizetes = new JLabel("Adható maximális fizrtés: "+minFizetes);
    JLabel lbMinFizetes = new JLabel("Adható minimális fizetés: "+maxFizetes);
    
    JSpinner sp=new JSpinner(new SpinnerNumberModel(aktFizetes, adhatoMin, adhatoMax, 50));
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
        //Ha nem valtozott a fizu osszeg, akkor NE mentsuk!
        if ((int)sp.getModel().getValue()!=aktFizetes) {
          //boolean siker = AdatBazisKezeles.modositFizetés(dolgozo.getEmpID(), (int)sp.getModel().getValue());
          dolgozo.setFizetes((int)sp.getModel().getValue());
        }else{
          JOptionPane.showMessageDialog((Component) e.getSource(), 
                    "Ugyanarra nem lehet módosítani a fizetést, ami\n korábban volt!", 
                    "Figyelmeztetés", 
                    JOptionPane.ERROR_MESSAGE);          
        }
        dispose();
      }
    });
    setVisible(true);
  }
}  
