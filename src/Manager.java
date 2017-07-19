import net.jini.core.entry.Entry;

/**
 * Created by joseLucas on 16/07/17.
 */
public class Manager implements Entry{

    public String type;
    public Integer numbOfAmbientes;
    public Integer nextAmbID;

    public Manager(){
        type = "manager";
    }

    public Manager(Integer numbOfAmbientes, Integer nextAmbID){
        this.numbOfAmbientes = numbOfAmbientes;
        this.nextAmbID = nextAmbID;
        type = "manager";
    }

    public Manager(String type){
        this.type = type;
    }
}
