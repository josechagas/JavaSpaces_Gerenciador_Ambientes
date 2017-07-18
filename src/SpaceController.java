/**
 * Created by joseLucas on 16/07/17.
 */

import net.jini.space.JavaSpace;

import java.util.ArrayList;

/**
 * This class will execute the methods necessary to comunicate with JavaSpace
 * */

public class SpaceController {
    private static JavaSpace space = null;



    public static Boolean findJavaSpaceService(){
        try{
            Lookup finder = new Lookup(JavaSpace.class);
            JavaSpace space = (JavaSpace) finder.getService();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return true;
    }


    //----------------------- Manager ---------------------------


    //read mode
    public static Manager getManager(){
        return new Manager(3,3);
    }



    public static void cleanUp(){
        Manager manager = getManager();
        removeAllAmb(manager);

    }


    //----------------------- Dispositivo ---------------------------

    /**
     * Read all dispositivos do ambiente correspondente
     * Read mode
     * */
    public static ArrayList<Dispositivo> getDispositivosOfAmbiente(Ambiente ambiente){
        ArrayList<Dispositivo> disps = new ArrayList<>();
        int start = ambiente.nextDispID - 1;
        int end = start - ambiente.numbOfDisps;
        for(int i = start; i > end; i--){
            String name = "disp"+(i);
            //caso nao tenha a tupla de nome name e disps.size() < ambiente.numbOfDisps e end > 0 faz end --
            disps.add(new Dispositivo(name,ambiente.name));
        }
        return disps;
    }

    /**Take mode
     * Remove todos os Dispositivos do ambiente
     * */
    public static void removeAllDispsFrom(Ambiente ambiente){
        ArrayList<Dispositivo> removedDisps = new ArrayList<>();
        int start = ambiente.nextDispID - 1;
        int end = start - ambiente.numbOfDisps;

        for(int i = start; i > end; i--){
            String name = "disp"+(i);
            Dispositivo disp = new Dispositivo(name,ambiente.name);
            removedDisps.add(disp);
            //caso nao tenha a tupla de nome name e ambs.size() < manager.numbOfAmbientes e end > 0 faz end --
            removeDisp(disp);
        }
    }

    /**Write mode
     * Adiciona o novo dispositivo tambem alterando a tupla do ambiente correspondente
     * incrementa o numero de dispositivos e o proximo id
     * */
    public static Boolean addDisp(Dispositivo disp){
        return true;
    }

    /**Take mode
     * Tira o dispositivo tambem alterando a tupla do ambiente correspondente
     * decrementa o numero de dispositivos
     * Tuplas adquiridas com o take devem ser devolvidas
     * */
    public static Dispositivo takeDisp(Dispositivo disp){
        return disp;
    }

    /**Take mode
     * Remove o dispositivo tambem alterando a tupla do ambiente correspondente
     * decrementa o numero de dispositivos
     * */
    public static Dispositivo removeDisp(Dispositivo disp){
        return disp;
    }



    //----------------------- Ambiente ---------------------------

    /**
     * Read mode
     * Le todos os ambientes do manager
     * */
    public static ArrayList<Ambiente> getAmbientes(Manager manager){

        ArrayList<Ambiente> ambs = new ArrayList<>();

        int start = manager.nextAmbID - 1;
        int end = start - manager.numbOfAmbientes;
        for(int i = start; i > end; i--){
            String name = "amb"+(i);
            //caso nao tenha a tupla de nome name e ambs.size() < manager.numbOfAmbientes e end > 0 faz end --
            ambs.add(new Ambiente(name,5,5));
        }

        return ambs;
    }

    /**Take mode
     * Remove todos os Dispositivos do ambiente
     * */
    public static ArrayList<Ambiente> removeAllAmb(Manager manager){

        ArrayList<Ambiente> removedAmbs = new ArrayList<>();
        int start = manager.nextAmbID - 1;
        int end = start - manager.numbOfAmbientes;

        for(int i = start; i > end; i--){

            String name = "amb"+(i);
            Ambiente amb = new Ambiente(name,5,5);
            removedAmbs.add(amb);
            //caso nao tenha a tupla de nome name e ambs.size() < manager.numbOfAmbientes e end > 0 faz end --
            removeAmb(amb);

        }
        return removedAmbs;
    }

    /**Write mode
     * Adiciona o novo Ambiente tambem alterando a tupla do manager correspondente
     * incrementa o numero de ambientes e o proximo id
     * */
    public static Boolean addAmb(Ambiente amb){
        return true;
    }

    /**Take mode
     * Remove o ambiente tambem alterando a tupla do manager correspondente
     * decrementa o numero de ambientes
     * */
    public static Ambiente removeAmb(Ambiente amb){
        removeAllDispsFrom(amb);
        return amb;
    }


}
