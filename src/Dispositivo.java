import net.jini.core.entry.Entry;

/**
 * Created by joseLucas on 16/07/17.
 */
public class Dispositivo implements Entry {

    public String type;
    public Integer id;
    public Integer ambienteID;

    public Dispositivo(){
        type = "disp";
    }

    public Dispositivo(Integer id, Integer ambienteID){
        type = "disp";
        this.id = id;
        this.ambienteID = ambienteID;
    }


    public String getName(){
        return type+id+"."+ambienteID;//+".a"+ambienteID;
    }

}
