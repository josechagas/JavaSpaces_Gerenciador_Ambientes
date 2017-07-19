import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Function;

/**
 * Created by joseLucas on 16/07/17.
 */
public class AmbienteVC{
    private JPanel contentPanel;
    private JButton newDispositiveButton;


    private JTable dispsTable;
    private DispositivosTableModel tableModel;

    //Ambiente
    public Ambiente ambiente;

    private Function<String,Ambiente> ambWithName;

    private Function<Ambiente,Integer> updateAmbTab;

    public void setAmbWithID(Function<String, Ambiente> ambWithName) {
        this.ambWithName = ambWithName;
    }

    public void setUpdateAmbTab(Function<Ambiente, Integer> updateAmbTab) {
        this.updateAmbTab = updateAmbTab;
    }

    AmbienteVC(Ambiente ambiente){

        this.ambiente = ambiente;
        setUpNewDispositiveButton();

        ArrayList<Dispositivo> disps = SpaceController.getDispositivosOfAmbiente(true,ambiente);
        Collections.reverse(disps);
        setUpDispsTable(disps);

    }

    public JPanel getContentPanel() {
        return contentPanel;
    }


    private void setUpNewDispositiveButton(){
        newDispositiveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Dispositivo disp = new Dispositivo(ambiente.nextDispID,ambiente.id);

                ambiente.numbOfDisps ++;
                ambiente.nextDispID ++;

                if(SpaceController.addDisp(disp)){
                    tableModel.dispositivos.add(disp);

                    if(!SpaceController.updateAmbiente(ambiente)){
                        ambiente.numbOfDisps --;
                        ambiente.nextDispID --;
                        SpaceController.removeDisp(disp);

                        System.out.println("falha ao tentar atualizar o ambiente apos adicionar dispositivo");
                        System.out.println("Dispositivo adicionado foi removido do espaco");

                    }
                    else{
                        System.out.println("adicionou o dispositivo com sucesso "+disp.getName());
                    }
                }
                else{
                    ambiente.numbOfDisps --;
                    ambiente.nextDispID --;
                    System.out.println("falha ao tentar adicionar um dispositivo");
                }

                tableModel.fireTableDataChanged();//updates table items
            }
        });
    }

    private void addDispToAmb(Ambiente amb){
        Dispositivo newOne = new Dispositivo(amb.nextDispID,amb.id);

        amb.numbOfDisps ++;
        amb.nextDispID ++;

        if(SpaceController.addDisp(newOne)){
            tableModel.dispositivos.add(newOne);

            if(!SpaceController.updateAmbiente(amb)){
                amb.numbOfDisps --;
                amb.nextDispID --;
                SpaceController.removeDisp(newOne);

                System.out.println("falha ao tentar atualizar o ambiente apos adicionar dispositivo");
                System.out.println("Dispositivo adicionado foi removido do espaco");

            }
            else{
                System.out.println("adicionou o dispositivo com sucesso "+newOne.getName());
            }
        }
        else{
            amb.numbOfDisps --;
            amb.nextDispID --;
            System.out.println("falha ao tentar adicionar um dispositivo");
        }
    }

    private void moveDispAction(Dispositivo disp,String ambName){
        String idStr = ambName.substring(ambName.length() - 1,ambName.length());
        Integer id = new Integer(idStr);

        Ambiente amb = ambWithName.apply(ambName);

        if(amb != null){
            //move o dispositivo
            removeDispAction(disp);
            addDispToAmb(amb);

            updateAmbTab.apply(amb);
        }
        else{
            JOptionPane.showMessageDialog(getContentPanel(),
                    "Ambiente "+ambName+" nao existe","Erro",JOptionPane.INFORMATION_MESSAGE);
        }
    }


    private void removeDispAction(Dispositivo disp){
        tableModel.dispositivos.removeIf(dispositivo -> {
            return dispositivo.getName().equals(disp.getName());
        });
        ambiente.numbOfDisps --;
        if(SpaceController.removeDisp(disp) != null){
            if(!SpaceController.updateAmbiente(ambiente)){
                System.out.println("falha ao tentar atualizar o ambiente apos remocao de dispositivo");
            }
            else{
                System.out.println("Removeu o dispositivo e atualizou o ambiente com sucesso");
            }
        }

        tableModel.fireTableStructureChanged();//updates cells, columns etc
    }




    //#### JTable classes and methods
    private void setUpDispsTable(ArrayList<Dispositivo> disps){
        tableModel = new DispositivosTableModel();

        tableModel.dispositivos.addAll(disps);

        dispsTable.setModel(tableModel);
        dispsTable.setDefaultRenderer(Dispositivo.class,new DispositivosRenderer());
        dispsTable.setDefaultEditor(Dispositivo.class,new DispositivosEditor());
        dispsTable.setRowHeight(60);
        dispsTable.setRowMargin(10);

    }



    public class DispositivosRenderer  implements TableCellRenderer{
        private DispositivoVC reusableCell = new DispositivoVC();

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

            if(value != null){
                //DispositivoVC disp = new DispositivoVC((Dispositivo) value);
                DispositivoVC disp = reusableCell.update((Dispositivo) value);
                /*
                disp.setMoveItemBlock(dispositivo -> {

                    System.out.println("mover "+dispositivo.getName());
                    JOptionPane.showInputDialog(getContentPanel(),"Qual o novo ambiente?",
                            "Mover "+dispositivo.getName(),JOptionPane.QUESTION_MESSAGE);

                    //String ambName = JOptionPane.showInputDialog(getContentPanel(),"Qual o novo ambiente?");

                    return 1;
                });

                disp.setRemoveItemBlock(dispositivo -> {

                    int response = JOptionPane.showConfirmDialog(getContentPanel(),"Deseja Continuar?"
                            ,"Remover "+dispositivo.getName(),JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null);
                    if(response == 0) {
                        removeDispAction(dispositivo);
                    }
                    return 1;

                });
                */
                return disp.getContentPane();
            }
            return null;
        }
    }


    public class DispositivosEditor extends AbstractCellEditor  implements TableCellEditor{
        private DispositivoVC reusableCell = new DispositivoVC();


        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {

            if(value != null){
                //DispositivoVC disp = new DispositivoVC((Dispositivo) value);
                DispositivoVC disp = reusableCell.update((Dispositivo) value);

                disp.setMoveItemBlock(dispositivo -> {
                    //System.out.println("mover "+dispositivo.getName());

                    String name = JOptionPane.showInputDialog(getContentPanel(),"Qual o novo ambiente?",
                            "Mover "+dispositivo.getName(),JOptionPane.QUESTION_MESSAGE);

                    if(name != null){
                        moveDispAction(dispositivo,name);
                    }

                    return 1;
                });

                disp.setRemoveItemBlock(dispositivo -> {

                    int response = JOptionPane.showConfirmDialog(getContentPanel(),"Deseja Continuar?"
                            ,"Remover "+dispositivo.getName(),JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null);
                    if(response == 0) {
                        removeDispAction(dispositivo);
                    }
                    return 1;

                });

                return disp.getContentPane();
            }

            return null;
        }

        @Override
        public Object getCellEditorValue() {
            return null;
        }
    }
}
