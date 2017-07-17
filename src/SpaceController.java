/**
 * Created by joseLucas on 16/07/17.
 */

import java.util.ArrayList;

/**
 * This class will execute the methods necessary to comunicate with JavaSpace
 * */

public class SpaceController {

    public static Manager getManager(){
        return new Manager(3,3);
    }



    /**
     * Read mode
     * */
    public static ArrayList<Dispositivo> getDispositivosOfAmbiente(Ambiente ambiente){
        ArrayList<Dispositivo> disps = new ArrayList<>();
        for(int i = 0; i < ambiente.numbOfDisps ; i++){
            disps.add(new Dispositivo("disp"+(i+1),ambiente.name));
        }
        return disps;
    }


    /**
     * Adiciona o novo dispositivo tambem alterando a tupla do ambiente correspondente
     * incrementa o numero de dispositivos e o proximo id
     * */
    public static Boolean writeDisp(Dispositivo disp){
        return true;
    }

    /**
     * Tira o dispositivo tambem alterando a tupla do ambiente correspondente
     * decrementa o numero de dispositivos
     * */
    public static Dispositivo takeDisp(Dispositivo disp){
        return disp;
    }

    /**
     * Remove o dispositivo tambem alterando a tupla do ambiente correspondente
     * decrementa o numero de dispositivos
     * */
    public static Dispositivo removeDisp(Dispositivo disp){
        return disp;
    }





    /**
     * Read mode
     * */
    public static ArrayList<Ambiente> getAmbientes(Manager manager){
        ArrayList<Ambiente> ambs = new ArrayList<>();
        for(int i = 0; i < manager.numbOfAmbientes ; i++){
            ambs.add(new Ambiente("amb"+(i+1),5,5));
        }
        return ambs;
    }

    /**
     * Adiciona o novo Ambiente tambem alterando a tupla do manager correspondente
     * incrementa o numero de ambientes e o proximo id
     * */
    public static Boolean writeAmb(Ambiente amb){
        return true;
    }

    /**
     * Remove o ambiente tambem alterando a tupla do manager correspondente
     * decrementa o numero de ambientes
     * */
    public static Ambiente removeAmb(Ambiente amb){
        return amb;
    }
}
