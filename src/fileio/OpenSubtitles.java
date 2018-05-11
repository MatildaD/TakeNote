package fileio;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OpenSubtitles extends JFrame implements ActionListener {

    public JFileChooser fc;


    public OpenSubtitles() {
        fc = new JFileChooser();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
