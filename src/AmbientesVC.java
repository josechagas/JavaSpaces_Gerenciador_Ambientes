import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by joseLucas on 16/07/17.
 */
public class AmbientesVC {
    private JTabbedPane tabbedPane;
    private JPanel contentPanel;
    private JButton rmAmbientButton;
    private JButton newAmbientButton;

    //manager
    public Manager manager;

    private ArrayList<Ambiente> ambs = new ArrayList<>();

    AmbientesVC(Manager manager){
        this.manager = manager;
        setUpNewAmbienteButton();
        setUpRmAmbienteButton();

        ambs.addAll(SpaceController.getAmbientes(manager));

        setUpTabPanel();

    }


    public JPanel getContentPanel() {
        return contentPanel;
    }



    private void setUpNewAmbienteButton(){

        newAmbientButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                manager.numbOfAmbientes ++;
                manager.nextAmbID++;
                Ambiente newOne = new Ambiente("amb"+manager.nextAmbID,0,0);
                newTab(newOne);
                SpaceController.writeAmb(newOne);
                //send to java space
            }
        });
    }

    private void setUpRmAmbienteButton(){

        rmAmbientButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int response = JOptionPane.showConfirmDialog(getContentPanel(),"Deseja Continuar?"
                        ,"Remover Ambiente Selecionado",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null);
                if(response == 0) {
                    manager.numbOfAmbientes --;

                    int index = rmCurrentTab();
                    SpaceController.removeAmb(ambs.get(index));
                    //remove from java space
                }
            }
        });
    }



    public void setUpTabPanel(){
        this.tabbedPane.removeAll();
        for (Ambiente amb :
                ambs) {
            newTab(amb);
        }
    }


    public void newTab(Ambiente ambiente){
        AmbienteVC ambienteVC = new AmbienteVC(ambiente);
        tabbedPane.addTab(ambiente.name,ambienteVC.getContentPanel());
    }

    public int rmCurrentTab(){
        int index = tabbedPane.getSelectedIndex();
        tabbedPane.removeTabAt(index);
        return index;
    }

}
