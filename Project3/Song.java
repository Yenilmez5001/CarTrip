public class Song implements Comparable<Song> {
    public int songID;
    public int playlistID;
    public String name;
    public int playCount;
    public int score;

    public Song(int ID, String name, int playCount, int score) {
        this.songID = ID;
        this.name = name;
        this.playCount = playCount;
        this.score = score;
    }

    public Song(int val) {
        this(val, "test", 0, val);
    }

    // fixed compare method


    @Override
    public int compareTo(Song song){
        if (this.score > song.score){
            return 1;
        }
        else if (this.score < song.score){
            return -1;
        }
        // else, compare their names
        else{
            return -this.name.compareTo(song.name);    /// note that "a".compareTo("z") returns -25
        }
    }

    public String toString() {
        if (songID == 0) return "dummy";
        return "(" + songID + ": " + score + ")";
    }

}


