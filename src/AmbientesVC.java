import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by joseLucas on 16/07/17.
 */
public class AmbientesVC {
    private JTabbedPane tabbedPane;
    private JPanel contentPanel;
    private JButton rmAmbientButton;
    private JButton newAmbientButton;

    private ArrayList<Ambiente> ambs = new ArrayList<>();

    AmbientesVC(){
        setUpNewAmbienteButton();
        setUpRmAmbienteButton();

        ambs.addAll(SpaceController.getAmbientes(true,Main.manager));
        Collections.reverse(ambs);
        setUpTabPanel();

    }

    public Ambiente ambienteForName(String name){
        for (Ambiente a : ambs) {
            if (a.getName().equals(name)) {
                return a;
            }
        }
        return null;
    }

    public void updateTabOfAmb(Ambiente amb){
        int index = ambs.indexOf(amb);

        tabbedPane.removeTabAt(index);

    }

    public JPanel getContentPanel() {
        return contentPanel;
    }



    private void setUpNewAmbienteButton(){

        newAmbientButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Ambiente newOne = new Ambiente(Main.manager.nextAmbID,0,0);
                Main.manager.nextAmbID++;
                Main.manager.numbOfAmbientes ++;
                if(SpaceController.addAmb(newOne)){
                    ambs.add(newOne);

                    if(!SpaceController.updateManager(Main.manager)){
                        Main.manager.nextAmbID--;
                        Main.manager.numbOfAmbientes--;
                        SpaceController.removeAmb(newOne);
                        System.out.println("falha ao tentar atualizar o manager apos adicionar ambiente");
                        System.out.println("Ambiente adicionado foi removido do espaco");
                    }
                    else{
                        System.out.println("adicionou o ambiente com sucesso "+newOne.getName());
                    }
                }
                else{
                    Main.manager.nextAmbID--;
                    Main.manager.numbOfAmbientes--;
                    System.out.println("falha ao tentar adicionar um ambiente");
                }

                newTab(newOne);


                /*ambiente.numbOfDisps ++;
                ambiente.nextDispID ++;

                if(SpaceController.addDisp(disp)){
                    tableModel.dispositivos.add(disp);

                    if(!SpaceController.updateAmbiente(ambiente)){
                        System.out.println("falha ao tentar atualizar o ambiente apos adicionar dispositivo");
                    }
                    else{
                        ambiente.numbOfDisps --;
                        ambiente.nextDispID --;
                        SpaceController.removeDisp(disp);
                        System.out.println("adicionou o ambiente com sucesso "+disp.getName());
                    }
                }
                else{
                    ambiente.numbOfDisps --;
                    ambiente.nextDispID --;
                    System.out.println("falha ao tentar adicionar um dispositivo");
                }*/
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
                    int index = rmCurrentTab();
                    Ambiente amb = ambs.remove(index);
                    if(SpaceController.removeAmb(amb) != null){
                        Main.manager.numbOfAmbientes --;

                        if(!SpaceController.updateManager(Main.manager)){
                            System.out.println("falha ao tentar atualizar o manager apos remocao de amabiente");
                        }
                    }
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
        ambienteVC.setAmbWithID(name -> {

            return ambienteForName(name);

        });

        ambienteVC.setUpdateAmbTab(ambiente1 -> {
            this.updateTabOfAmb(ambiente1);
            return 1;
        });

        tabbedPane.addTab(ambiente.getName(),ambienteVC.getContentPanel());
    }

    public int rmCurrentTab(){
        int index = tabbedPane.getSelectedIndex();
        tabbedPane.removeTabAt(index);
        return index;
    }

}
