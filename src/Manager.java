import net.jini.core.entry.Entry;

/**
 * Created by joseLucas on 16/07/17.
 */
public class Manager implements Entry{

    public String code = "manager";

    public Integer numbOfAmbientes;
    public Integer nextAmbID;

    Manager(){

    }

    Manager(Integer numbOfAmbientes, Integer nextAmbID){
        this.numbOfAmbientes = numbOfAmbientes;
        this.nextAmbID = nextAmbID;
    }

}
