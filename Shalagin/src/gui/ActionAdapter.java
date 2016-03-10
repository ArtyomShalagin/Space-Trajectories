package gui;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ActionAdapter implements ActionListener, ChangeListener {
    private View v;

    public ActionAdapter(View v) {
        this.v = v;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        v.drawPanel.repaint();
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (e.getSource().equals(v.observableSlider)) {
            v.observableSpaceSize = ((double) v.observableSlider.getValue()) * 1e6;
            v.drawPanel.repaint();
        }
    }
}
