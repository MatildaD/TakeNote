package takenote;


import components.*;
import external.WrapLayout;
import fileio.OpenSubtitles;
import javafx.scene.Scene;
import net.miginfocom.swing.MigLayout;
import listeners.ButtonListener;


import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.BoxLayout;


public class NoteFrame extends JFrame {


    private Note note;


    private JFrame window = new JFrame("TakeNote");



    private JPanel seasonPanel = new JPanel(new MigLayout());
    private JPanel subtitlePanel = new JPanel(new MigLayout());

    private JPanel scenePanel = new JPanel(new MigLayout());
    private JPanel tagsPanel = new JPanel(new MigLayout());
    private JPanel topPanel = new JPanel(new MigLayout());

    private JButton addSeasonButton = new JButton("Add Season");
    private JButton removeSeasonButton = new JButton("Remove Season");
    private JButton addEpisodeButton = new JButton("Add Episode");
    private JButton removeEpisodeButton = new JButton("Remove Episode");
    private JButton addSceneNoteButton = new JButton("Add Note");
    private JButton removeSceneNoteButton = new JButton("Remove Note");
    private JButton addTagButton = new JButton("Add Tag");
    private JButton removeTagButton = new JButton("Remove Tag");
    private JButton addSubtitlesButton = new JButton("Add Subtitles");
    private JButton removeSubtitlesButton = new JButton("Remove Subtitles");



    //Scrolly panels
    private JScrollPane subtitleScroll;
    private JScrollPane seasonScroll;
    private JScrollPane sceneNoteScroll;
    private JScrollPane tagScroll;


    //Subtitles
    private JFileChooser fc = new JFileChooser();


    //SceneNotes
    public List<JTextArea> activeNotesList;
    public List<SubtitleBit> activeSubtitleBits;


    final String EPISODE_SPACE = "      ";


    public NoteFrame(Note note) {
        this.note = note;
        //window.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        window.setExtendedState(JFrame.MAXIMIZED_BOTH);
        window.setVisible(true);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setupFrame();

    }

    public Note getNote() {
        return note;
    }

    private JSplitPane subtitlesAndNotes;
    private JSplitPane subtitleNotesAndSeasons;
    private JSplitPane topAndBottom;
    private JSplitPane tagsAndNotes;






    public void setupFrame() {
        activeNotesList = new ArrayList<>();
        activeSubtitleBits = new ArrayList<>();

        seasonPanel.setBackground(Color.white);
        subtitlePanel.setBackground(Color.white);
        scenePanel.setBackground(Color.white);
        topPanel.setBackground(Color.white);
        tagsPanel.setBackground(Color.white);


        subtitleScroll = new JScrollPane(subtitlePanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        seasonScroll = new JScrollPane(seasonPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        sceneNoteScroll = new JScrollPane(scenePanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        tagScroll = new JScrollPane(tagsPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        tagsAndNotes = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sceneNoteScroll, tagsPanel);
        subtitlesAndNotes = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, subtitleScroll, tagsAndNotes);
        subtitleNotesAndSeasons = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, seasonScroll, subtitlesAndNotes);
        topAndBottom = new JSplitPane(JSplitPane.VERTICAL_SPLIT, topPanel, subtitleNotesAndSeasons);

        subtitlesAndNotes.setPreferredSize(new Dimension(100, 550));
        topPanel.setMinimumSize(new Dimension(0, 100));
        seasonScroll.setPreferredSize(new Dimension(150, 100));
        subtitleScroll.setPreferredSize(new Dimension(300, 100));
        subtitleScroll.getVerticalScrollBar().setUnitIncrement(16);
        sceneNoteScroll.setPreferredSize(new Dimension(1200, 100));
        sceneNoteScroll.getVerticalScrollBar().setUnitIncrement(16);
        topAndBottom.setPreferredSize(new Dimension(600, 800));
        tagsPanel.setPreferredSize(new Dimension(100, 100));




        topPanel.add(addSeasonButton);
        topPanel.add(removeSeasonButton);
        topPanel.add(addEpisodeButton);
        topPanel.add(removeEpisodeButton);
        topPanel.add(addSceneNoteButton);
        topPanel.add(removeSceneNoteButton);
        topPanel.add(addTagButton);
        topPanel.add(removeTagButton);
        topPanel.add(addSubtitlesButton);
        topPanel.add(removeSubtitlesButton);


        addSeasonButton.addActionListener(new ButtonListener(this));
        removeSeasonButton.addActionListener(new ButtonListener(this));
        addEpisodeButton.addActionListener(new ButtonListener(this));
        removeEpisodeButton.addActionListener(new ButtonListener(this));
        addSceneNoteButton.addActionListener(new ButtonListener(this));
        removeSceneNoteButton.addActionListener(new ButtonListener(this));
        addTagButton.addActionListener(new ButtonListener(this));
        removeTagButton.addActionListener(new ButtonListener(this));
        addSubtitlesButton.addActionListener(new ButtonListener(this));
        removeSubtitlesButton.addActionListener(new ButtonListener(this));





        window.add(topAndBottom);


    }


    public void updateSeasons() {
        seasonPanel.removeAll();
        for (Season s: note.getSeasons()) {
            JLabel se = new JLabel(s.getName());
            se.putClientProperty("getSe", s);
            se.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    JLabel label = (JLabel) e.getSource();
                    Season season = (Season) label.getClientProperty("getSe");
                    if (e.getClickCount() == 1) {
                        season.flipExpands();

                        updateSubtitles();
                        updateSeasons();

                    } else if (e.getClickCount() == 2) {
                        renameSeason(season);
                        updateSeasons();
                    }
                }

                @Override
                public void mousePressed(MouseEvent e) {}

                @Override
                public void mouseReleased(MouseEvent e) {}

                @Override
                public void mouseEntered(MouseEvent e) {}

                @Override
                public void mouseExited(MouseEvent e) {}

            });
            seasonPanel.add(se, "wrap");

            if (s.expands()) {
                for (Episode e: s.getEpisodeList()) {
                    JLabel ep = new JLabel(EPISODE_SPACE + e.getName());
                    ep.putClientProperty("getEp", e);
                    ep.addMouseListener(new MouseListener() {


                        public void mouseClicked(MouseEvent event) {
                            JLabel label = (JLabel) event.getSource();
                            Episode episode = (Episode) label.getClientProperty("getEp");
                            saveNotes();
                            if (event.getClickCount() == 1) {
                                if (note.getSelectedEpisode() != null) {
                                    Point p1 = subtitleScroll.getViewport().getViewPosition();
                                    note.getSelectedEpisode().setSubtitleScrollPos(p1);
                                    Point p2 = sceneNoteScroll.getViewport().getViewPosition();
                                    note.getSelectedEpisode().setSceneNoteScrollPos(p2);
                                }

                                note.setSelectedEpisode(episode);
                                updateAll();


                            } else if (event.getClickCount() == 2) {
                                renameEpisode(episode);
                            }
                        }

                        public void mouseReleased(MouseEvent event) {}
                        public void mousePressed(MouseEvent event) {}
                        public void mouseEntered(MouseEvent event) {}
                        public void mouseExited(MouseEvent event) {}
                    });

                    seasonPanel.add(ep, "pushx, wrap");

                    if(e.equals(note.getSelectedEpisode())) {
                        ep.setBackground(Color.LIGHT_GRAY);
                        ep.setOpaque(true);
                    }

                }

            }
        }


        window.revalidate();
        window.repaint();
    }



    public void renameEpisode(Episode e) {
        JTextField episodeName = new JTextField(e.getName());
        JComboBox<Season> season = new JComboBox<>();
        note.getSeasons().forEach(season::addItem);
        season.setEditable(false);
        season.setSelectedItem(e.getSeason());

        JPanel inputs = createInputs(episodeName, season, true);
        String[] buttons = { "Save", "Delete Episode", "Cancel" };

        int ans = JOptionPane.showOptionDialog(NoteFrame.this, inputs, "Confirmation", JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE, null, buttons, buttons[0]);

        if (ans == 1) {
            note.removeEpisode(e);
            updateAll();
        }
    }



    public void renameSeason(Season s) {
        JTextField seasonName = new JTextField(s.getName());

        JPanel inputs = createInputs(seasonName, null, false);
        JOptionPane.showMessageDialog(window, inputs, "Edit Season name", JOptionPane.PLAIN_MESSAGE);

        while (shouldAskAgain(seasonName)) {
            inputs = popup(changeInputs(inputs, seasonName), "Edit Season name", JOptionPane.PLAIN_MESSAGE);
        }
        s.setName((seasonName.getText()));
        updateSeasons();
        updateSubtitles();
    }



    public void updateAll() {
        updateSubtitles();
        updateSeasons();
        updateSceneNotes();

        window.revalidate();
        window.repaint();
    }


    public void saveNotes() {
        for (JTextArea a: activeNotesList) {
            SceneNote s = (SceneNote) a.getClientProperty("getScene");
            s.setNote(a.getText());
        }
        activeNotesList.clear();
    }



    public void updateSubtitles() {
        subtitlePanel.removeAll();
        try {
            subtitlePanel.add(new JLabel("<html><h3> Subtitles for " + note.getSelectedEpisode().toString() + "</h3></html>"), "wrap");
        } catch (java.lang.NullPointerException e) {}
        for (SubtitleBit bit: note.getSelectedEpisode().getSubtitles()) {
            JLabel label = new JLabel("<html>" + bit.toString() + "</html>");
            label.putClientProperty("getBit", bit);
            label.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    JLabel l = (JLabel) e.getSource();
                    if (e.getClickCount() == 2) {

                        SubtitleBit bit = (SubtitleBit) l.getClientProperty("getBit");
                        saveNotes();
                        newSceneNote(bit);
                        updateSceneNotes();
                        updateSubtitles(bit);

                        window.revalidate();
                        window.repaint();

                    }
                }

                @Override
                public void mousePressed(MouseEvent e) {

                }

                @Override
                public void mouseReleased(MouseEvent e) {

                }

                @Override
                public void mouseEntered(MouseEvent e) {

                }

                @Override
                public void mouseExited(MouseEvent e) {

                }
            });
            subtitlePanel.add(label, "wrap, gap 0px 50px");
        }

        subtitleScroll.getViewport().setViewPosition(note.getSelectedEpisode().getSubtitleScrollPos());
    }


    public void updateSubtitles(SubtitleBit b) {
        subtitlePanel.removeAll();
        JLabel jumpTo = new JLabel();
        try {
            subtitlePanel.add(new JLabel("<html><h3> Subtitles for " + note.getSelectedEpisode().toString() + "</h3></html>"), "wrap");
        } catch (java.lang.NullPointerException e) {}
        for (SubtitleBit bit: note.getSelectedEpisode().getSubtitles()) {
            JLabel label = new JLabel("<html>" + bit.toString() + "</html>");
            if (bit.equals(b)) {
                label.setOpaque(true);
                label.setBackground(Color.lightGray);
                jumpTo = label;

            }
            label.putClientProperty("getBit", bit);
            label.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    JLabel l = (JLabel) e.getSource();
                    if (e.getClickCount() == 2) {
                        SubtitleBit bit = (SubtitleBit) l.getClientProperty("getBit");
                        saveNotes();
                        bit.setScrollPos(subtitleScroll.getViewport().getViewPosition());
                        newSceneNote(bit);
                        updateSceneNotes();
                        updateSubtitles(bit);
                        window.revalidate();
                        window.repaint();
                    }
                }

                @Override
                public void mousePressed(MouseEvent e) {

                }

                @Override
                public void mouseReleased(MouseEvent e) {

                }

                @Override
                public void mouseEntered(MouseEvent e) {

                }

                @Override
                public void mouseExited(MouseEvent e) {

                }
            });
            subtitlePanel.add(label, "wrap, gap 0px 50px");
        }
        subtitleScroll.getViewport().setViewPosition(b.getScrollPos());
    }


    public void newSceneNote(SubtitleBit bit) {
            SceneNote sn = new SceneNote("", bit);
            note.getSelectedEpisode().addNote(sn);
            updateSceneNotes();
            window.revalidate();
            window.repaint();
    }




    public void updateSceneNotes(){
        scenePanel.removeAll();
        try {
            scenePanel.add(new JLabel("<html><h1> Notes and Tags for " + note.getSelectedEpisode().toString() + "</h1></html>"), "wrap");
        } catch (java.lang.NullPointerException e) {}

        List<SceneNote> notesList = note.getSelectedEpisode().getNotes();
        SceneNote lastAdded = new SceneNote("Dummy");
        if (!notesList.isEmpty()) {
            lastAdded = notesList.get(notesList.size() - 1);
            System.out.println(lastAdded.getNote());
        }


        //Sort list of notes based on starting time, aka chronological order compared to the subtitles
        Collections.sort(notesList, new Comparator<SceneNote>() {
            @Override
            public int compare(final SceneNote object1, final SceneNote object2) {
                return object1.getStartTime().compareTo(object2.getStartTime());
            }
        });
        note.getSelectedEpisode().setNotes(notesList);


        int count = 0;

        for (SceneNote s: notesList) {
            count++;


            JPanel panel = new JPanel(new MigLayout());
            panel.setBackground(Color.green);
            JPanel tagPanel = new JPanel(new WrapLayout());
            tagPanel.setBackground(Color.blue);
            //Adding order matters! Make sure tagPanel is added first, see tagInput action listener!
            panel.add(tagPanel, "cell 1 1, grow, pushx, pushy, width 100:650:650");


            JTextField tagInput = new JTextField();
            tagInput.putClientProperty("getScene", s);

            tagInput.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JTextField t = (JTextField) e.getSource();
                    String tagText = t.getText();
                    t.setText("");
                    JPanel tagP = (JPanel) t.getParent().getComponent(0);
                    SceneNote scene = (SceneNote) t.getClientProperty("getScene");

                    Tag tag;
                    if (note.tagExists(tagText)) {
                        tag = note.getTag(tagText);
                    } else {
                        tag = new Tag(tagText);
                    }
                    note.addTag(tag);
                    scene.addTag(tag);
                    tagP.add(new JLabel(tag.getTag()));
                    tagP.revalidate();
                    tagP.repaint();




                }
            });


            panel.add(new JLabel("Start time: " + s.getStartTime()), "cell 0 0, grow");
            panel.add(new JLabel("Add Tag(s):"), "align right, cell 0 0");
            panel.add(tagInput, "cell 1 0, width 150");





            JTextArea textArea = new JTextArea(s.getNote());
            textArea.setPreferredSize(new Dimension(500, 50));
            textArea.setMaximumSize(new Dimension(500, 1000));
            textArea.setWrapStyleWord(true);
            textArea.setLineWrap(true);
            textArea.putClientProperty("getScene", s);
            textArea.addMouseListener(new MouseListener() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    JTextArea ta = (JTextArea) e.getSource();
                    SceneNote sn = (SceneNote) ta.getClientProperty("getScene");
                    note.getSelectedEpisode().setActiveSceneNote(sn);
                    updateSubtitles(sn.getSub());
                    //updateSceneNotes();
                    window.revalidate();
                    //window.repaint();
                }

                @Override
                public void mousePressed(MouseEvent e) {

                }

                @Override
                public void mouseReleased(MouseEvent e) {

                }

                @Override
                public void mouseEntered(MouseEvent e) {

                }

                @Override
                public void mouseExited(MouseEvent e) {

                }
            });
            activeNotesList.add(textArea);
            panel.add(textArea, "cell 0 1, grow, pushy");


            JLabel sn = new JLabel(s.getNote());
            for (Tag t: s.getTagList()) {
                JLabel l = new JLabel(t.toString());
                tagPanel.add(l, "gapright 10");
            }

            scenePanel.add(panel, "grow, pushx, wrap");

        }



    }



    // Asks the user for input to select an item from an iterable list
    // Requires the object to be cast to the desired class when returned
    private Object selectItem(String message, Iterable<Object> iterableList) {
        JComboBox<Object> thing = new JComboBox<>();
        iterableList.forEach(thing::addItem);
        thing.setEditable(false);
        JOptionPane.showMessageDialog(window, thing, message, JOptionPane.PLAIN_MESSAGE);
        return thing.getSelectedItem();
    }

    // Warning the user that removing something will result in a chain reaction of removal
    private int removeWarning(String message) {
        return JOptionPane.showConfirmDialog(window, message, "Warning", JOptionPane.YES_NO_OPTION);
    }

    // Creates (and adds to budget) a main category based on user input
    public void addSeason() {
        JTextField seasonName = new JTextField();

        JPanel inputs = createInputs(seasonName, null, false);
        JOptionPane.showMessageDialog(window, inputs, "Add Season", JOptionPane.PLAIN_MESSAGE);

        while (shouldAskAgain(seasonName)) {
            inputs = popup(changeInputs(inputs, seasonName), "Add Season", JOptionPane.PLAIN_MESSAGE);
        }

        Season newSeason = new Season(seasonName.getText());
        note.addSeason(newSeason);
        updateSeasons();

    }

    // Removes a main category, and all of its sub categories
    public void removeSeason() {
        if (note.getSeasons().isEmpty()) { return; }
        Collection<Object> selectFrom = note.getSeasons().stream().collect(Collectors.toList());
        Season seasonToRemove = (Season) selectItem("Remove Season", selectFrom);

        int answer = removeWarning(
                "Warning - Removing a Season will remove all its Episodes and all their SceneNotes. Do you wish to proceed?");
        if (answer == JOptionPane.OK_OPTION) {
            note.removeSeason(seasonToRemove);
        }
        updateSeasons();
    }





    // Informs the user that there can't exist a sub category without a main for it to belong to
    private void noEpispdeWithoutSeason() {
        JOptionPane.showMessageDialog(window, "You need to create a Season first", "No Episode Without Season",
                JOptionPane.INFORMATION_MESSAGE);

    }



    // Creates (and adds to budget) a sub category based on user input
    public void addEpisode() {
        if (note.getSeasons().isEmpty()) {
            noEpispdeWithoutSeason();
            return;
        }

        JComboBox<Season> category = new JComboBox<>();
        note.getSeasons().forEach(category::addItem);
        category.setEditable(false);
        JTextField episodeName = new JTextField();

        JPanel inputs = createInputs(episodeName, category, true);

        JOptionPane.showMessageDialog(window, inputs, "Add Episode", JOptionPane.PLAIN_MESSAGE);

        // Keeps prompting the user for inputs until valid input is given
        while (shouldAskAgain(episodeName)) {
            inputs = popup(changeInputs(inputs, episodeName), "Add Episode", JOptionPane.PLAIN_MESSAGE);
        }

        Season season = (Season) category.getSelectedItem();
        Episode newEp = new Episode(episodeName.getText(), season);
        note.addEpisode(newEp, season);
        updateSeasons();
    }


    // Removes a sub category of a users choise, and also removes all transactions
    // that are associated with that sub category
    public void removeEpisode() {
        if (note.getSeasons().isEmpty()) { return; }
        Collection<Object> selectFrom = note.getSeasons().stream().collect(Collectors.toList());
        Season seasonToRemoveFrom = (Season) selectItem("Choose Season to remove Episode from", selectFrom);


        if (seasonToRemoveFrom.getEpisodeList().isEmpty()) { return; }
        Collection<Object> selectEpFrom = seasonToRemoveFrom.getEpisodeList().stream().collect(Collectors.toList());
        Episode episodeToRemove = (Episode) selectItem("Remove Episode", selectEpFrom);

        int answer = removeWarning("Warning - Removing an Episode" +
                " will remove all its SceneNotes. Do you wish to proceed?");
        if (answer == JOptionPane.YES_OPTION) {
            note.removeEpisode(episodeToRemove);
        }
        updateSeasons();
    }
    
    
    
    // Reconstructs and returns JPanel inputs if what the user submitted was not valid
    private JPanel changeInputs(JPanel inputs, JTextField name) {

        if (name.getText().isEmpty() || name.getText().equals("You must enter a name")){
            name.setText("You must enter a name");
            name.setBackground(Color.RED);
        } else {
            name.setBackground(Color.WHITE);
        }
        return inputs;
    }


    // Shows the user a message dialog with inputs as the panel, returns
    // the input after the user has finished entering information
    private JPanel popup(JPanel inputs, String message, int constantValue) {
        JOptionPane.showMessageDialog(window, inputs, message, constantValue);
        return inputs;
    }



    private boolean shouldAskAgain(JTextField name) {
        return name.getText().isEmpty() || name.getText().equals("You must enter a name");
    }





    // Creates and adds all components to the inputs panel used for creating and editing main and sub categories
    private JPanel createInputs(JTextField categoryName, JComboBox<Season> seasons, boolean isEp) {
        JPanel inputs = new JPanel(new MigLayout());
        inputs.add(new JLabel("Name"), "wrap");
        inputs.add(categoryName, "grow, pushx, wrap");

        if (isEp) {
            inputs.add(new JLabel("Season"), "grow, wrap");
            inputs.add(seasons, "grow");
        }

        return inputs;
    }



    public void openSubtitles() {
        if (note.getSelectedEpisode() != null) {

            int returnVal = fc.showOpenDialog(NoteFrame.this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                note.getSelectedEpisode().importSubtitles(file.getAbsolutePath());
                updateSubtitles();
                window.revalidate();
                window.repaint();
            }
        }
    }


    public void removeSubtitles() {
        note.getSelectedEpisode().clearSubtitles();
        updateSubtitles();
        window.revalidate();
        window.repaint();

    }




    public JButton getAddSeasonButton() {return addSeasonButton;}
    public JButton getRemoveSeasonButton() {return removeSeasonButton;}
    public JButton getAddEpisodeButton() {return addEpisodeButton;}
    public JButton getRemoveEpisodeButton() {return removeEpisodeButton;}
    public JButton getAddSceneNoteButton() {return addSceneNoteButton;}
    public JButton getRemoveSceneNoteButton() {return removeSceneNoteButton;};
    public JButton getAddTagButton() {return addTagButton;}
    public JButton getRemoveTagButton() {return removeTagButton;};
    public JButton getAddSubtitlesButton() {return addSubtitlesButton;}
    public JButton getRemoveSubtitlesButton() {return removeSubtitlesButton;};


}
