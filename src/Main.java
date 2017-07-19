import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by joseLucas on 16/07/17.
 */
public class Main {

    public static Manager manager;

    public static Boolean clearAll = false;

    public static void main(String[] args) {
        // TODO Auto-generated method stub

        //ComunicationManager.getInstance();
        Boolean success = SpaceController.findJavaSpaceService();

        if(success){
            System.out.println("Encontrou o Espaco");
            SpaceController.getAllTuples();
            if(clearAll){
                SpaceController.cleanUpJS();
                System.out.println("Limpou o Espaco");
                SpaceController.getAllTuples();
                manager = new Manager(0,0);//SpaceController.getManager(false);
                if(SpaceController.addManager(manager)){

                    presentWindow();

                }
            }
            else{
                manager = SpaceController.getManager(true);

                if(manager == null){
                    manager = new Manager(0,0);//SpaceController.getManager(false);
                    if(SpaceController.addManager(manager)){

                        presentWindow();

                    }
                }
                else{
                    presentWindow();
                }
            }

        }
        else{
            System.out.println("Nao encontrou o espaco de tuplas");
        }
    }


    private static void presentWindow(){
        AmbientesVC ambientes = new AmbientesVC();

        JFrame appFrame = new JFrame();
        appFrame.setContentPane(ambientes.getContentPanel());
        appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        appFrame.setSize(660,660);
        appFrame.setResizable(false);
        appFrame.setVisible(true);

        appFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {

            }
        });
    }


    private static void teste(){
        Boolean success = SpaceController.findJavaSpaceService();

        if(success){
            while(true){
                try{
                    Message msg = new Message();
                    msg.content = "asdasdsd";
                    Message ms =(Message) SpaceController.space.takeIfExists(msg, null, 60 * 1000);
                    Boolean c = msg.content == msg.content;

                    if(ms == null){
                        break;
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }
            try{
                Message msg = new Message();
                msg.content = "asdasdsd";
                ///msg.nextAmbID = 0;
                msg.numbOfAmbientes = 0;

                //SpaceController.space.write(msg, null, 60 * 1000);
                //Message m =(Message) SpaceController.space.takeIfExists(new Message(), null, 60 * 1000);
                Boolean c = msg.content == msg.content;
            }
            catch (Exception e){
                e.printStackTrace();
            }

            try{
                Message msg = new Message();
                msg.content = "asdasdsd";
                Message ms = (Message) SpaceController.space.readIfExists(msg, null, 60 * 1000);
                Boolean c = ms.content == msg.content;
            }
            catch (Exception e){
                e.printStackTrace();
            }

            Dispositivo d = new Dispositivo(-1,-1);
            SpaceController.addDisp(d);

            Dispositivo d2 = new Dispositivo();
            d2.ambienteID = -1;
            //d2.id = 0;

            Dispositivo d3 = SpaceController.getDisp(false,d2);

            //manager = new Manager(0,0,"manager");
            //SpaceController.addManager(manager);
            //SpaceController.getManager(true);
            //SpaceController.addAmb(new Ambiente(0,0,0));
            //SpaceController.addDisp(new Dispositivo(0,0));


            //Ambiente a = SpaceController.getAmbiente(true,new Ambiente());


            //SpaceController.getAllTuples();
        }
    }
}
