package takenote;

import components.Episode;
import components.Season;

import javax.swing.*;

public class TestNote extends JFrame{


    public static void main(String[] args) {
        Note note = new Note("TestNote");
        NoteFrame noteFrame = new NoteFrame(note);

        test(noteFrame);


    }

    public static void test(NoteFrame noteFrame){
        noteFrame.getNote().addSeason(new Season("Series 1"));
        noteFrame.getNote().addEpisode(new Episode("Episode 1", noteFrame.getNote().getSeasons().get(0)), noteFrame.getNote().getSeasons().get(0));
        noteFrame.getNote().addEpisode(new Episode("Episode 2", noteFrame.getNote().getSeasons().get(0)), noteFrame.getNote().getSeasons().get(0));
        noteFrame.getNote().addEpisode(new Episode("Episode 3", noteFrame.getNote().getSeasons().get(0)), noteFrame.getNote().getSeasons().get(0));
        noteFrame.updateSeasons();

    }


}
