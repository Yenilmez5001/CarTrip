import java.util.Arrays;

public class Heap{
    protected Song[] array;
    protected int currentSize;

    public Heap (int capacity) {
        currentSize = capacity;
        array = new Song[capacity + 1];
        // traverse the array and initialize all heap elements' scores to -1
        for (int i=1; i<array.length; i++){  // array[0] remains null
            Song song = new Song(0, "dummy;", -1, Integer.MIN_VALUE);
            array[i] = song;
        }
    }

    public Heap(Heap heap) {
        this.array = new Song[heap.array.length];
        System.arraycopy(heap.array, 0, this.array, 0, this.array.length);
        this.currentSize = heap.currentSize;
    }

    public int compare(Song song1, Song song2){
        return 0;
    }


    protected void enlargeArray(int newSize) {
        Song[] old = array;
        array = new Song[newSize];
        for (int i = 0; i < old.length; i++) {
            array[i] = old[i];
        }
    }
    public void insert(Song song) {
        if (currentSize == array.length - 1) {
            enlargeArray(array.length * 2 + 1);
        }
        int hole = ++currentSize;
        for (array[0] = song; compare(song, array[hole / 2]) < 0; hole /= 2) {
            array[hole] = array[hole / 2];
        }
        array[hole] = song;
    }

    public Song peek() {
        if (array.length != 1){
            return array[1];
        }
        return null;
    }

    // delete method will not be used, since we do not want O(n) time complexity
    // we will only use pop method to remove the top element
    // we will handle the remove operation in a different way
    public Song pop() {
        Song minItem = peek();
        array[1] = array[currentSize--];
        percolateDown(1);
        return minItem;
    }

    private void buildHeap(){
        for(int i = currentSize / 2; i > 0; i--) {
            percolateDown(i);
        }
    }
    private void percolateDown(int hole) {
        int child;
        Song temp = array[hole];

        while(hole * 2 <= currentSize) {
            child = hole * 2;
            if(child != currentSize && compare(array[child + 1],array[child]) < 0) {
                child++;
            }
            if(compare(array[child],temp) < 0) {
                array[hole] = array[child];
            } else {
                break;
            }

            hole = child;
        }
        array[hole] = temp;
    }

    public int size() {
        return currentSize;
    }

    public String toString() {
        Heap tmp;
        if (this instanceof MaxHeap) tmp = new MaxHeap((MaxHeap) this);
        else if (this instanceof MinHeap) tmp = new MinHeap((MinHeap) this);
        else tmp = new EpicBlendHeap(this);

        StringBuilder sb = new StringBuilder();
        while (tmp.size() != 0) sb.append(tmp.pop()).append(' ');
        return sb.toString();
    }
}

class MaxHeap extends Heap {
    public MaxHeap(){
        super(1);
    }

    public MaxHeap(MaxHeap heap) {
        super(heap);
    }
    @Override
    public int compare(Song song1, Song song2){  // this will be used to compare scores of two songs
        return -song1.compareTo(song2);
    }
}
class MinHeap extends Heap {
    public MinHeap(int capacity){ super(capacity); }

    public MinHeap(MinHeap heap) {
        super(heap);
    }
    @Override
    public int compare(Song song1, Song song2){   // this will be used to compare scores of two songs
        return song1.compareTo(song2);
    }
}

class EpicBlendHeap extends Heap {
    public EpicBlendHeap(){ super(0); }

    public EpicBlendHeap(Heap heap) {
        super(heap);
    }
    @Override
    public int compare(Song song1, Song song2){   // this will be used to compare scores of two songs
        if (song1.playCount < song2.playCount){
            return 1;
        }
        else if (song1.playCount > song2.playCount){
            return -1;
        }
        // if there is a tie, lexigraphically smaller names will be prioritized
        else{
            return song1.name.compareTo(song2.name);
        }
    }
}