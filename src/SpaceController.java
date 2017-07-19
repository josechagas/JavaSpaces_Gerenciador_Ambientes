/**
 * Created by joseLucas on 16/07/17.
 */

import net.jini.core.lease.Lease;
import net.jini.space.JavaSpace;

import java.util.ArrayList;
import java.util.function.BiFunction;

/**
 * This class will execute the methods necessary to comunicate with JavaSpace
 * */

public class SpaceController {
    public static JavaSpace space = null;

    public static Boolean findJavaSpaceService(){
        try{
            Lookup finder = new Lookup(JavaSpace.class);
            space = (JavaSpace) finder.getService();

            if (space == null) {
                return false;
            }
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }


    //----------------------- Manager ---------------------------
    /**
     * Write
     * */
    public static Boolean addManager(Manager manager){
        Manager m = null;
        try {
            Lease l = space.write(manager,null,Lease.FOREVER);
            l.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Read
     * */
    public static Manager getManager(Boolean readOnly) {
        Manager m = null;
        try {
            Manager ms = new Manager();
            m = (Manager) (readOnly ? space.readIfExists(ms,null,60 * 1000) :
                    space.takeIfExists(ms,null,60 * 1000));

            if(m != null){
                return new Manager(m.numbOfAmbientes,m.nextAmbID);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }



        return null;
    }

    /**
     * Read
     * */
    public static void getManager(Boolean readOnly,BiFunction<Boolean,Manager, Integer> completion){
        InBackground.execute(integer -> {
            Manager m = null;
            try{
                m = (Manager) (readOnly ? space.readIfExists(new Manager(),null,0) :
                        space.takeIfExists(new Manager(),null,0));
                completion.apply(true,m);
            }
            catch (Exception e){
                completion.apply(false,null);
            }

            return false;
        });

    }
    /**
     * Take and Write
     * */
    public static Boolean updateManager(Manager manager){
        Manager m = null;
        m = removeManager();
        Manager newManager = new Manager();
        newManager.numbOfAmbientes = manager.numbOfAmbientes;
        newManager.nextAmbID = manager.nextAmbID;

        return addManager(newManager);
    }

    /**
     * Take
     * */
    public static Manager removeManager(){
        Manager m = null;
        try {
            m = (Manager) space.takeIfExists(new Manager(),null,0);

            if(m != null){
                return new Manager(m.numbOfAmbientes,m.nextAmbID);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void getAllTuples(){
        boolean looking = true;
        System.out.println("-----Tuplas -----------");

        Manager m = getManager(true);
        if(m != null){
            System.out.println("--manager "+m.toString());
            ArrayList<Ambiente> ambs = getAmbientes(true,m);
            if(ambs != null){
                for (Ambiente a : ambs) {
                    System.out.println("----ambiente "+a.getName());
                    ArrayList<Dispositivo> disps = getDispositivosOfAmbiente(true,a);

                    if(disps != null){
                        for (Dispositivo disp :
                                disps) {
                            System.out.println("-------dispositivo "+disp.getName());
                        }
                    }
                }
            }
        }
        else{
            looking = m!= null;
        }

        System.out.println("-----Tuplas Fim -----------");


    }

    public static void cleanUpJS(){
        boolean remove = true;
        while (remove){
            try{
                Manager m = removeManager();
                if(m != null){
                    removeAllAmb(m);
                    remove = true;
                }
                else{
                    remove = false;
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }

        while(SpaceController.removeAmb(new Ambiente()) != null);
        while(SpaceController.removeDisp(new Dispositivo()) != null);
        while(SpaceController.removeManager() != null);

    }


    //----------------------- Dispositivo ---------------------------

    /**Write mode
     * Adiciona o novo dispositivo tambem alterando a tupla do ambiente correspondente
     * incrementa o numero de dispositivos e o proximo id
     * */
    public static Boolean addDisp(Dispositivo disp){
        Dispositivo d = null;
        try {
            Lease l = space.write(disp,null,Lease.FOREVER);
            l.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**Read
     * Tira o dispositivo tambem alterando a tupla do ambiente correspondente
     * decrementa o numero de dispositivos
     * Tuplas adquiridas com o take devem ser devolvidas
     * */
    public static Dispositivo getDisp(Boolean readOnly,Dispositivo disp){
        Dispositivo d = null;
        try {
            d = (Dispositivo) (readOnly ? space.readIfExists(disp,null,0) : space.takeIfExists(disp,null,0));

            if(d != null){
                return readOnly ? d : new Dispositivo(d.id,d.ambienteID);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /*Move to another ambiente
    public static Boolean moveDisp(Dispositivo disp,Ambiente a){
        removeDisp(disp);
        a.numbOfDisps --;
        if(updateAmbiente(a)){
            disp.ambienteID = a.id;
            return addDisp(disp);
        }
        return false;
    }
*/
    /**Take mode
     * Remove o dispositivo tambem alterando a tupla do ambiente correspondente
     * decrementa o numero de dispositivos
     * */
    public static Dispositivo removeDisp(Dispositivo disp){
        Dispositivo d = null;
        try {
            d = (Dispositivo) space.takeIfExists(disp,null,0);

            if(d != null){
                return new Dispositivo(d.id,d.ambienteID);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }


    /**
     * Read all dispositivos do ambiente correspondente
     * Read mode
     * */
    public static ArrayList<Dispositivo> getDispositivosOfAmbiente(Boolean readOnly,Ambiente ambiente){
        ArrayList<Dispositivo> disps = new ArrayList<>();
        int start = ambiente.nextDispID - 1;
        int end = start - ambiente.numbOfDisps;
        for(int i = start; i > end; i--){
            Dispositivo disp = new Dispositivo();
            disp.id = i;
            disp.ambienteID = ambiente.id;
            disp = getDisp(readOnly,disp);

            if(disp == null && end > -1){//ainda pode existir algum dispositivo no espaco
                //existira dispositivos na tupla caso exclua dispositivos fora da ordem numerica
                end --;
            }
            else if(disp != null){
                disps.add(disp);
            }
        }
        return disps;
    }

    /**Take mode
     * Remove todos os Dispositivos do ambiente
     * */
    public static ArrayList<Dispositivo> removeAllDispsFrom(Ambiente ambiente){
        int offset = 6;
        ArrayList<Dispositivo> removedDisps = new ArrayList<>();
        int start = ambiente.nextDispID - 1;
        int end = start - ambiente.numbOfDisps;

        for(int i = start + offset; i > end; i--){
            Dispositivo disp = new Dispositivo();
            disp.id = i;
            disp.ambienteID = ambiente.id;
            disp = removeDisp(disp);

            if(disp == null && end > -1){//ainda pode existir algum dispositivo no espaco
                //existira dispositivos na tupla caso exclua dispositivos fora da ordem numerica
                end --;
            }
            else if(disp != null){
                removedDisps.add(disp);
            }
        }
        return removedDisps;
    }




    //----------------------- Ambiente ---------------------------
    /**Write mode
     * Adiciona o novo Ambiente tambem alterando a tupla do manager correspondente
     * incrementa o numero de ambientes e o proximo id
     * */
    public static Boolean addAmb(Ambiente amb){
        Ambiente a = null;
        try {
            Lease l = space.write(amb,null,Lease.FOREVER);
            l.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static Ambiente getAmbiente(Boolean readOnly,Ambiente amb){
        Ambiente a = null;
        try {
            a = (Ambiente) (readOnly ? space.readIfExists(amb,null,0) : space.takeIfExists(amb,null,0));

            if(a != null){
                return readOnly ? a : new Ambiente(a.id,a.numbOfDisps,a.nextDispID);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Take and Write
     * */
    public static Boolean updateAmbiente(Ambiente amb){
        Ambiente a = new Ambiente();
        a.id = amb.id;
        //a = removeAmb(a);//do not using this because its removing all its dispositivies and I do not want it on updates
        try{
            a = (Ambiente) space.takeIfExists(a,null,0);
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }

        Ambiente newAmb = new Ambiente();
        newAmb.id = amb.id;
        newAmb.numbOfDisps = amb.numbOfDisps;
        newAmb.nextDispID = amb.nextDispID;

        return addAmb(newAmb);
    }
    /**Take mode
     * Remove o ambiente tambem alterando a tupla do manager correspondente
     * decrementa o numero de ambientes
     * */
    public static Ambiente removeAmb(Ambiente amb){
        Ambiente a = new Ambiente();
        a.id = amb.id;
        try {
            a = (Ambiente) space.takeIfExists(a,null,0);

            if(a != null){
                removeAllDispsFrom(a);
                return new Ambiente(a.id,a.numbOfDisps,a.nextDispID);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }


    /**
     * Read/Take mode
     * Le todos os ambientes do manager
     * */
    public static ArrayList<Ambiente> getAmbientes(Boolean readOnly,Manager manager){

        ArrayList<Ambiente> ambs = new ArrayList<>();

        int start = manager.nextAmbID - 1;
        int end = start - manager.numbOfAmbientes;
        for(int i = start; i > end; i--){
            Ambiente amb = new Ambiente();
            amb.id = i;
            //caso nao tenha a tupla de nome name e ambs.size() < manager.numbOfAmbientes e end > 0 faz end --
            amb = getAmbiente(readOnly,amb);

            if(amb == null && end > -1){//ainda pode existir algum ambiente no espaco
                //existira ambiente na tupla caso exclua ambientes fora da ordem numerica
                end --;
            }
            else if(amb != null){
                ambs.add(amb);
            }
        }
        return ambs;
    }

    /**Take mode
     * Remove todos os Dispositivos do ambiente
     * */
    public static ArrayList<Ambiente> removeAllAmb(Manager manager){
        int offset  = 6;
        ArrayList<Ambiente> removedAmbs = new ArrayList<>();
        int start = manager.nextAmbID - 1;
        int end = start - manager.numbOfAmbientes;

        for(int i = start + offset; i > end; i--){
            Ambiente amb = new Ambiente();
            amb.id = i;
            //caso nao tenha a tupla de nome name e ambs.size() < manager.numbOfAmbientes e end > 0 faz end --
            amb = removeAmb(amb);

            if(amb == null && end > -1){//ainda pode existir algum ambiente no espaco
                //existira ambiente na tupla caso exclua ambientes fora da ordem numerica
                end --;
            }
            else if(amb != null){
                removedAmbs.add(amb);
            }
        }
        return removedAmbs;
    }



}
