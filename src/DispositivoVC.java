import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Function;

/**
 * Created by joseLucas on 16/07/17.
 */
public class DispositivoVC {
    private JLabel titleLabel;
    private JButton moveButton;
    private JButton removeButton;
    private JPanel contentPane;

    public Dispositivo dispositivo;

    private Function<Dispositivo,Integer> removeItemBlock;
    private Function<Dispositivo,Integer> moveItemBlock;

    DispositivoVC(){

    }

    DispositivoVC(Dispositivo disp){
        dispositivo =disp;
        titleLabel.setText(disp.getName());
        setUpMoveButton();

        setUpRemoveButton();
    }

    public DispositivoVC update(Dispositivo disp){
        dispositivo = disp;
        titleLabel.setText(disp.getName());
        setUpMoveButton();

        setUpRemoveButton();
        return this;
    }

    public void setMoveItemBlock(Function<Dispositivo, Integer> moveItemBlock) {
        this.moveItemBlock = moveItemBlock;
    }

    public void setRemoveItemBlock(Function<Dispositivo, Integer> removeItemBlock) {
        this.removeItemBlock = removeItemBlock;
    }

    public JPanel getContentPane() {
        return contentPane;
    }

    private void setUpMoveButton(){
        if(moveButton.getActionListeners().length == 0) {
            moveButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    System.out.println("move "+titleLabel.getText());
                    moveItemBlock.apply(dispositivo);

                }
            });
        }
    }

    private void setUpRemoveButton(){
        if(removeButton.getActionListeners().length == 0) {
            removeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    removeItemBlock.apply(dispositivo);

                }
            });
        }
    }

}
