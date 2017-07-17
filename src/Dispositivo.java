import net.jini.core.entry.Entry;

/**
 * Created by joseLucas on 16/07/17.
 */
public class Dispositivo implements Entry {

    public String name;
    public String ambiente;

    Dispositivo(){

    }

    Dispositivo(String name, String ambiente){
        this.name = name;
        this.ambiente = ambiente;
    }

}
