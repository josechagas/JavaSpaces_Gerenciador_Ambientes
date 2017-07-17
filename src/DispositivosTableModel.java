import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

/**
 * Created by joseLucas on 16/07/17.
 */
public class DispositivosTableModel extends AbstractTableModel {

    public ArrayList<Dispositivo> dispositivos;


    DispositivosTableModel(){
        dispositivos = new ArrayList<Dispositivo>();
    }


    @Override
    public int getRowCount() {
        /*if(dispositivos.size() % 2 == 0){
            return dispositivos.size()/2;
        }
        else{
            return (dispositivos.size() + 1)/2;
        }*/
        return dispositivos.size();

    }

    @Override
    public int getColumnCount() {
        return 1;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return Dispositivo.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    @Override
    public String getColumnName(int column) {
        return "DISPOSITIVOS";
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        int pos = rowIndex + getRowCount()*columnIndex;
        if(dispositivos.size() > pos){
            return dispositivos.get(pos);
        }
        return null;
    }

}
