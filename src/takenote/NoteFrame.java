package takenote;


import com.sun.org.apache.xpath.internal.SourceTree;
import components.*;
import enums.Selected;
import external.Java2sAutoTextField;
import external.TextPrompt;
import external.WrapLayout;
import fileio.SaveNote;
import net.miginfocom.swing.MigLayout;
import listeners.ButtonListener;


import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;




//TODO: Add functionality to set custom Start Time for SceneNote


//TODO: Add functionality to add choose/add/remove Color for tags
//TODO: Add functionality to Select Season and add notes to Season
//TODO: Add functionality to Search using Tags


public class NoteFrame extends JFrame {


    private Note note;


    private JFrame window = new JFrame("TakeNote");



    private JPanel seasonPanel = new JPanel(new MigLayout());
    private JPanel subtitlePanel = new JPanel(new MigLayout());

    private JPanel scenePanel = new JPanel(new MigLayout());
    private JPanel tagsPanel = new JPanel(new MigLayout());
    private JPanel topPanel = new JPanel(new MigLayout());

    private JButton addSeasonButton = new JButton("Add Season");
    private JButton removeSeasonButton = new JButton("Delete Season");
    private JButton addEpisodeButton = new JButton("Add Episode");
    private JButton removeEpisodeButton = new JButton("Delete Episode");
    private JButton addSceneNoteButton = new JButton("Add Note");
    private JButton addSubtitlesButton = new JButton("Add Subtitles");
    private JButton removeSubtitlesButton = new JButton("Delete Subtitles");

    //Scrolly panels
    private JScrollPane subtitleScroll;
    private JScrollPane seasonScroll;
    private JScrollPane sceneNoteScroll;
    private JScrollPane tagScroll;


    //Subtitles
    private JFileChooser fc = new JFileChooser();


    //SceneNotes
    private List<JTextArea> activeNotesList;
    private List<SubtitleBit> activeSubtitleBits;

    //Tags
    private List<Tag> foundTags;




    NoteFrame(Note note) {
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


    public JPanel getTagsPanel() {
        return tagsPanel;
    }

    private void setupFrame() {
        activeNotesList = new ArrayList<>();
        activeSubtitleBits = new ArrayList<>();
        foundTags = null;

        seasonPanel.setBackground(Color.white);
        subtitlePanel.setBackground(Color.white);
        scenePanel.setBackground(Color.white);
        topPanel.setBackground(Color.white);
        tagsPanel.setBackground(Color.white);


        subtitleScroll = new JScrollPane(subtitlePanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        seasonScroll = new JScrollPane(seasonPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        sceneNoteScroll = new JScrollPane(scenePanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        tagScroll = new JScrollPane(tagsPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        tagsAndNotes = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sceneNoteScroll, tagScroll);
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
        tagScroll.setPreferredSize(new Dimension(150, 100));
        topAndBottom.setPreferredSize(new Dimension(600, 800));
        //tagsPanel.setPreferredSize(new Dimension(100, 100));




        topPanel.add(addSeasonButton);
        topPanel.add(removeSeasonButton);
        topPanel.add(addEpisodeButton);
        topPanel.add(removeEpisodeButton);
        topPanel.add(addSceneNoteButton);
        topPanel.add(addSubtitlesButton);
        topPanel.add(removeSubtitlesButton);


        addSeasonButton.addActionListener(new ButtonListener(this));
        removeSeasonButton.addActionListener(new ButtonListener(this));
        addEpisodeButton.addActionListener(new ButtonListener(this));
        removeEpisodeButton.addActionListener(new ButtonListener(this));
        addSceneNoteButton.addActionListener(new ButtonListener(this));
        addSubtitlesButton.addActionListener(new ButtonListener(this));
        removeSubtitlesButton.addActionListener(new ButtonListener(this));
        setupTagSearch();
        createMenus();

        window.add(topAndBottom);
    }

    private void createMenus() {
        final JMenu file = new JMenu("File");
        file.setMnemonic('F');

        JMenuItem save = new JMenuItem("Save", 'S');
        JMenuItem load = new JMenuItem("Load", 'L');
        file.add(save);
        file.add(load);
        final JMenuBar menuBar = new JMenuBar();
        menuBar.add(file);
        window.setJMenuBar(menuBar);

        final ActionListener al = e -> {

            if (e.getSource().equals(save)) {
                save();

            } else if (e.getSource().equals(load)) {
                load();
            }
        };

        save.addActionListener(al);
        load.addActionListener(al);

        window.revalidate();
        window.repaint();

    }

    // Attempts to save the current budget to file based on user input filename
    private void save() {
        int answer = 0;
        if (!note.getLastSavedPath().equals("")) {
            fc.setSelectedFile(new File(note.getLastSavedPath()));
        }
        int returnVal = fc.showSaveDialog(NoteFrame.this);



        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            System.out.println(file.getName());

            // Add correct file extension
            if (file.getName().length() <= note.getFILEENDING().length()) {
                file = new File(file.toString() + note.getFILEENDING());
            } else if (!file.getName().toLowerCase().substring(file.getName().length()-note.getFILEENDING().length()).equals(note.getFILEENDING())) {
                file = new File(file.toString() + note.getFILEENDING());
            }

            if (file.exists() && !file.isDirectory()) {
                String warning = "A file already exists with that name. Do you wish to overwrite it?";
                String[] b = {"Overwrite", "Cancel"};
                answer = JOptionPane.showOptionDialog(NoteFrame.this, warning, "File already exists", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, b, b[1]);
            }

        if (answer == 0) {
            try (FileOutputStream fileOut = new FileOutputStream(file.getAbsolutePath())) {
                try (ObjectOutput out = new ObjectOutputStream(fileOut)) {
                    out.writeObject(note);
                    out.close();
                    fileOut.close();

                    note.setLastSavedPath(file.getAbsolutePath());
                }
            } catch (IOException problem) {
                problem.printStackTrace();
            }
        }
        }

    }


    // Asks user if they want to save the current budget before opening a new one,
    // then asks for a filename. If it exists, the budget opens. If not, the user
    // gets a message and the program continues with the current budget.
    private void load() {

        String warning = "Do you wish to save your current session before loading a new one?";
        String[] b = {"Yes", "No"};
        int answer = JOptionPane.showOptionDialog(NoteFrame.this, warning, "Save current first?", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, b, b[1]);
        if (answer == 0) {
            save();

        }

        int returnVal = fc.showOpenDialog(NoteFrame.this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();

            // Tries to open the requested file
            try (FileInputStream fileIn = new FileInputStream(file.getAbsolutePath())) {
                try (ObjectInput in = new ObjectInputStream(fileIn)) {
                    Note newNote = (Note) in.readObject();
                    in.close();
                    fileIn.close();
                    note = newNote;
                    updateAll();

                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            } catch (FileNotFoundException fex) {
                System.out.println("File could not be found");
                fex.printStackTrace();
            } catch (IOException iex) {
                iex.printStackTrace();
            } catch (ClassNotFoundException cex) {
                System.out.println("Class not found");
                cex.printStackTrace();
            }
        }

    }




    private void setupTagSearch() {
        JTextField tagSearchField = new JTextField();
        tagSearchField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                System.out.println("KeyEvent " + e.toString());
                JTextField tagField = (JTextField) e.getSource();

                System.out.println("Keycode = " + e.getKeyCode());

                System.out.println("Input: " + tagField.getText() );

                if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    System.out.println("backspace");
                    foundTags = null;
                }

                foundTags = note.searchTags(tagField.getText(), foundTags);
                updateTags();
                tagsPanel.revalidate();
                tagsPanel.repaint();
            }
        });


        tagSearchField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField searchField = (JTextField) e.getSource();
                searchField.setText("");
                foundTags = null;
                updateTags();
                tagsPanel.revalidate();
                tagsPanel.repaint();
            }
        });


        topPanel.add(tagSearchField, "aligny baseline, align right, width 150, height 30");
    }




    private void updateSeasons() {
        seasonPanel.removeAll();
        for (Season s: note.getSeasons()) {
            JLabel se = new JLabel(s.getName());

            String imagePath = (s.expands()) ? "/images/expanded.png" : "/images/unexpanded.png";
            imagePath = (s.getEpisodeList().isEmpty()) ? "/images/empty.png" : imagePath;
            Image expandPicture = new ImageIcon(this.getClass().getResource(imagePath)).getImage();

            JLabel expandLabel = new JLabel(new ImageIcon(expandPicture));
            seasonPanel.add(expandLabel, "split 2");
            expandLabel.putClientProperty("getSe", s);
            expandLabel.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    JLabel label = (JLabel) e.getSource();
                    Season season = (Season) label.getClientProperty("getSe");
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        season.flipExpands();
                        updateSubtitles();
                        updateSeasons();
                        subtitlePanel.revalidate();
                        subtitlePanel.repaint();
                        seasonPanel.revalidate();
                        seasonPanel.repaint();
                    }
                }
                @Override                public void mousePressed(MouseEvent e) {                }
                @Override                public void mouseReleased(MouseEvent e) {                }
                @Override                public void mouseEntered(MouseEvent e) {                }
                @Override                public void mouseExited(MouseEvent e) {                }
            });


            se.putClientProperty("getSe", s);
            se.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    JLabel label = (JLabel) e.getSource();
                    Season season = (Season) label.getClientProperty("getSe");
                    if (e.getClickCount() == 2) {
                        editSeason(season);
                    }

                }
                @Override                public void mousePressed(MouseEvent e) {}
                @Override                public void mouseReleased(MouseEvent e) {}
                @Override                public void mouseEntered(MouseEvent e) {}
                @Override                public void mouseExited(MouseEvent e) {}

            });
            seasonPanel.add(se, "gapleft 3px, wrap");

            if (s.expands()) {
                for (Episode e: s.getEpisodeList()) {
                    JLabel ep = new JLabel(e.getName());
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
                                editEpisode(episode);
                            }
                        }

                        public void mouseReleased(MouseEvent event) {}
                        public void mousePressed(MouseEvent event) {}
                        public void mouseEntered(MouseEvent event) {}
                        public void mouseExited(MouseEvent event) {}
                    });

                    seasonPanel.add(ep, "gapleft 25px, pushx, wrap");

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


    private boolean justSpacesInString(String text) {
        if (text.equals("")) { return true; }

        for (char c: text.toCharArray()) {
            if (c != ' ') {
                return false;
            }
        }

        return true;
    }

    private void editEpisode(Episode e) {
        JTextField episodeName = new JTextField(e.getName());
        JComboBox<Season> season = new JComboBox<>();
        note.getSeasons().forEach(season::addItem);
        season.setEditable(false);
        season.setSelectedItem(e.getSeason());

        JPanel inputs = createInputs(episodeName, season, true);
        String[] buttons = { "Save", "Delete Episode", "Cancel" };

        int ans = JOptionPane.showOptionDialog(NoteFrame.this, inputs, "Edit Episode", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, buttons, buttons[0]);


        if (ans == 1) {
            String warning = "WARNING: Removing an Episode will remove all its SceneNotes, Subtitles and Tags. Do you wish to delete the Episode?";
            String[] b = {"Yes", "No"};
            int a = JOptionPane.showOptionDialog(NoteFrame.this, warning, "WARNING!", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, b, b[1]);
            if (a == 0) {
                note.removeEpisode(e);
                updateAll();
            }

        } else if (ans == 0) {
            String newName = episodeName.getText().trim();
            if (!justSpacesInString(newName)) {
                e.setEpisodeName(newName);
                Season newSeason = (Season) season.getSelectedItem();
                if (!e.getSeason().equals(newSeason)) {
                    e.getSeason().getEpisodeList().remove(e);
                    e.setSeason(newSeason);
                    newSeason.addEpisode(e);
                }

            } else {
                System.out.println("Name cannot only contains spaces");
            }
            updateAll();
        }
    }


    private void editSeason(Season s) {
        JPanel inputs = new JPanel(new MigLayout());
        JTextField seasonName = new JTextField(s.getName());
        JTextField episodeName = new JTextField();
        inputs.add(new JLabel("Season name:"), "wrap");
        inputs.add(seasonName, "grow, pushx, wrap");
        inputs.add(new JLabel("Add episodes (separated by commas)"), "wrap");
        inputs.add(episodeName, "grow, pushx, wrap");

        String[] buttons = { "Save", "Delete Season", "Cancel" };

        int ans = JOptionPane.showOptionDialog(NoteFrame.this, inputs, "Edit Season", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, buttons, buttons[0]);

        if (ans == 1) {
            String warning = "WARNING: Removing a Season will remove all its Episodes, including their SceneNotes, Subtitles and Tags. Do you wish to delete the Season?";
            String[] b = {"Yes", "No"};
            int a = JOptionPane.showOptionDialog(NoteFrame.this, warning, "WARNING!", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, b, b[1]);
            if (a == 0) {
                s.clearEpisodes();
                note.getSeasons().remove(s);
                updateAll();
            }

        } else if (ans == 0) {
            String newName = seasonName.getText().trim();
            if (!justSpacesInString(newName)) {
                s.setName(newName);
            }
            String[] newEpisodes = episodeName.getText().split(",");

            for (String ep : newEpisodes) {
                ep = ep.trim();
                if (!justSpacesInString(ep)) {
                    Episode e = new Episode(ep, s);
                    s.addEpisode(e);
                }
            }
            updateSeasons();
            seasonPanel.revalidate();
            seasonPanel.repaint();
        }
    }



    public void updateAll() {
        updateSubtitles();
        updateSeasons();
        updateSceneNotes();
        updateTags();

        window.revalidate();
        window.repaint();
    }


    private void saveNotes() {
        for (JTextArea a: activeNotesList) {
            SceneNote s = (SceneNote) a.getClientProperty("getScene");
            s.setNote(a.getText());
        }
        activeNotesList.clear();
    }



    private void updateSubtitles() {
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
                        bit.setScrollPos(subtitleScroll.getViewport().getViewPosition());
                        newSceneNote(bit);
                        updateSceneNotes();
                        updateSubtitles(bit);

                        window.revalidate();
                        window.repaint();

                    }
                }
                @Override                public void mousePressed(MouseEvent e) {     }
                @Override                public void mouseReleased(MouseEvent e) {         }
                @Override                public void mouseEntered(MouseEvent e) {               }
                @Override                public void mouseExited(MouseEvent e) {               }
            });
            subtitlePanel.add(label, "wrap, gap 0px 50px");
        }

        subtitleScroll.getViewport().setViewPosition(note.getSelectedEpisode().getSubtitleScrollPos());
    }


    private void updateSubtitles(SubtitleBit b) {
        subtitlePanel.removeAll();
        try {
            subtitlePanel.add(new JLabel("<html><h3> Subtitles for " + note.getSelectedEpisode().toString() + "</h3></html>"), "wrap");
        } catch (java.lang.NullPointerException e) {}
        for (SubtitleBit bit: note.getSelectedEpisode().getSubtitles()) {
            JLabel label = new JLabel("<html>" + bit.toString() + "</html>");
            if (bit.equals(b)) {
                label.setOpaque(true);
                label.setBackground(Color.lightGray);

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
                @Override                public void mousePressed(MouseEvent e) {               }
                @Override                public void mouseReleased(MouseEvent e) {               }
                @Override                public void mouseEntered(MouseEvent e) {               }
                @Override                public void mouseExited(MouseEvent e) {              }
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


    public String removeInitialSpaces(String string) {
        return string.trim();

    }

    private void editTag(Tag t) {
        JPanel inputs = new JPanel(new MigLayout());
        JLabel tname = new JLabel("Tag name:");
        JTextField tagName = new JTextField(t.getTag());
        JLabel taliases = new JLabel("Enter aliases (separated by commas)");
        JTextField alias = new JTextField();

        inputs.add(tname, "wrap");
        inputs.add(tagName, "grow, pushx, wrap");
        inputs.add(taliases, "wrap");
        inputs.add(alias, "grow, pushx, wrap");

        String[] buttons = { "Save", "Delete Tag", "Handle Alias(es)", "Cancel" };

        int ans = JOptionPane.showOptionDialog(NoteFrame.this, inputs, "Edit Tag", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, buttons, buttons[0]);

        if (ans == 1) {
            String warning = "WARNING: Removing a Tag will remove it across all your SceneNotes. Do you wish to remove this Tag?";
            String[] b = {"Yes", "No"};
            int a = JOptionPane.showOptionDialog(NoteFrame.this, warning, "WARNING!", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, b, b[1]);
            if (a == 0) {
                note.removeTag(t);
                updateAll();
            }
        } else if (ans == 0) {
            String newName = tagName.getText().trim();

            if (note.tagExists(newName) && alias.getText().trim().isEmpty() && !t.getTag().equals(newName) && !t.hasAlias(newName)) {
                JOptionPane.showMessageDialog(NoteFrame.this, "A Tag already exists with that name. Tag name remains unchanged");
            } else {
                if (!justSpacesInString(newName)) {
                    if (t.hasAlias(newName)) {
                        t.removeAlias(newName);
                        t.addAlias(t.getTag());
                    }
                    t.setTag(newName);
                }
            }

            String[] aliases = alias.getText().split(",");

            for (String al : aliases) {
                al = al.trim();
                if (!justSpacesInString(al)) {
                    if (!note.tagExists(al)) {
                        t.addAlias(al);
                    }
                }
            }
            updateTags();
            updateSceneNotes();
            scenePanel.revalidate();
            scenePanel.repaint();
            tagsPanel.revalidate();
            tagsPanel.repaint();

        } else if (ans == 2) {
            //Remove alias
            JComboBox<String> aliasCombo = new JComboBox<>();
            if (t.getAliases().isEmpty()) {
                String[] OK_button = {"OK"};
                JOptionPane.showOptionDialog(NoteFrame.this, "No Aliases to handle", "No Aliases", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, OK_button, OK_button[0]);
            } else {
                JPanel removeAliasPanel = new JPanel(new MigLayout());
                JLabel removeAliasLabel = new JLabel("Choose an alias to:");
                t.getAliases().forEach(aliasCombo::addItem);
                aliasCombo.setEditable(false);
                removeAliasPanel.add(removeAliasLabel, "wrap");
                removeAliasPanel.add(aliasCombo, "grow");

                String[] removeAliasButtons = {"Remove", "Choose to Dispaly",  "Cancel"};
                int removeAliasAns = JOptionPane.showOptionDialog(NoteFrame.this, removeAliasPanel, "Remove Alias!", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, removeAliasButtons, removeAliasButtons[1]);

                if (removeAliasAns == 0) {
                    if (aliasCombo.getSelectedItem() != null) {
                        t.removeAlias(aliasCombo.getSelectedItem().toString());
                    }
                } else if (removeAliasAns == 1) {
                    if (aliasCombo.getSelectedItem() != null) {
                        String newDisplay = aliasCombo.getSelectedItem().toString();
                        t.addAlias(t.getTag());
                        t.removeAlias(newDisplay);
                        t.setTag(newDisplay);

                    }
                }

            }

        }
    }


    public void updateTags() {
        tagsPanel.removeAll();
        List<Tag> tagsToDraw = (foundTags != null) ? foundTags : note.getTagList();

        for (Tag t: tagsToDraw) {
            JLabel l = new JLabel(t.getTag());

            tagsPanel.add(l, "wrap");

            if (t.getSelectedStatus() == Selected.CONTAINS) {
                l.setOpaque(true);
                l.setBackground(Color.lightGray);
            } else if (t.getSelectedStatus() == Selected.NOT_CONTAINS) {
                l.setOpaque(true);
                l.setBackground(Color.red);
            }



            l.putClientProperty("getTag", t);

            l.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    JLabel label = (JLabel) e.getSource();
                    Tag tag = (Tag) label.getClientProperty("getTag");
                    if (e.getClickCount() == 1 && SwingUtilities.isMiddleMouseButton(e)) {

                        editTag(tag);
                        updateAll();
                    } else if ( SwingUtilities.isRightMouseButton(e)) { //Open menu (middle click)
                        tag.setSelectedStatus(Selected.DESELECTED);
                        label.setOpaque(false);
                    } else if (SwingUtilities.isLeftMouseButton(e)) { //Deselect (right click)
                        tag.rotateSelected();
                        label.setOpaque(true);
                        label.revalidate();
                        label.repaint();
                        if (tag.getSelectedStatus() == Selected.CONTAINS) {  //Rotate selection (left click)
                            label.setBackground(Color.lightGray);
                        } else if (tag.getSelectedStatus() == Selected.NOT_CONTAINS) {
                            label.setBackground(Color.red);
                        } else if (tag.getSelectedStatus() == Selected.DESELECTED) {
                            label.setOpaque(false);
                        }
                        label.revalidate();
                        label.repaint();

                    }
                }
                @Override                public void mousePressed(MouseEvent e) {               }
                @Override                public void mouseReleased(MouseEvent e) {                }
                @Override                public void mouseEntered(MouseEvent e) {                }
                @Override                public void mouseExited(MouseEvent e) {                }
            });
        }


    }

    private void updateLocalTagPanel(JPanel tagP, SceneNote sceneNote) {
        tagP.removeAll();
        for (Tag t: sceneNote.getTagList()) {
            JLabel l = new JLabel(t.toString());
            l.putClientProperty("getScene", sceneNote);
            l.putClientProperty("getTag", t);
            l.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    JLabel tagLabel = (JLabel) e.getSource();
                    JPanel tagPanel = (JPanel) tagLabel.getParent();
                    Tag tag = (Tag) tagLabel.getClientProperty("getTag");
                    SceneNote scene = (SceneNote) tagLabel.getClientProperty("getScene");
                    String message = "Do you wish to remove this tag from this SceneNote?";
                    String[] removeTagButtons = {"Remove", "Cancel"};
                    int removeTagAns = JOptionPane.showOptionDialog(NoteFrame.this, message, "Remove Tag!", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, removeTagButtons, removeTagButtons[1]);

                    if (removeTagAns == 0) {
                        scene.removeTag(tag);
                        if (!note.multipleOccurrencesOfTag(tag)) {
                            note.removeTag(tag);
                        }
                    }
                    updateLocalTagPanel(tagPanel, scene);
                    updateTags();
                    tagsPanel.revalidate();
                    tagsPanel.repaint();
                }
                @Override                public void mousePressed(MouseEvent e) {                }
                @Override                public void mouseReleased(MouseEvent e) {                }
                @Override                public void mouseEntered(MouseEvent e) {                }
                @Override                public void mouseExited(MouseEvent e) {                }
            });

            tagP.add(l, "gapright 10");
        }
        tagP.revalidate();
        tagP.repaint();
    }



    private void updateSceneNotes() {
        scenePanel.removeAll();
        try {
            scenePanel.add(new JLabel("<html><h1> Notes and Tags for " + note.getSelectedEpisode().toString() + "</h1></html>"), "wrap");
        } catch (java.lang.NullPointerException e) {}

        List<SceneNote> notesList = note.getSelectedEpisode().getNotes();

        //Sort list of notes based on starting time, aka chronological order compared to the subtitles
        Collections.sort(notesList, (object1, object2) -> object1.getStartTime().compareTo(object2.getStartTime()));
        note.getSelectedEpisode().setNotes(notesList);

        for (SceneNote s: notesList) {
            JPanel panel = new JPanel(new MigLayout());
            panel.setBackground(Color.decode("#d8d8d8"));
            JPanel tagPanel = new JPanel(new WrapLayout(FlowLayout.CENTER, 22, 7));
            tagPanel.setBackground(Color.decode("#d8d8d8"));
            //Adding order matters! Make sure tagPanel is added first, see tagInput action listener!
            panel.add(tagPanel, "cell 1 1, grow, pushx, pushy, spany, width 100:300:650");

            Java2sAutoTextField tagInputField= new Java2sAutoTextField(note.getTagList());
            TextPrompt textPrompt = new TextPrompt("Add tag(s):", tagInputField);
            textPrompt.setForeground( Color.LIGHT_GRAY );
            textPrompt.changeStyle(Font.ITALIC);
            textPrompt.setShow(TextPrompt.Show.FOCUS_LOST);
            //panel.add(new JLabel("Add Tag(s):"), " cell 1 0");
            panel.add(tagInputField, "cell 1 0, width 50:200:200, height 23:23:23, aligny top");
            tagInputField.putClientProperty("getScene", s);
            tagInputField.setStrict(false);
            tagInputField.setText("");

            tagInputField.putClientProperty("getTagInput", tagInputField);

            //Tag input
            tagInputField.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    Java2sAutoTextField input = (Java2sAutoTextField) e.getSource();
                    List<String> tagNamesList = note.getAllTagNames();
                    Collections.sort(tagNamesList);
                    input.setDataList(tagNamesList);
                    input.setText("");

                }
                @Override                public void mousePressed(MouseEvent e) {                }
                @Override                public void mouseReleased(MouseEvent e) {                }
                @Override                public void mouseEntered(MouseEvent e) {                }
                @Override                public void mouseExited(MouseEvent e) {                }
            });

            // Taginput actionlistener
            tagInputField.addActionListener(e -> {
                Java2sAutoTextField input = (Java2sAutoTextField) e.getSource();
                String tagText = input.getText().trim();
                String[] split = tagText.split("\\(");
                tagText = split[0].trim();
                input.setText("");
                JPanel tagP = (JPanel) input.getParent().getComponent(0);
                SceneNote scene = (SceneNote) input.getClientProperty("getScene");

                if (note.tagExists(tagText)) {
                    if (!scene.hasTag(note.getTag(tagText))) {
                        scene.addTag(note.getTag(tagText));
                    }

                } else {
                    if (!justSpacesInString(tagText)) {
                        Tag tag = new Tag(tagText);
                        note.addTag(tag);
                        scene.addTag(tag);
                    }
                }
                updateTags();
                tagsPanel.revalidate();
                tagsPanel.repaint();
                updateLocalTagPanel(tagP, scene);
            });

            //Delete button SceneNote
            String imagePath = "/images/lightgraycross.png";
            Image deleteSceneNotePicture = new ImageIcon(this.getClass().getResource(imagePath)).getImage();

            JLabel deleteSceneNoteLabel = new JLabel(new ImageIcon(deleteSceneNotePicture));
            JPanel deleteSceneNotePanel = new JPanel();
            deleteSceneNotePanel.add(deleteSceneNoteLabel);
            panel.add(deleteSceneNotePanel, "cell 3 0, align right, aligny top");
            deleteSceneNotePanel.putClientProperty("getNote", s);
            deleteSceneNotePanel.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    JPanel p = (JPanel) e.getSource();
                    SceneNote sn = (SceneNote) p.getClientProperty("getNote");
                    String warning = "WARNING: Removing a SceneNote will remove all its Tags and its association with the Subtitles. Do you wish to delete the SceneNote?";
                    String[] b = {"Yes", "No"};
                    int a = JOptionPane.showOptionDialog(NoteFrame.this, warning, "WARNING!", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, b, b[1]);
                    if (a == 0) {
                        note.deleteSceneNote(sn);
                        updateAll();
                    }
                }
                @Override                public void mousePressed(MouseEvent e) {                }
                @Override                public void mouseReleased(MouseEvent e) {                }
                @Override                public void mouseEntered(MouseEvent e) {
                    JPanel p = (JPanel) e.getSource();
                    Image redCross = new ImageIcon(this.getClass().getResource("/images/redcross.png")).getImage();
                    JLabel redCrossL = new JLabel(new ImageIcon(redCross));
                    p.removeAll();
                    p.add(redCrossL);
                    p.revalidate();
                    p.repaint();

                }
                @Override                public void mouseExited(MouseEvent e) {
                    JPanel p = (JPanel) e.getSource();
                    Image redCross = new ImageIcon(this.getClass().getResource("/images/lightgraycross.png")).getImage();
                    JLabel redCrossL = new JLabel(new ImageIcon(redCross));
                    p.removeAll();
                    p.add(redCrossL);
                    p.revalidate();
                    p.repaint();

                }
            });

            //panel.add(new JLabel("Start time: " + s.getStartTime()), "cell 1 0, align right");



            JTextArea textArea = new JTextArea(s.getNote());
            textArea.setPreferredSize(new Dimension(100, 60));
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
                @Override                public void mousePressed(MouseEvent e) {                }
                @Override                public void mouseReleased(MouseEvent e) {                }
                @Override                public void mouseEntered(MouseEvent e) {                }
                @Override                public void mouseExited(MouseEvent e) {                }
            });
            activeNotesList.add(textArea);
            panel.add(textArea, "cell 0 0, spany, grow, width 100:500:700");


            JLabel sn = new JLabel(s.getNote());
            updateLocalTagPanel(tagPanel, s);


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
        JPanel inputs = new JPanel(new MigLayout());
        JTextField seasonName = new JTextField();
        JTextField episodeName = new JTextField();
        inputs.add(new JLabel("Season name:"), "wrap");
        inputs.add(seasonName, "grow, pushx, wrap");
        inputs.add(new JLabel("Add episodes (separated by commas)"), "wrap");
        inputs.add(episodeName, "grow, pushx, wrap");

        String[] buttons = { "Save", "Cancel" };

        int ans = JOptionPane.showOptionDialog(NoteFrame.this, inputs, "Add Season", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, buttons, buttons[0]);


        if (ans == 0) {
            String[] seasonNames = seasonName.getText().split(",");
            for (String se: seasonNames) {
                String newName = se.trim();
                Season s = new Season(newName);
                if (!justSpacesInString(newName)) {
                    note.addSeason(s);
                    String[] newEpisodes = episodeName.getText().split(",");

                    for (String ep : newEpisodes) {
                        ep = ep.trim();
                        if (!justSpacesInString(ep)) {
                            Episode e = new Episode(ep, s);
                            s.addEpisode(e);
                        }
                    }
                    updateSeasons();
                    seasonPanel.revalidate();
                    seasonPanel.repaint();

                } else {
                    JOptionPane.showMessageDialog(NoteFrame.this, "The Season name cannot be empty");
                }
            }
        }
    }

    // Removes a main category, and all of its sub categories
    public void removeSeason() {
        if (note.getSeasons().isEmpty()) { return; }

        JComboBox<Season> seasonComboBox = new JComboBox<>();
        note.getSeasons().forEach(seasonComboBox::addItem);
        seasonComboBox.setEditable(false);
        seasonComboBox.setSelectedIndex(0);

        String[] buttons = { "Remove", "Cancel" };

        int ans = JOptionPane.showOptionDialog(NoteFrame.this, seasonComboBox, "Season to remove", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, buttons, buttons[1]);

        if (ans == 0) {
            String warning = "WARNING: Removing a Season will remove all its Episodes, including their SceneNotes, Subtitles and Tags. Do you wish to delete the Season?";
            String[] b = {"Yes", "No"};
            int a = JOptionPane.showOptionDialog(NoteFrame.this, warning, "WARNING!", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, b, b[1]);
            if (a == 0) {
                Season s = (Season) seasonComboBox.getSelectedItem();
                s.clearEpisodes();
                note.getSeasons().remove(s);
                updateAll();
            }
        }

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
        JTextField episodeName = new JTextField();
        JComboBox<Season> season = new JComboBox<>();
        note.getSeasons().forEach(season::addItem);
        season.setEditable(false);
        season.setSelectedIndex(0);

        JPanel inputs = createInputs(episodeName, season, true);
        String[] buttons = { "Save", "Cancel" };

        int ans = JOptionPane.showOptionDialog(NoteFrame.this, inputs, "Edit Episode", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, buttons, buttons[0]);


        if (ans == 0) {
            String newName = episodeName.getText().trim();
            if (!justSpacesInString(newName)) {
                Season newSeason = (Season) season.getSelectedItem();
                Episode e = new Episode(newName, newSeason);
                newSeason.addEpisode(e);

            } else {
                System.out.println("Name cannot only contains spaces");
            }
            updateAll();
        }
    }


    // Removes a sub category of a users choise, and also removes all transactions
    // that are associated with that sub category
    public void removeEpisode() {
        if (note.getSeasons().isEmpty()) { return; }
        if (note.getSelectedEpisode() != null) {
            Episode e = note.getSelectedEpisode();
            String warning = "WARNING: Removing an Episode will remove all its SceneNotes, Subtitles and Tags. Do you wish to delete the Episode?";
            String[] b = {"Yes", "No"};
            int r = JOptionPane.showOptionDialog(NoteFrame.this, warning, "WARNING!", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, b, b[1]);
            if (r == 0) {
                note.removeEpisode(e);
                updateAll();
            }
        } else {
            JComboBox<Season> season = new JComboBox<>();
            note.getSeasons().forEach(season::addItem);
            season.setEditable(false);
            season.setSelectedIndex(0);
            String[] buttons = {"Choose Season", "Cancel"};
            int ans = JOptionPane.showOptionDialog(NoteFrame.this, season, "Choose Season to delete Episode from", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, buttons, buttons[0]);

            if (ans == 0) {
                Season s = (Season) season.getSelectedItem();
                JComboBox<Episode> episode = new JComboBox<>();
                s.getEpisodeList().forEach(episode::addItem);
                episode.setEditable(false);
                episode.setSelectedIndex(0);

                int a = JOptionPane.showOptionDialog(NoteFrame.this, episode, "Choose Episode to delete", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, buttons, buttons[0]);

                if (a == 0) {
                    Episode e = (Episode) episode.getSelectedItem();
                    String warning = "WARNING: Removing an Episode will remove all its SceneNotes, Subtitles and Tags. Do you wish to delete the Episode?";
                    String[] b = {"Yes", "No"};
                    int r = JOptionPane.showOptionDialog(NoteFrame.this, warning, "WARNING!", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, b, b[1]);
                    if (r == 0) {
                        note.removeEpisode(e);
                        updateAll();
                    }
                }
            }
        }

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
        String warning = "WARNING: Removing Subtitles will remove all association with its SceneNotes. Do you wish to delete the Subtitles?";
        String[] b = {"Yes", "No"};
        int r = JOptionPane.showOptionDialog(NoteFrame.this, warning, "WARNING!", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, b, b[1]);
        if (r == 0) {
            note.removeSubtitles();
            updateSubtitles();
            subtitlePanel.revalidate();
            subtitlePanel.repaint();
        }
    }




    public JButton getAddSeasonButton() {return addSeasonButton;}
    public JButton getRemoveSeasonButton() {return removeSeasonButton;}
    public JButton getAddEpisodeButton() {return addEpisodeButton;}
    public JButton getRemoveEpisodeButton() {return removeEpisodeButton;}
    public JButton getAddSceneNoteButton() {return addSceneNoteButton;}
    public JButton getAddSubtitlesButton() {return addSubtitlesButton;}
    public JButton getRemoveSubtitlesButton() {return removeSubtitlesButton;}


}
