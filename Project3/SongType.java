import java.util.ArrayList;

public class SongType {
    public boolean[] isDeleted; // this array stores whether a song is deleted from its playlist or not
    public FirstN[] allLists; // this is the array containing all the playlists
    public FirstN globalFirstN; // this object stores all candidates for the epic blend
    private final boolean[] isDeletedFromFirstN; // this array will be used to update the epic blend
    private int totalAddEvents;
    int specialLimitForType;

    SongType(int numberOfPlaylists, int specialLimitForType) {
        this.specialLimitForType = specialLimitForType;
        isDeleted = new boolean[Project3.totalSongCount+1];
        isDeletedFromFirstN = new boolean[Project3.totalSongCount+1];
        allLists = new FirstN[numberOfPlaylists+1];
        globalFirstN = new FirstN(specialLimitForType, isDeletedFromFirstN);
        totalAddEvents = 0;
    }

    public int[] add(Song song, FirstN firstN){
        ChangeEvent changeEvent = firstN.add(song);
        Song added = changeEvent.addedToFirstN;
        Song deleted = changeEvent.deletedFromFirstN;
        return updateGlobalFirstN(added, deleted);
    }
    public int[] remove(Song song, FirstN firstN){
        ChangeEvent changeEvent = firstN.remove(song);
        Song added = changeEvent.addedToFirstN;
        Song deleted = changeEvent.deletedFromFirstN;
        return updateGlobalFirstN(added, deleted);
    }

    private int[] updateGlobalFirstN(Song added, Song deleted) {
        int[] pair =  new int[]{0, 0};
        ChangeEvent ce1 = null, ce2 = null;
        if (added != null && added.songID != 0) {
            ce1 = globalFirstN.add(added);
            if (ce1.addedToFirstN != null && ce1.addedToFirstN.songID != 0) totalAddEvents++;
            if (ce1.deletedFromFirstN != null && ce1.deletedFromFirstN.songID != 0) totalAddEvents--;
        }
        if (deleted != null && deleted.songID != 0) {
            ce2 = globalFirstN.remove(deleted);
            if (ce2.addedToFirstN != null && ce2.addedToFirstN.songID != 0) totalAddEvents++;
            if (ce2.deletedFromFirstN != null && ce2.deletedFromFirstN.songID != 0) totalAddEvents--;
        }
        if (ce1 != null && ce1.addedToFirstN != null && ce2 != null && ce2.deletedFromFirstN != null && ce1.addedToFirstN.songID == ce2.deletedFromFirstN.songID) {
            pair[0] = (ce2.addedToFirstN == null) ? 0 : ce2.addedToFirstN.songID;
            pair[1] = (ce1.deletedFromFirstN == null) ? 0 : ce1.deletedFromFirstN.songID;
        } else if (ce2 != null && ce2.addedToFirstN != null && ce1 != null && ce1.deletedFromFirstN != null && ce2.addedToFirstN.songID == ce1.deletedFromFirstN.songID) {
            pair[0] = (ce1.addedToFirstN == null) ? 0 : ce1.addedToFirstN.songID;
            pair[1] = (ce2.deletedFromFirstN == null) ? 0 : ce2.deletedFromFirstN.songID;
        } else {
            if (ce1 != null && ce1.addedToFirstN != null) pair[0] = ce1.addedToFirstN.songID;
            if (ce2 != null && ce2.addedToFirstN != null) pair[0] = ce2.addedToFirstN.songID;

            if (ce1 != null && ce1.deletedFromFirstN != null) pair[1] = ce1.deletedFromFirstN.songID;
            if (ce2 != null && ce2.deletedFromFirstN != null) pair[1] = ce2.deletedFromFirstN.songID;
        }
        return pair;
    }

    public void ask(EpicBlendHeap heap, int mode){

        if (mode == 1) {
            MinHeap mh = new MinHeap(globalFirstN.minHeap);
            while (mh.size() != 0) {
                if (isDeletedFromFirstN[mh.peek().songID] || mh.peek().songID == 0) {
                    mh.pop();
                    continue;
                }
                heap.insert(mh.pop());
            }
        } else {
            // test
            MaxHeap topla = new MaxHeap();
            topla.pop();
            for (FirstN firstn : allLists) {
                if (firstn == null) continue;
                MinHeap tmp = new MinHeap(firstn.minHeap);
                while (tmp.size() != 0) {
                    if (isDeleted[tmp.peek().songID] || tmp.peek().songID == 0) {
                        tmp.pop();
                        continue;
                    }
                    topla.insert(tmp.pop());
                }
            }
            for (int i = 0; i < specialLimitForType; i++) {
                heap.insert(topla.pop());
                if (topla.size() == 0) break;
            }
        }
    }
}
