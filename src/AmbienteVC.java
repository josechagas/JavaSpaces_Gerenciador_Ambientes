import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

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


    AmbienteVC(Ambiente ambiente){
        this.ambiente = ambiente;
        setUpNewDispositiveButton();

        ArrayList<Dispositivo> disps = SpaceController.getDispositivosOfAmbiente(ambiente);

        setUpDispsTable(disps);
    }

    public JPanel getContentPanel() {
        return contentPanel;
    }


    private void setUpNewDispositiveButton(){
        newDispositiveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                ambiente.numbOfDisps ++;
                ambiente.nextDispID ++;
                Dispositivo disp = new Dispositivo("disp"+ambiente.nextDispID,ambiente.name);
                addDispositiveToTable(disp);

                SpaceController.writeDisp(disp);
                //send to java space
            }
        });
    }

    private void moveDispAction(Dispositivo disp){
        SpaceController.takeDisp(disp);
        //take from java space
        ambiente.numbOfDisps --;
        System.out.println("moving");
        //update and send to java space
        SpaceController.writeDisp(disp);//updated to new ambiente
    }

    private void removeDispAction(Dispositivo disp){
        ambiente.numbOfDisps --;
        this.rmDispositiveFromTable(disp);
        System.out.println("removing");
        SpaceController.removeDisp(disp);
        //remove from java space
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

    private void rmDispositiveFromTable(Dispositivo disp){
        tableModel.dispositivos.removeIf(dispositivo -> {
            return dispositivo.name.equals(disp.name);
        });
        tableModel.fireTableStructureChanged();//updates cells, columns etc
    }

    private void addDispositiveToTable(Dispositivo disp){
        tableModel.dispositivos.add(disp);
        tableModel.fireTableDataChanged();//updates table items
    }


    public class DispositivosRenderer  implements TableCellRenderer{
        private DispositivoVC reusableCell = new DispositivoVC();

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

            if(value != null){
                //DispositivoVC disp = new DispositivoVC((Dispositivo) value);
                DispositivoVC disp = reusableCell.update((Dispositivo) value);
                disp.setMoveItemBlock(dispositivo -> {
                    moveDispAction(dispositivo);
                    return 1;
                });

                disp.setRemoveItemBlock(dispositivo -> {

                    int response = JOptionPane.showConfirmDialog(getContentPanel(),"Deseja Continuar?"
                            ,"Remover "+dispositivo.name,JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null);
                    if(response == 0) {
                        removeDispAction(dispositivo);
                    }
                    return 1;

                });

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
                    moveDispAction(dispositivo);
                    return 1;
                });

                disp.setRemoveItemBlock(dispositivo -> {

                    int response = JOptionPane.showConfirmDialog(getContentPanel(),"Deseja Continuar?"
                            ,"Remover "+dispositivo.name,JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null);
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
