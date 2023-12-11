import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class Project3 {
    public static void main(String[] args) throws Exception {
        EpicBlend epicBlend;
        HashMap<Integer, Song> songs = new HashMap<Integer, Song>();
        try {
            FileWriter writer = new FileWriter("../output.txt");
            File songsFile = new File("songs1.txt");
            File inputFile = new File("general_small.txt");
            Scanner reader = new Scanner(songsFile);
            int numOfSongs = Integer.parseInt(reader.nextLine());
            
            //reading songs.txt file
            for (int i = 0; i < numOfSongs; i++) {
                String[] structure = reader.nextLine().split(" ");
                int id = Integer.parseInt(structure[0]);
                String name = structure[1];
                int playCount = Integer.parseInt(structure[2]);
                int heartacheScore = Integer.parseInt(structure[3]);
                int roadtripScore = Integer.parseInt(structure[4]);
                int blissfulScore = Integer.parseInt(structure[5]);
                Song song = new Song(id, name, playCount, heartacheScore, roadtripScore, blissfulScore);
                songs.put(id, song);
            }   

            reader.close();

            // reading sample input file and creating epic blend
            reader = new Scanner(inputFile);

            String[] firstLine = reader.nextLine().split(" ");
            int playlistLimit = Integer.parseInt(firstLine[0]);
            int heartacheLimit = Integer.parseInt(firstLine[1]);
            int roadtripLimit = Integer.parseInt(firstLine[2]);
            int blissfulLimit = Integer.parseInt(firstLine[3]);
            
            epicBlend = new EpicBlend(playlistLimit,  heartacheLimit,  roadtripLimit, blissfulLimit, writer);

            // reading sample input file and creating playlists
            int numOfPlaylists = Integer.parseInt(reader.nextLine());

            for (int i = 0; i < numOfPlaylists; i++) {
                String[] data = reader.nextLine().split(" ");
                int playlistId = Integer.parseInt(data[0]);
                int playlistSize = Integer.parseInt(data[1]);

                data = reader.nextLine().split(" ");
                Song[] items = new Song[playlistSize];

                for (int j = 0; j < playlistSize; j++) {
                    items[j] = songs.get(Integer.parseInt(data[j]));
                }
                
                epicBlend.addPlaylist(playlistId, playlistSize, items);
            }
            epicBlend.createEpicMaxHeaps();
            epicBlend.createEpicBlend();

            // reading sample input file and performing operations
            int numOfOperations = Integer.parseInt(reader.nextLine());
            for (int i = 0; i < numOfOperations; i++) {
                String[] data = reader.nextLine().split(" ");
                chooseOperation(data, epicBlend, songs);
            }
            reader.close();
            writer.close();
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void chooseOperation(String[] data, EpicBlend epicBlend, HashMap<Integer, Song> songs) throws IOException {
        String operation = data[0];
        if (operation.equals("ASK")) {
            epicBlend.ask();
        }
        else {
            int songId = Integer.parseInt(data[1]);
            int playlistId = Integer.parseInt(data[2]);
            if (operation.equals("ADD")) {
                epicBlend.add(playlistId, songs.get(songId));
            }
            if (operation.equals("REM")) {
                epicBlend.remove(playlistId, songId);
            }
        }
    }
}
