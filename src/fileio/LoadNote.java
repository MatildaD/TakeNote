package fileio;

import takenote.Note;

import java.io.*;

public class LoadNote {



    public static Note loadNoteFromFile(Note old, String filename) {

        // Tries to open the requested file
        try (FileInputStream fileIn = new FileInputStream(filename)){
            try (ObjectInput in = new ObjectInputStream(fileIn)) {
                Note budget = (Note) in.readObject();
                in.close();
                fileIn.close();
                return budget;
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

        // In case of exception, returns the old budget
        return old;
    }
}