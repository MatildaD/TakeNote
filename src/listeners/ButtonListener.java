package listeners;


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
    }
    }




    /**
    public void actionPerformed(ActionEvent e) {

        if (e.getSource().equals(noteFrame.getAddAccountButton())) {
            noteFrame.addAccount();
        } else if (e.getSource().equals(noteFrame.getRemoveAccountButton())) {
            noteFrame.removeAccount();

        } else if (e.getSource().equals(noteFrame.getBudgetButton())) {
            noteFrame.changeMainTo(noteFrame.getBudgetPanel());

        } else if (e.getSource().equals(noteFrame.getTransactionsButton())) {
            noteFrame.changeMainTo(noteFrame.getTransactionPanel());

        } else if (e.getSource().equals(noteFrame.getReportButton())) {
            noteFrame.changeMainTo(noteFrame.getReportPanel());

        } else if (e.getSource().equals(noteFrame.getEditAccountButton())) {
            noteFrame.editAccount();

        } else if (e.getSource().equals(noteFrame.getAddMainCategoryButton())) {
            noteFrame.addMainCategory();

        } else if (e.getSource().equals(noteFrame.getRemoveMainCategoryButton())) {
            noteFrame.removeMainCategory();

        } else if (e.getSource().equals(noteFrame.getEditMainCategoryButton())) {
            noteFrame.editMainCategory();

        } else if (e.getSource().equals(noteFrame.getAddSubCategoryButton())) {
            noteFrame.addSubCategory();

        } else if (e.getSource().equals(noteFrame.getRemoveSubCategoryButton())) {
            noteFrame.removeSubCategory();

        } else if (e.getSource().equals(noteFrame.getEditSubCategoryButton())) {
            noteFrame.editSubCategory();

        } else if (e.getSource().equals(noteFrame.getAddTransactionButton())) {
            noteFrame.addTransaction();

        } else if (e.getSource().equals(noteFrame.getRemoveTransactionButton())) {
            noteFrame.removeTransaction();
        }
    }

     **/
}
