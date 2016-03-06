package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ActionAdapter implements ActionListener {
    private View v;

    public ActionAdapter(View v) {
        this.v = v;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        v.drawPanel.repaint();
    }
}
