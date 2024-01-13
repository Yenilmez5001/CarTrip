import java.util.ArrayList;

public class FirstN {
    MinHeap minHeap;
    MaxHeap maxHeap;
    boolean[] isDeletedArray;

    public FirstN(int capacity, boolean[] isDeletedArray) {
        minHeap = new MinHeap(capacity);
        maxHeap = new MaxHeap();
        this.isDeletedArray = isDeletedArray;
    }

    public ChangeEvent add(Song song) {
        clearHeaps();

        ChangeEvent changeEvent = new ChangeEvent();
        if (song.compareTo(minHeap.peek()) > 0) {

            Song tmp = minHeap.pop();
            changeEvent.deletedFromFirstN = tmp;

//            if (tmp.songID != -1) System.out.println("song " + tmp.songID + " minheap ten silindi1" + "score: " + tmp.score);

            maxHeap.insert(tmp);

            if (isDeletedArray[song.songID]) isDeletedArray[song.songID] = false;
            else minHeap.insert(song);

            changeEvent.addedToFirstN = song;
        } else {
            if (isDeletedArray[song.songID]) isDeletedArray[song.songID] = false;

            else maxHeap.insert(song);
//            System.out.println("song " + song.songID + " maxheap e eklendi" + "score: " + song.score);
        }
        return changeEvent;
    }

    public ChangeEvent remove(Song song) {
        clearHeaps();

        ChangeEvent changeEvent = new ChangeEvent();

        if (song.compareTo(minHeap.peek()) >= 0) {
            Song tmp = maxHeap.pop();
            changeEvent.addedToFirstN = tmp;
            minHeap.insert(tmp);
            changeEvent.deletedFromFirstN = song;
            isDeletedArray[song.songID] = true;
        } else if (song.compareTo(maxHeap.peek()) <= 0) {
            isDeletedArray[song.songID] = true;
        } else {
            System.out.println("BURAYA NASIL GELDI GELMEMESI LAZIM 2");
        }

        clearHeaps();
        return changeEvent;
    }

    private void clearHeaps() {
        while (minHeap.peek() != null && isDeletedArray[minHeap.peek().songID]) {
            isDeletedArray[minHeap.pop().songID] = false;
        }
        while (maxHeap.peek() != null && isDeletedArray[maxHeap.peek().songID]) {
            isDeletedArray[maxHeap.pop().songID] = false;
        }
    }

    public String toString() {
        ArrayList<Integer> dels = new ArrayList<>();
        for (int i = 0; i < isDeletedArray.length; i++) {
            if (isDeletedArray[i]) dels.add(i);
        }
        return "FirstN{\n" +
                "     minHeap=" + minHeap +
                "\n     maxHeap=" + maxHeap +
                "\n     deleted=" + dels +
                "\n}";
    }
}
