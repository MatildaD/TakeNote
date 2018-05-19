package listeners;


import components.SubtitleBit;
import takenote.NoteFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonListener implements ActionListener {

    private NoteFrame noteFrame;


    public ButtonListener(NoteFrame noteFrame){
        this.noteFrame = noteFrame;
    }


    /**
     * Checks which button was pressed and calls a method based on which was pressed
     */

    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(noteFrame.getAddSeasonButton())) {
            noteFrame.addSeason();
        } else if (e.getSource().equals(noteFrame.getRemoveSeasonButton())) {
            noteFrame.removeSeason();
        } else if (e.getSource().equals(noteFrame.getAddEpisodeButton())) {
            noteFrame.addEpisode();
        } else if (e.getSource().equals(noteFrame.getRemoveEpisodeButton())) {
            noteFrame.removeEpisode();
        }  else if (e.getSource().equals(noteFrame.getAddSubtitlesButton())) {
            noteFrame.openSubtitles();
        }  else if (e.getSource().equals(noteFrame.getRemoveSubtitlesButton())) {
            noteFrame.removeSubtitles();
        }  else if (e.getSource().equals(noteFrame.getAddSceneNoteButton())) {
            if (noteFrame.getNote().getSelectedSub() != null) {
                noteFrame.newSceneNote(noteFrame.getNote().getSelectedSub());
            } else {
                noteFrame.newSceneNote(new SubtitleBit("00:00:00", "00:00:00", "", "", "", 0, noteFrame.getNote().getSelectedEpisode(), 0));
            }
        } else if (e.getSource().equals(noteFrame.getSearchButton())) {
            noteFrame.search();
    }
    }


}
