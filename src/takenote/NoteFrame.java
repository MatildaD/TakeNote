package takenote;



import java.awt.*;
import javax.swing.*;


public class NoteFrame extends JFrame {


    private Note note;


    private JFrame window = new JFrame("TakeNote");
    private JPanel seasonPanel = new JPanel();
    private JPanel subtitlePanel = new JPanel();

    private JPanel scenePanel = new JPanel();
    private JPanel tagsPanel = new JPanel();
    private JPanel topPanel = new JPanel();


    public NoteFrame(Note note) {
        this.note = note;
        //window.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        window.setExtendedState(JFrame.MAXIMIZED_BOTH);
        window.setVisible(true);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        testyTest();

    }






    JSplitPane subtitlesAndNotes = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, subtitlePanel, scenePanel);
    JSplitPane subtitleNotesAndSeasons = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, seasonPanel, subtitlesAndNotes);
    JSplitPane topAndBottom = new JSplitPane(JSplitPane.VERTICAL_SPLIT, topPanel, subtitleNotesAndSeasons);






    public void testyTest() {
        seasonPanel.setBackground(Color.cyan);
        subtitlePanel.setBackground(Color.red);
        scenePanel.setBackground(Color.yellow);
        topPanel.setBackground(Color.green);

        subtitlesAndNotes.setPreferredSize(new Dimension(100, 550));
        topPanel.setMinimumSize(new Dimension(0, 100));
        seasonPanel.setPreferredSize(new Dimension(200, 100));
        subtitlePanel.setPreferredSize(new Dimension(400, 100));


        window.add(topAndBottom);

    }









}
