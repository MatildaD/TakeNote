package takenote;

import components.Episode;
import components.Season;
import components.Tag;
import sun.swing.SwingAccessor;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class TestNote extends JFrame{


    public static void main(String[] args) {
        Note note = new Note("TestNote");
        NoteFrame noteFrame = new NoteFrame(note);

        test(noteFrame);


    }

    private static void test(NoteFrame noteFrame){
        noteFrame.getNote().addSeason(new Season("Series 1"));
        noteFrame.getNote().addEpisode(new Episode("Episode 1", noteFrame.getNote().getSeasons().get(0)), noteFrame.getNote().getSeasons().get(0));
        noteFrame.getNote().addEpisode(new Episode("Episode 2", noteFrame.getNote().getSeasons().get(0)), noteFrame.getNote().getSeasons().get(0));
        noteFrame.getNote().addEpisode(new Episode("Episode 3", noteFrame.getNote().getSeasons().get(0)), noteFrame.getNote().getSeasons().get(0));
        noteFrame.getNote().setSelectedEpisode(noteFrame.getNote().getSeasons().get(0).getEpisodeList().get(0));
        //noteFrame.getNote().getSeasons().get(0).getEpisodeList().get(0).importSubtitles("D:\\All\\Movies\\Call The Midwife\\Subtitles\\S1E1_new.txt");
        //noteFrame.newSceneNote(noteFrame.getNote().getSelectedEpisode().getSubtitles().get(0));
        noteFrame.updateAll();

    }


}
