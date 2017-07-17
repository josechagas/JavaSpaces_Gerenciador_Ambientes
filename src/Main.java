import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by joseLucas on 16/07/17.
 */
public class Main {



    public static void main(String[] args) {
        // TODO Auto-generated method stub

        //ComunicationManager.getInstance();
        Manager ma = SpaceController.getManager();

        AmbientesVC ambientes = new AmbientesVC(ma);

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
}
