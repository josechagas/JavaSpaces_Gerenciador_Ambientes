/**
 * Created by joseLucas on 16/07/17.
 */
public class Ambiente {

    public String name;
    public Integer numbOfDisps;
    public Integer nextDispID;

    Ambiente(){

    }

    Ambiente(String name,Integer numbOfDisps,Integer nextDispID){
        this.name = name;
        this.numbOfDisps = numbOfDisps;
        this.nextDispID = nextDispID;
    }

}
