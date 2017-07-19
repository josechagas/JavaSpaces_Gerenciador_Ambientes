import net.jini.core.entry.Entry;

/**
 * Created by joseLucas on 16/07/17.
 */
public class Ambiente implements Entry {

    public String type;
    public Integer id;
    public Integer numbOfDisps;
    public Integer nextDispID;

    public Ambiente(){
        type = "amb";
    }

    public Ambiente(Integer id,Integer numbOfDisps,Integer nextDispID){
        type = "amb";
        this.id = id;
        this.numbOfDisps = numbOfDisps;
        this.nextDispID = nextDispID;
    }



    public String getName(){
        return type+id;
    }
}
