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
import sun.security.krb5.SCDynamicStoreConfig;


import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.security.Key;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;




//TODO: Add functionality to set custom Start Time for SceneNote
//TODO: Add functionality to add choose/add/remove Color for tags


public class NoteFrame extends JFrame {


    private Note note;
    private JFrame window = new JFrame("TakeNote");


    /* -------------------------------------------------------------------
     * 	Main Panels
     *  ------------------------------------------------------------------*/


    private JPanel seasonPanel = new JPanel(new MigLayout());
    private JPanel subtitlePanel = new JPanel(new MigLayout());

    private JPanel scenePanel = new JPanel(new MigLayout());
    private JPanel tagsPanel = new JPanel(new MigLayout());
    private JPanel topPanel = new JPanel(new MigLayout());


    /* -------------------------------------------------------------------
     * 	Buttons
     *  ------------------------------------------------------------------*/

    private JButton addSeasonButton = new JButton("Add Season");
    private JButton removeSeasonButton = new JButton("Delete Season");
    private JButton addEpisodeButton = new JButton("Add Episode");
    private JButton removeEpisodeButton = new JButton("Delete Episode");
    private JButton addSceneNoteButton = new JButton("Add Note");
    private JButton addSubtitlesButton = new JButton("Add Subtitles");
    private JButton removeSubtitlesButton = new JButton("Delete Subtitles");
    private JButton deselectAllTagsButton = new JButton("Deselect All Tags");
    private JButton notAllUnselectedTagsButton = new JButton("NOT All Unselected Tags");
    private JButton searchButton = new JButton("Search");



    private JCheckBox searchOnlySelected = new JCheckBox("Search selected Season/Episode only");

    /* -------------------------------------------------------------------
     * 	Scrollpanels
     *  ------------------------------------------------------------------*/
    private JScrollPane subtitleScroll;
    private JScrollPane seasonScroll;
    private JScrollPane sceneNoteScroll;
    private JScrollPane tagScroll;


    /* -------------------------------------------------------------------
     * 	File Chooser
     *  ------------------------------------------------------------------*/
    private JFileChooser fc = new JFileChooser();


    //SceneNotes
    private List<JTextArea> activeNotesList;

    //Tags
    private List<Tag> foundTags;
    private JTextField tagSearchField;


    /* -------------------------------------------------------------------
     * 	Search helpers
     *  ------------------------------------------------------------------*/
    private JTextField searchField;

    private List<SceneNote> foundNotes;
    private List<SubtitleBit> foundSubs;

    private JLabel activeSub;
    private SceneNote activeNote;

    private Map<SubtitleBit, JLabel> activeSubtitleBits;
    private Map<SceneNote, JPanel> activeNotes;

    private boolean searchModeEnabled = true;



    /* -------------------------------------------------------------------
     * 	Constructor
     *  ------------------------------------------------------------------*/

    NoteFrame(Note note) {
        this.note = note;
        window.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        window.setExtendedState(JFrame.MAXIMIZED_BOTH);
        window.setVisible(true);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setupFrame();
    }

    public Note getNote() {
        return note;
    }



    /* -------------------------------------------------------------------
     * 	Setup
     *  ------------------------------------------------------------------*/
    private void setupFrame() {
        window.setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());
        searchModeEnabled = false;


        activeNotesList = new ArrayList<>();
        activeSubtitleBits = new HashMap<>();
        activeNotes = new HashMap<>();
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


        JSplitPane notesAndSub = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, subtitleScroll, sceneNoteScroll);
        JSplitPane tagAndNotesSub = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, notesAndSub, tagScroll);
        JSplitPane seasonAndSubNotesTags = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, seasonScroll, tagAndNotesSub);
        JSplitPane topAndBottom = new JSplitPane(JSplitPane.VERTICAL_SPLIT, topPanel, seasonAndSubNotesTags);



        //subtitlesAndNotes.setPreferredSize(new Dimension(100, 550));
        topPanel.setMinimumSize(new Dimension(0, 67));
        seasonScroll.setPreferredSize(new Dimension(150, 100));
        subtitleScroll.setPreferredSize(new Dimension(300, 100));
        subtitleScroll.getVerticalScrollBar().setUnitIncrement(16);
        sceneNoteScroll.setPreferredSize(new Dimension(1200, 100));
        sceneNoteScroll.getVerticalScrollBar().setUnitIncrement(16);
        tagScroll.setPreferredSize(new Dimension(150, 100));
        tagScroll.getVerticalScrollBar().setUnitIncrement(16);
        topAndBottom.setPreferredSize(new Dimension(600, 800));
        //tagsPanel.setPreferredSize(new Dimension(100, 100));


        topPanel.add(addSeasonButton, "cell 0 0, grow");
        topPanel.add(removeSeasonButton, "cell 0 1, grow");
        topPanel.add(addEpisodeButton, "cell 1 0, grow");
        topPanel.add(removeEpisodeButton, "cell 1 1, grow");
        topPanel.add(addSceneNoteButton, "cell 3 0, spany 2, grow, gapright 150px");
        topPanel.add(addSubtitlesButton, "cell 2 0, grow");
        topPanel.add(removeSubtitlesButton, "cell 2 1, grow");


        addSeasonButton.addActionListener(new ButtonListener(this));
        removeSeasonButton.addActionListener(new ButtonListener(this));
        addEpisodeButton.addActionListener(new ButtonListener(this));
        removeEpisodeButton.addActionListener(new ButtonListener(this));
        addSceneNoteButton.addActionListener(new ButtonListener(this));
        addSubtitlesButton.addActionListener(new ButtonListener(this));
        removeSubtitlesButton.addActionListener(new ButtonListener(this));
        searchButton.addActionListener(new ButtonListener(this));

        deselectAllTagsButton.addActionListener(new ButtonListener(this));
        notAllUnselectedTagsButton.addActionListener(new ButtonListener(this));



        setupTagSearch();
        createMenus();
        setupSearch();
        setupShortcuts();
        window.add(topAndBottom);
    }


    private void setupShortcuts() {

        Action focusTagSearch = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                tagSearchField.requestFocus();
            }
        };
        tagSearchField.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.CTRL_DOWN_MASK),
                "focusTagSearch");
        tagSearchField.getActionMap().put("focusTagSearch", focusTagSearch);


        Action focusSearchField = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                searchField.requestFocus();
            }
        };
        searchField.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_DOWN_MASK),
                "focusSearchField");
        searchField.getActionMap().put("focusSearchField", focusSearchField);


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

    /* -------------------------------------------------------------------
     * 	Save / Load
     *  ------------------------------------------------------------------*/

    private void save() {
        saveNotes();
        int answer = 0;
        if (!note.getLastSavedPath().equals("")) {
            fc.setSelectedFile(new File(note.getLastSavedPath()));
        }
        int returnVal = fc.showSaveDialog(NoteFrame.this);



        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();


            // Add correct file extension
            if (file.getName().length() <= note.getFILEENDING().length()) {
                file = new File(file.toString() + note.getFILEENDING());
            } else if (!file.getName().toLowerCase().substring(file.getName().length()-note.getFILEENDING().length()).equals(note.getFILEENDING())) {
                file = new File(file.toString() + note.getFILEENDING());
            }

            if (file.exists() && !file.isDirectory() && !file.getAbsolutePath().equals(note.getLastSavedPath())) {
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

    private void load() {
        saveNotes();
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

    /* -------------------------------------------------------------------
     * 	Search Functions
     *  ------------------------------------------------------------------*/

    public void search() {
        if (note.atLeastOneTagIsSelected() || !searchField.getText().equals("")) {
            enableSearchMode();
        } else {
            disableSearchMode();
            return;
        }

        if (note.atLeastOneTagIsSelected()) {
            foundNotes = note.notesFilteredByTags(searchOnlySelected.isSelected());

        } else {
            foundNotes = null;
        }

        if (!searchField.getText().equals("")) {
            foundNotes = note.searchNotes(searchField.getText(), foundNotes, searchOnlySelected.isSelected());
            foundSubs = note.searchSubs(searchField.getText(), searchOnlySelected.isSelected());

        } else if (!note.atLeastOneTagIsSelected()) {
            foundNotes = null;
        }

        updateSceneNotes();
        if (foundSubs == null || foundSubs.isEmpty()) {
            noSubsFound();
        } else {
            updateSubtitles(foundSubs.get(0));
        }
    }


    private void searchByTag() {
        if (note.atLeastOneTagIsSelected() || !searchField.getText().equals("")) {
            enableSearchMode();
        } else {
            disableSearchMode();
            return;
        }
        if (!searchField.getText().equals("")) {
            search();
            return;
        }

        if (note.atLeastOneTagIsSelected()) {
            foundNotes = note.notesFilteredByTags(searchOnlySelected.isSelected());

        } else {
            foundNotes = null;
        }
        updateSceneNotes();
    }

    /* -------------------------------------------------------------------
     * 	Setup Search & Search Helper Functions
     *  ------------------------------------------------------------------*/


    public void resetSearch() {
        note.deselectAllTags();
        searchField.setText("");
    }

    private void noSubsFound() {
        subtitlePanel.removeAll();
        subtitlePanel.add(new JLabel("<html><h2> No results found </h2></html>"));
        subtitlePanel.revalidate();
        subtitlePanel.repaint();
    }

    private void setupSearch() {
        searchField = new JTextField();
        TextPrompt textPrompt = new TextPrompt("Search notes:", searchField);
        textPrompt.setForeground( Color.LIGHT_GRAY );
        textPrompt.changeStyle(Font.ITALIC);
        textPrompt.setShow(TextPrompt.Show.FOCUS_LOST);

        searchField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                search();
            }

        });
        searchField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                searchField.setText("");
            }

            @Override
            public void focusLost(FocusEvent e) {

            }
        });

        topPanel.add(searchField, "cell 4 1, width 150, height 27, split 2");
        topPanel.add(searchButton, "cell 5 1");
        topPanel.add(searchOnlySelected, "cell 4 0, grow");
        searchOnlySelected.setMnemonic(KeyEvent.VK_E);


        topPanel.add(notAllUnselectedTagsButton, "align right,cell 7 0");
        topPanel.add(deselectAllTagsButton, "cell 7 0,  split 2");
    }



    private void setupTagSearch() {
        tagSearchField = new JTextField();
        TextPrompt textPrompt = new TextPrompt("Search tag(s):", tagSearchField);
        textPrompt.setForeground( Color.LIGHT_GRAY );
        textPrompt.changeStyle(Font.ITALIC);
        textPrompt.setShow(TextPrompt.Show.FOCUS_LOST);


        tagSearchField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {           }

            @Override
            public void keyPressed(KeyEvent e) {            }

            @Override
            public void keyReleased(KeyEvent e) {
                JTextField tagField = (JTextField) e.getSource();

                if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    foundTags = null;
                }

                if (e.getKeyCode() == KeyEvent.VK_DOWN && foundTags.size() == 1) {
                    foundTags.get(0).rotateSelected();
                }

                foundTags = note.searchTags(tagField.getText(), foundTags);
                updateTags();
            }
        });


        tagSearchField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField searchField = (JTextField) e.getSource();
                searchField.setText("");
                if (foundTags.size() == 1) {
                    foundTags.get(0).setSelectedStatus(Selected.CONTAINS);
                    searchByTag();

                }
                foundTags = null;
                updateTags();
            }
        });
        topPanel.add(tagSearchField, "cell 7 1, push, aligny baseline, align right, width 150, height 25");
    }

    private void enableSearchMode() {
        searchModeEnabled = true;
    }


    private void disableSearchMode() {
        searchModeEnabled = false;
        note.deselectAllTags();
        foundNotes = null;
        foundSubs = null;
        activeNote = null;
        activeSub = null;
        resetSearch();
        updateAll();
    }



    /* -------------------------------------------------------------------
     * 	Update Seasons
     *  ------------------------------------------------------------------*/


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
                        disableSearchMode();
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
                    note.setSelectedSeason(season);
                    disableSearchMode();

                }
                @Override                public void mousePressed(MouseEvent e) {}
                @Override                public void mouseReleased(MouseEvent e) {}
                @Override                public void mouseEntered(MouseEvent e) {}
                @Override                public void mouseExited(MouseEvent e) {}

            });

            if(s.equals(note.getSelectedSeason()) && !searchModeEnabled) {
                se.setBackground(Color.LIGHT_GRAY);
                se.setOpaque(true);
            }

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

                            if (note.getSelectedEpisode() != null) {
                                Point p1 = subtitleScroll.getViewport().getViewPosition();
                                note.getSelectedEpisode().setSubtitleScrollPos(p1);
                                Point p2 = sceneNoteScroll.getViewport().getViewPosition();
                                note.getSelectedEpisode().setSceneNoteScrollPos(p2);
                            }

                            note.setSelectedEpisode(episode);
                            disableSearchMode();

                            if (event.getClickCount() == 2) {
                                editEpisode(episode);
                            }
                        }

                        public void mouseReleased(MouseEvent event) {}
                        public void mousePressed(MouseEvent event) {}
                        public void mouseEntered(MouseEvent event) {}
                        public void mouseExited(MouseEvent event) {}
                    });

                    seasonPanel.add(ep, "gapleft 25px, pushx, wrap");

                    if(e.equals(note.getSelectedEpisode()) && !searchModeEnabled) {
                        ep.setBackground(Color.LIGHT_GRAY);
                        ep.setOpaque(true);
                    }
                }
            }
        }
        seasonPanel.revalidate();
        seasonPanel.repaint();
    }


    /* -------------------------------------------------------------------
     * 	Update All
     *  ------------------------------------------------------------------*/

    public void updateAll() {
        updateSubtitles(new SubtitleBit("00:00:00", "00:00:00", "", "", "", 0, note.getSelectedEpisode(), 0));
        updateSeasons();
        updateSceneNotes();
        updateTags();

        window.revalidate();
        window.repaint();
    }


    /* -------------------------------------------------------------------
     * 	Update Subtitles & Helper Functions
     *  ------------------------------------------------------------------*/


    private void updateSubtitles(SubtitleBit b) {
        subtitlePanel.removeAll();

        if (note.getSelectedSeason() != null && !searchModeEnabled) { return;}

        Episode lastEpisode = null;
        Season lastSeason = null;

        if (searchModeEnabled) {
            subtitlePanel.add(new JLabel("<html><h2> Search results </h2></html>"), "wrap");
        } else if (note.getSelectedEpisode() != null){
            subtitlePanel.add(new JLabel("<html><h3> Subtitles for " + note.getSelectedEpisode().getName() + "</h3></html>"), "wrap");
        } else if (note.getSelectedSeason() != null) {
            subtitlePanel.add(new JLabel("<html><h3> Subtitles for " + note.getSelectedSeason().getName() + "</h3></html>"), "wrap");
        }


        List<SubtitleBit> subtitlesToDraw = new ArrayList<>();
        if (searchModeEnabled) {
            subtitlesToDraw = foundSubs;

        }  else if (note.getSelectedEpisode() != null) {
            subtitlesToDraw = note.getSelectedEpisode().getSubtitles();
        }


        for (SubtitleBit bit: subtitlesToDraw) {
            if (searchModeEnabled) {
                if (bit.getEpisode().getSeason() != null) {
                    if (lastSeason != bit.getEpisode().getSeason()) {
                        lastSeason = bit.getEpisode().getSeason();
                        JLabel headline = new JLabel("<html><h2>"  + bit.getEpisode().getSeason().getName() + "</h2></html>");
                        subtitlePanel.add(headline, "wrap");
                    }
                }
                if (bit.getEpisode() != null) {
                    if (lastEpisode != (bit.getEpisode())) {
                        lastEpisode = bit.getEpisode();
                        JLabel headline = new JLabel("<html><h3>"  + bit.getEpisode().getName() + "</h3></html>");
                        subtitlePanel.add(headline, "wrap");
                    }
                }
            }

            JLabel label = new JLabel("<html>" + bit.getSubtitle() + "</html>");
            if (bit.equals(b) && !searchModeEnabled) {
                label.setOpaque(true);
                label.setBackground(Color.lightGray);
                activeSub = label;
            }

            label.putClientProperty("getBit", bit);
            label.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    JLabel l = (JLabel) e.getSource();
                    SubtitleBit bit = (SubtitleBit) l.getClientProperty("getBit");
                    if (e.getClickCount() == 2 && !searchModeEnabled) {
                        saveNotes();
                        bit.setScrollPos(subtitleScroll.getViewport().getViewPosition());
                        if (activeSub != l && l != null) {
                            repaintSub(l);
                        }
                        newSceneNote(bit);

                    } else if (e.getClickCount() == 2 && searchModeEnabled) {
                        saveNotes();
                        note.setSelectedEpisode(bit.getEpisode());
                        disableSearchMode();
                        updateSubtitles(bit);

                    }
                }
                @Override                public void mousePressed(MouseEvent e) {                }
                @Override                public void mouseReleased(MouseEvent e) {                }
                @Override                public void mouseEntered(MouseEvent e) {                }
                @Override                public void mouseExited(MouseEvent e) {                }
            });
            int smallGap = 7;
            int mediumGap = 12;
            int largeGap = 35;
            int timeForLargeGap = 4000;

            if (bit.getTimeSinceLast() < timeForLargeGap && !searchModeEnabled) {
                subtitlePanel.add(label, "wrap, gapy " + Integer.toString(smallGap) + "px");
            } else if (!searchModeEnabled){
                subtitlePanel.add(label, "wrap, gapy " + Integer.toString(largeGap) +"px");
            } else if (searchModeEnabled) {
                subtitlePanel.add(label, "wrap, gapy " + Integer.toString(mediumGap) +"px");
            }

            activeSubtitleBits.put(bit, label);

        }

        if (b != null && activeSubtitleBits.containsKey(b)) {
            activeSub = activeSubtitleBits.get(b);
            subtitleScroll.validate();
            setCorrectSubtitleScrollHeight();

        } else if (note.getSelectedEpisode() != null) {
            subtitleScroll.getViewport().setViewPosition(note.getSelectedEpisode().getSubtitleScrollPos());
        }
        subtitlePanel.revalidate();
        subtitlePanel.repaint();
    }


    public void setCorrectSubtitleScrollHeight() {
        if (activeSub == null) {
            subtitleScroll.getViewport().setViewPosition(new Point(0, 0));
            subtitlePanel.revalidate();
            subtitlePanel.repaint();
            return;
        }
        int newY = Math.min(subtitlePanel.getHeight(), activeSub.getBounds().y+subtitleScroll.getHeight()/2);
        if (activeSub.getY() > subtitleScroll.getHeight()/2) {
            activeSub.setBounds(activeSub.getBounds().x, newY, activeSub.getBounds().width, activeSub.getBounds().height);
        } else {
            activeSub.setBounds(activeSub.getBounds().x, 0, activeSub.getBounds().width, activeSub.getBounds().height);
        }
        subtitleScroll.getViewport().setViewPosition(new Point(0, 0));
        subtitlePanel.scrollRectToVisible(activeSub.getBounds());
        subtitlePanel.revalidate();
        subtitlePanel.repaint();
    }

    private void repaintSub(JLabel label) {
        if (activeSub != null) {
            activeSub.setOpaque(false);
            activeSub.revalidate();
            activeSub.repaint();
        }
        activeSub = label;
        label.setOpaque(true);
        label.setBackground(Color.lightGray);
        label.revalidate();
        label.repaint();
    }


    /* -------------------------------------------------------------------
     * 	Update/Edit Tags
     *  ------------------------------------------------------------------*/


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
            } else if (t.getSelectedStatus() == Selected.MUST_CONTAIN) {
                l.setOpaque(true);
                l.setBackground(Color.decode("#478dff"));;
            }

            l.putClientProperty("getTag", t);

            l.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    JLabel label = (JLabel) e.getSource();
                    Tag tag = (Tag) label.getClientProperty("getTag");
                    if (e.getClickCount() == 1 && SwingUtilities.isMiddleMouseButton(e)) {//Open menu (middle click)
                        editTag(tag);
                        updateSceneNotes();
                    } else if ( SwingUtilities.isRightMouseButton(e)) {  //Deselect (right click)
                        tag.setSelectedStatus(Selected.DESELECTED);
                        label.setOpaque(false);
                        label.revalidate();
                        label.repaint();
                        searchByTag();

                    } else if (SwingUtilities.isLeftMouseButton(e)) {//Rotate selection (left click)
                        tag.rotateSelected();
                        label.setOpaque(true);
                        Selected status = tag.getSelectedStatus();
                        if (status == Selected.CONTAINS) {
                            label.setBackground(Color.lightGray);
                        } else if (status == Selected.NOT_CONTAINS) {
                            label.setBackground(Color.red);
                        } else if (status == Selected.DESELECTED) {
                            label.setOpaque(false);
                        } else if (status == Selected.MUST_CONTAIN) {
                            label.setBackground(Color.decode("#478dff"));
                        }
                        label.revalidate();
                        label.repaint();
                        searchByTag();

                    }
                }
                @Override                public void mousePressed(MouseEvent e) {               }
                @Override                public void mouseReleased(MouseEvent e) {                }
                @Override                public void mouseEntered(MouseEvent e) {                }
                @Override                public void mouseExited(MouseEvent e) {                }
            });
        }
        tagsPanel.revalidate();
        tagsPanel.repaint();
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

    /* -------------------------------------------------------------------
     * 	Update Notes
     *  ------------------------------------------------------------------*/

    private void updateSceneNotes() {
        scenePanel.removeAll();
        if (!searchModeEnabled && note.getSelectedEpisode() != null) {
            scenePanel.add(new JLabel("<html><h1> Notes and Tags for " + note.getSelectedEpisode().getName() + "</h1></html>"), "wrap");
        } else if (!searchModeEnabled && note.getSelectedSeason() != null) {
            scenePanel.add(new JLabel("<html><h1> Notes and Tags for " + note.getSelectedSeason().getName() + "</h1></html>"), "wrap");
        } else if (searchModeEnabled && (foundNotes != null || !foundNotes.isEmpty())) {
            scenePanel.add(new JLabel("<html><h1> Search results: " + foundNotes.size() + " results found </h1></html>"), "wrap");
        } else if (searchModeEnabled && (foundNotes == null || foundNotes.isEmpty())) {
            scenePanel.add(new JLabel("<html><h1> No results found </h1></html>"), "wrap");
        } else {
            scenePanel.add(new JLabel("<html><h1> This should not be happening! </h1></html>"), "wrap");
            return;
        }

        List<SceneNote> notesList = new ArrayList<>();
        if (note.getSelectedEpisode() != null && !searchModeEnabled) {
            notesList = note.getSelectedEpisode().getNotes();
        } else if (note.getSelectedSeason() != null && !searchModeEnabled) {
            notesList = note.getSelectedSeason().getNotes();

        } else if (searchModeEnabled) {
            notesList = foundNotes;
        }

        Season lastSeason = null;
        Episode lastEpisode = null;

        for (SceneNote s: notesList) {
            if (searchModeEnabled) {

                if (s.getSeason() != null) {
                    if (lastSeason != s.getSeason()) {
                        lastSeason = s.getSeason();
                        JLabel headline = new JLabel("<html><h2>"  + s.getSeason().getName() + "</h2></html>");
                        scenePanel.add(headline, "wrap");
                    }
                }
                if (s.getEpisode() != null) {
                    if (lastSeason != s.getEpisode().getSeason()) {
                        lastSeason = s.getEpisode().getSeason();
                        JLabel headline = new JLabel("<html><h2>"  + s.getEpisode().getSeason().getName() + "</h2></html>");
                        scenePanel.add(headline, "wrap");
                    }

                    if (lastEpisode != (s.getEpisode())) {
                        lastEpisode = s.getEpisode();
                        JLabel headline = new JLabel("<html><h3>"  + s.getEpisode().getName() + "</h3></html>");
                        scenePanel.add(headline, "wrap");
                    }
                }
            }



            JPanel panel = new JPanel(new MigLayout());
            panel.setBackground(Color.decode("#d8d8d8"));
            JPanel tagPanel = new JPanel(new WrapLayout(FlowLayout.CENTER, 22, 7));
            tagPanel.setBackground(Color.decode("#d8d8d8"));


            Java2sAutoTextField tagInputField= new Java2sAutoTextField(note.getTagList());
            TextPrompt textPrompt = new TextPrompt("Add tag(s):", tagInputField);
            textPrompt.setForeground( Color.LIGHT_GRAY );
            textPrompt.changeStyle(Font.ITALIC);
            textPrompt.setShow(TextPrompt.Show.FOCUS_LOST);


            tagInputField.putClientProperty("getScene", s);
            tagInputField.setStrict(false);
            tagInputField.setText("");

            tagInputField.addFocusListener(new FocusListener() {
                @Override
                public void focusGained(FocusEvent e) {
                    Java2sAutoTextField input = (Java2sAutoTextField) e.getSource();
                    SceneNote sn = (SceneNote) input.getClientProperty("getScene");
                    if (note.getSelectedEpisode() != null && !searchModeEnabled) {
                        note.getSelectedEpisode().setActiveSceneNote(sn);

                        if (!sn.getSub().getSubtitle().equals("") && activeSub != null) {
                            repaintSub(activeSubtitleBits.get(sn.getSub()));
                            setCorrectSubtitleScrollHeight();

                        }
                    }
                    colorNote(sn);
                    List<String> tagNamesList = note.getAllTagNames();
                    Collections.sort(tagNamesList);
                    input.setDataList(tagNamesList);
                    input.setText("");
                }

                @Override
                public void focusLost(FocusEvent e) {
                    Java2sAutoTextField input = (Java2sAutoTextField) e.getSource();
                    SceneNote sn = (SceneNote) input.getClientProperty("getScene");
                    input.setText("");
                    unColorNote(sn);
                }
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
                updateLocalTagPanel(tagP, scene);
            });

            //Delete button SceneNote
            String imagePath = "/images/lightgraycross.png";
            Image deleteSceneNotePicture = new ImageIcon(this.getClass().getResource(imagePath)).getImage();

            JLabel deleteSceneNoteLabel = new JLabel(new ImageIcon(deleteSceneNotePicture));
            JPanel deleteSceneNotePanel = new JPanel();
            deleteSceneNotePanel.add(deleteSceneNoteLabel);

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
                        if (searchModeEnabled) {
                            foundNotes.remove(sn);
                        }
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


            JTextArea textArea = new JTextArea(s.getNote());
            textArea.setPreferredSize(new Dimension(100, 60));
            textArea.setWrapStyleWord(true);
            textArea.setLineWrap(true);
            textArea.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, null);
            textArea.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, null);
            textArea.putClientProperty("getScene", s);

            textArea.addFocusListener(new FocusListener() {
                @Override
                public void focusGained(FocusEvent e) {
                    JTextArea ta = (JTextArea) e.getSource();
                    SceneNote sn = (SceneNote) ta.getClientProperty("getScene");
                    if (note.getSelectedEpisode() != null && !searchModeEnabled) {
                        note.getSelectedEpisode().setActiveSceneNote(sn);
                        note.setSelectedSub(sn.getSub());

                        if (!sn.getSub().getSubtitle().equals("") && activeSubtitleBits.get(sn.getSub()) != null) {
                            repaintSub(activeSubtitleBits.get(sn.getSub()));
                            setCorrectSubtitleScrollHeight();

                        }
                    }
                    colorNote(sn);

                }

                @Override
                public void focusLost(FocusEvent e) {
                    JTextArea ta = (JTextArea) e.getSource();
                    SceneNote sn = (SceneNote) ta.getClientProperty("getScene");
                    sn.setNote(ta.getText());
                    unColorNote(sn);
                }
            });

            activeNotesList.add(textArea);
            activeNotes.put(s, panel);

            panel.putClientProperty("getScene", s);
            panel.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2 && searchModeEnabled) {
                        JPanel p = (JPanel) e.getSource();
                        SceneNote sceneNote = (SceneNote) p.getClientProperty("getScene");
                        saveNotes();

                        if (sceneNote.getEpisode() != null) {
                            note.setSelectedEpisode(sceneNote.getEpisode());
                        } else if (sceneNote.getSeason() != null) {
                            note.setSelectedSeason(sceneNote.getSeason());
                        }
                        disableSearchMode();
                        colorNote(sceneNote);
                        SwingUtilities.invokeLater(() -> setCorrectNoteScrollHeight());
                    }
                }
                @Override                public void mousePressed(MouseEvent e) {                }
                @Override                public void mouseReleased(MouseEvent e) {                }
                @Override                public void mouseEntered(MouseEvent e) {                }
                @Override                public void mouseExited(MouseEvent e) {                }
            });

            //Adding order matters! Make sure tagPanel is added first, see tagInput action listener!
            panel.add(tagPanel, "cell 1 1, grow, pushx, pushy, spany, width 100:300:650");
            panel.add(tagInputField, "cell 1 0, width 50:200:200, height 23:23:23, aligny top");
            panel.add(deleteSceneNotePanel, "cell 3 0, align right, aligny top");
            panel.add(textArea, "cell 0 0, spany, grow, width 100:500:700");

            updateLocalTagPanel(tagPanel, s);
            scenePanel.add(panel, "grow, pushx, wrap");
        }

        scenePanel.revalidate();
        scenePanel.repaint();


    }

    /* -------------------------------------------------------------------
     * 	Update Note Helper Functions
     *  ------------------------------------------------------------------*/

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

    public void newSceneNote(SubtitleBit bit) {
        if (searchModeEnabled) { return; }

        SceneNote sn = new SceneNote("", bit);
        boolean isEpisode = false;
        if (note.getSelectedEpisode() != null) {
            isEpisode = true;
            sn.setEpisode(note.getSelectedEpisode());
            if (bit !=null) {
                bit.setEpisode(note.getSelectedEpisode());
            }
        } else if (note.getSelectedSeason() != null) {
            isEpisode = false;
            sn.setSeason(note.getSelectedSeason());
            bit.setEpisode(null);
        }

        List<SceneNote> notesList = (isEpisode ) ? note.getSelectedEpisode().getNotes() : note.getSelectedSeason().getNotes();
        notesList.add(sn);


        //Sort list of notes based on starting time, aka chronological order compared to the subtitles
        Collections.sort(notesList, (object1, object2) -> object1.getStartTime().compareTo(object2.getStartTime()));

        if (isEpisode) {
            note.getSelectedEpisode().setNotes(notesList);
        } else {
            note.getSelectedSeason().setNotes(notesList);
        }

        updateSceneNotes();
        if (activeNote != null) {
            unColorNote(activeNote);
        }
        colorNote(sn);
        SwingUtilities.invokeLater(() -> setCorrectNoteScrollHeight());
    }

    public void setCorrectNoteScrollHeight() {
        scenePanel.validate();
        JPanel notePanel = activeNotes.get(activeNote);

        if (notePanel == null) {
            sceneNoteScroll.getViewport().setViewPosition(new Point(0, 0));
            return;
        }

        int yPos;
        int centerY = notePanel.getBounds().y + (notePanel.getBounds().height/2 );
        int notePanelHeight = scenePanel.getHeight();
        int halfScrollPanelHeight = sceneNoteScroll.getHeight() / 2;

        System.out.println("Y: " + notePanel.getBounds().y + " height/2: " + notePanel.getBounds().height/2 + " total CenterY: " + centerY);
        System.out.println("Notepanel height " + scenePanel.getHeight() + "Half height " + sceneNoteScroll.getHeight() / 2);

        if (centerY < halfScrollPanelHeight) {
            yPos = 0;
        } else if (centerY > notePanelHeight - halfScrollPanelHeight) {
            yPos = scenePanel.getHeight();
        } else {
            yPos = centerY + halfScrollPanelHeight;
        }

        notePanel.setBounds(notePanel.getBounds().x, yPos, notePanel.getBounds().width, notePanel.getBounds().height);
        JTextArea textArea = (JTextArea) notePanel.getComponent(3);
        textArea.requestFocus();
        textArea.setCaretPosition(textArea.getDocument().getLength());

        sceneNoteScroll.getViewport().setViewPosition(new Point(0, 0));

        scenePanel.scrollRectToVisible(notePanel.getBounds());


    }

    public void unColorNote(SceneNote note) {
        if (note == activeNote) { activeNote = null; }
        if (!activeNotes.containsKey(note)) { return; }
        JPanel notePanel = activeNotes.get(note);
        notePanel.setBackground(Color.decode("#d8d8d8"));
        notePanel.getComponent(0).setBackground(Color.decode("#d8d8d8"));
        notePanel.getComponent(0).revalidate();
        notePanel.getComponent(0).repaint();
        notePanel.revalidate();
        notePanel.repaint();
    }

    public void colorNote(SceneNote note) {
        if (!activeNotes.containsKey(note)) { return; }

        JPanel notePanel = activeNotes.get(note);
        notePanel.setBackground(Color.lightGray);
        notePanel.getComponent(0).setBackground(Color.lightGray);
        notePanel.getComponent(0).revalidate();
        notePanel.getComponent(0).repaint();
        notePanel.revalidate();
        notePanel.repaint();

        activeNote = note;
    }



    /* -------------------------------------------------------------------
     * 	Add/Remove/Edit Season
     *  ------------------------------------------------------------------*/

    public void addSeason() {
        if (searchModeEnabled) {
            disableSearchMode();
        }
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

                } else {
                    JOptionPane.showMessageDialog(NoteFrame.this, "The Season name cannot be empty");
                }
            }
        }
    }


    public void removeSeason() {
        if (searchModeEnabled) {
            disableSearchMode();
        }
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
                note.removeSeason(s);
                updateAll();
            }
        }
    }


    private void editSeason(Season s) {
        if (searchModeEnabled) {
            disableSearchMode();
        }
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
        }
    }


    /* -------------------------------------------------------------------
     * 	Add/Remove/Edit Episode
     *  ------------------------------------------------------------------*/


    public void addEpisode() {
        if (searchModeEnabled) {
            disableSearchMode();
        }
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


    public void removeEpisode() {
        if (searchModeEnabled) {
            disableSearchMode();
        }
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


    private void editEpisode(Episode e) {
        if (searchModeEnabled) {
            disableSearchMode();
        }
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

    /* -------------------------------------------------------------------
     * 	Helper Function Episodes/Seasons
     *  ------------------------------------------------------------------*/


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

    private void noEpispdeWithoutSeason() {
        JOptionPane.showMessageDialog(window, "You need to create a Season first", "No Episode Without Season",
                JOptionPane.INFORMATION_MESSAGE);
    }


    /* -------------------------------------------------------------------
     * 	Open/Remove Subtitles
     *  ------------------------------------------------------------------*/


    public void openSubtitles() {
        if (searchModeEnabled) {
            disableSearchMode();
        }
        int open = 0;
        if (note.getSelectedSeason() != null) {
            String warning = "Currently it is not possible to add Subtitles to a Season.";
            String[] b = {"OK"};
            JOptionPane.showOptionDialog(NoteFrame.this, warning, "No Subtitles for Season", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, b, b[0]);
        }

        if (note.getSelectedEpisode() != null) {
            if (!note.getSelectedEpisode().getSubtitles().isEmpty()) {
                String warning = "WARNING: This Episode already has Subtitles. Do you wish to add additional Subtitles?";
                String[] b = {"Yes", "No"};
                open = JOptionPane.showOptionDialog(NoteFrame.this, warning, "WARNING!", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, b, b[1]);

            }
            if (open == 0) {
                int returnVal = fc.showOpenDialog(NoteFrame.this);

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    note.getSelectedEpisode().importSubtitles(file.getAbsolutePath());
                    saveNotes();
                    updateSubtitles(null);
                }
            }

        }
    }


    public void removeSubtitles() {
        if (searchModeEnabled) {
            disableSearchMode();
        }
        if (note.getSelectedSeason() != null) {return;}
        if (note.getSelectedEpisode() != null) {
            if (note.getSelectedEpisode().getSubtitles().isEmpty()) {
                return;
            }
        }

        String warning = "WARNING: Removing Subtitles will remove all association with its SceneNotes. Do you wish to delete the Subtitles?";
        String[] b = {"Yes", "No"};
        int r = JOptionPane.showOptionDialog(NoteFrame.this, warning, "WARNING!", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, b, b[1]);
        if (r == 0) {

            note.removeSubtitles();
            saveNotes();
            updateSubtitles(null);

        }
    }

    /* -------------------------------------------------------------------
     * 	Selecting Tags
     *  ------------------------------------------------------------------*/

    public void deselectAllTags() {
        note.deselectAllTags();
        disableSearchMode();
    }

    public void notAllUnselectedTags() {
        note.notAllUnselectedTags();
        updateTags();
        enableSearchMode();
        searchByTag();


    }

    /* -------------------------------------------------------------------
     * 	Helper Function Misc
     *  ------------------------------------------------------------------*/

    private boolean justSpacesInString(String text) {
        if (text.equals("")) { return true; }

        for (char c: text.toCharArray()) {
            if (c != ' ') {
                return false;
            }
        }

        return true;
    }
    private void saveNotes() {
        for (JTextArea a: activeNotesList) {
            SceneNote s = (SceneNote) a.getClientProperty("getScene");
            s.setNote(a.getText());
        }
        activeNotesList.clear();
    }





    /* -------------------------------------------------------------------
     * 	Button Fetchers
     *  ------------------------------------------------------------------*/


    public JButton getAddSeasonButton() {return addSeasonButton;}
    public JButton getRemoveSeasonButton() {return removeSeasonButton;}
    public JButton getAddEpisodeButton() {return addEpisodeButton;}
    public JButton getRemoveEpisodeButton() {return removeEpisodeButton;}
    public JButton getAddSceneNoteButton() {return addSceneNoteButton;}
    public JButton getAddSubtitlesButton() {return addSubtitlesButton;}
    public JButton getRemoveSubtitlesButton() {return removeSubtitlesButton;}
    public JButton getSearchButton() {return searchButton;}

    public JButton getDeselectAllTagsButton() {return deselectAllTagsButton;}
    public JButton getNotAllUnselectedTagsButton() {return  notAllUnselectedTagsButton;}


}
