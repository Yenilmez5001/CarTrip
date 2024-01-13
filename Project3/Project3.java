/**
 * Osman Selim Yuksel
 */

///////// OBJECT ORIENTED PROGRAMMING /////////
///////// OBJECT ORIENTED PROGRAMMING /////////
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Project3 {
    public static int totalSongCount;
    public static Song[] allHeartacheSongs;
    public static Song[] allRoadtripSongs;
    public static Song[] allBlissfulSongs;

    static String songFile = "test/songs.txt";
    static String inputFile = "test/inputs/tiny_playlists_small.txt";
    static String outputFile = "myoutput.txt";

    static FileWriter fileWriter;

    public static void main(String[] args) throws IOException {
        songFile = args[0];
        inputFile = args[1];
        outputFile = args[2];

        fileWriter = new FileWriter(outputFile);

        readSong();
        readInput();

        fileWriter.close();
    }


    public static void readSong() throws IOException {

        // for each type of song, create a FirstN class
        // there will be 3 instances of FirstN class, heartache, roadtrip, blissful

        Scanner sc = new Scanner(new File(songFile));
        String firstLine = sc.nextLine();
        totalSongCount = Integer.parseInt(firstLine.split(" ")[0]);
        allHeartacheSongs = new Song[totalSongCount+1];
        allRoadtripSongs = new Song[totalSongCount+1];
        allBlissfulSongs = new Song[totalSongCount+1];

        for (int i = 0; i<totalSongCount; i++){
            String line = sc.nextLine();
            String[] songInfo = line.split(" ");
            // introduce all attributes of a song
            int songID = Integer.parseInt(songInfo[0]);
            String songName = songInfo[1];
            int playCount = Integer.parseInt(songInfo[2]);

            int hScore = Integer.parseInt(songInfo[3]);
            Song song1 = new Song(songID, songName, playCount, hScore);

            int rScore = Integer.parseInt(songInfo[4]);
            Song song2 = new Song(songID, songName, playCount, rScore);

            int bScore = Integer.parseInt(songInfo[5]);
            Song song3 = new Song(songID, songName, playCount, bScore);
            // notice that ID's are given in order, so we can use them as index

            allHeartacheSongs[songID] = song1;
            allRoadtripSongs[songID] = song2;   // ID's start from 1, but array indices start from 0
            allBlissfulSongs[songID] = song3;

        }
    }

    public static void readInput() throws IOException {
        Scanner sc = new Scanner(new File(inputFile));
        String firstLine = sc.nextLine();
        String[] firstLineInfo = firstLine.split(" ");
        int playlistLimit = Integer.parseInt(firstLineInfo[0]); // each playlist can conttribute at most this many songs to Epic Blend for each category
        int heartacheLimit = Integer.parseInt(firstLineInfo[1]); // EpicBlend can include up to this many songs from the heartache category
        int roadtripLimit = Integer.parseInt(firstLineInfo[2]); // EpicBlend can include up to this many songs from the roadtrip category
        int blissfulLimit = Integer.parseInt(firstLineInfo[3]);  // EpicBlend can include up to this many songs from the blissful category

        String secondLine = sc.nextLine();
        int playlistCount = Integer.parseInt(secondLine.split(" ")[0]);   // number of playlists

        SongType heartache = new SongType(playlistCount, heartacheLimit);
        SongType roadtrip = new SongType(playlistCount, roadtripLimit);
        SongType blissful = new SongType(playlistCount, blissfulLimit);

        for (int i=0; i<playlistCount; i++){

            String line = sc.nextLine();
            String[] playlistInfo = line.split(" ");
            int playlistID = Integer.parseInt(playlistInfo[0]);
            int songCount = Integer.parseInt(playlistInfo[1]);

            FirstN heartacheFirstN = new FirstN(playlistLimit, heartache.isDeleted);
            heartache.allLists[playlistID] = heartacheFirstN;

            FirstN roadtripFirstN = new FirstN(playlistLimit, roadtrip.isDeleted);
            roadtrip.allLists[playlistID] = roadtripFirstN;

            FirstN blissfulFirstN = new FirstN(playlistLimit, blissful.isDeleted);
            blissful.allLists[playlistID] = blissfulFirstN;

            String nextLine = sc.nextLine();
            String[] songIDsArray = nextLine.split(" ");

            for (int j=0; j<songCount; j++){
                int songID = Integer.parseInt(songIDsArray[j]);

                Song song1 = allHeartacheSongs[songID];
                song1.playlistID = playlistID;
                heartache.add(song1, heartacheFirstN);

                Song song2 = allRoadtripSongs[songID];
                song2.playlistID = playlistID;
                roadtrip.add(song2, roadtripFirstN);

                Song song3 = allBlissfulSongs[songID];
                song3.playlistID = playlistID;
                blissful.add(song3, blissfulFirstN);
            }
        }

        int eventCount = Integer.parseInt(sc.nextLine().split(" ")[0]);

        System.out.println(blissful.allLists[2]);

        for (int j=0; j<eventCount; j++){
            String eventLine = sc.nextLine();
            String[] eventInfo = eventLine.split(" ");
            String eventType = eventInfo[0];
            if (eventType.startsWith("R")){  // REMOVE event
                int songID = Integer.parseInt(eventInfo[1]);
                int listID = Integer.parseInt(eventInfo[2]);

                if (listID == 2) {
                    System.out.println("REMOVE: " + allBlissfulSongs[songID]);
                    System.out.println(blissful.allLists[listID]);
                }

                // REMOVE operation
                int[] hPair = heartache.remove(allHeartacheSongs[songID], heartache.allLists[listID]);
                int[] rPair = roadtrip.remove(allRoadtripSongs[songID], roadtrip.allLists[listID]);
                int[] bPair = blissful.remove(allBlissfulSongs[songID], blissful.allLists[listID]);

                fileWriter.write(hPair[0] + " " + rPair[0] + " " + bPair[0] + "\n");  // print the added songs to EpicBlend
                fileWriter.write(hPair[1] + " " + rPair[1] + " " + bPair[1] + "\n");  // print the deleted songs from EpicBlend

                if (listID == 2) {
                    System.out.print(hPair[0] + " " + rPair[0] + " " + bPair[0] + "\n");  // print the added songs to EpicBlend
                    System.out.print(hPair[1] + " " + rPair[1] + " " + bPair[1] + "\n");  // print the deleted songs from EpicBlend
                    System.out.println(blissful.allLists[listID]);
                }

            } else if (eventType.startsWith("AD")){ // ADD event
                int songID = Integer.parseInt(eventInfo[1]);
                int listID = Integer.parseInt(eventInfo[2]);

                if (listID == 2) {
                    System.out.println("ADD: " + allBlissfulSongs[songID]);
                    System.out.println(blissful.allLists[listID]);
                }

                int[] hPair = heartache.add(allHeartacheSongs[songID], heartache.allLists[listID]);
                int[] rPair = roadtrip.add(allRoadtripSongs[songID], roadtrip.allLists[listID]);
                int[] bPair = blissful.add(allBlissfulSongs[songID], blissful.allLists[listID]);

                fileWriter.write(hPair[0] + " " + rPair[0] + " " + bPair[0] + "\n");  // print the added songs to EpicBlend
                fileWriter.write(hPair[1] + " " + rPair[1] + " " + bPair[1] + "\n");  // print the deleted songs from EpicBlend

                if (listID == 2) {
                    System.out.print(hPair[0] + " " + rPair[0] + " " + bPair[0] + "\n");  // print the added songs to EpicBlend
                    System.out.print(hPair[1] + " " + rPair[1] + " " + bPair[1] + "\n");  // print the deleted songs from EpicBlend
                    System.out.println(blissful.allLists[listID]);
                }

            } else {  // ASK event
                EpicBlendHeap heap = new EpicBlendHeap();
                heartache.ask(heap, 1);
                roadtrip.ask(heap, 1);
                blissful.ask(heap, 1);
                int lastID = -1;
                while (heap.size() != 0) {
                    if (heap.peek().songID != lastID) {
                        lastID = heap.peek().songID;
                        fileWriter.write(heap.pop().songID + " ");
                    } else heap.pop();
                }
                fileWriter.write("\n");
            }
        }

    }
}