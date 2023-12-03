import java.util.ArrayList;

public class Heap {
    private ArrayList<Song> songs;
    private int size;
    private boolean maxHeap;
    private String category;
    
    public Heap(boolean type, String category) {
        this.songs = new ArrayList<Song>();
        songs.add(null);
        this.size = 0;
        this.maxHeap = type ? true:false;
        this.category = category;
    }

    public Heap(boolean type, String category, Song[] items) {
        this(type, category);
        for (Song item: items) {
            songs.add(item);
            size++;
        }
        buildHeap();
    }
    
    public ArrayList<Song> getSongs() {
        return songs;
    }

    public void add(Song song) {
        size++;
        songs.add(song);
        int hole = size;
        
        while (hole > 1 && (maxHeap ? song.getCategoryScore(category) < songs.get(hole).getCategoryScore(category):song.getCategoryScore(category) > songs.get(hole).getCategoryScore(category))) {
            Song parent = songs.get(hole/2);
            songs.set(hole / 2, song);
            songs.set(hole, parent);
            hole = hole / 2;
        }
    }
    
    public Song peek() {
        return songs.get(1);
    }

    public Song pop() {
        Song root = peek();
        songs.set(1, songs.get(size));
        songs.remove(size);
        size--;
        if (size > 1) {
            percolateDown(1);
        }

        return root;
    }

    public void buildHeap() {
        for (int i = size/2; i > 0; i--) {
            percolateDown(i);
        }
    }

    public int size() {
        return size;
    }

    public void percolateDown(int hole) {
        int child;
        Song temp = songs.get(hole);
        
        while (hole * 2 <= size) {
            child = hole * 2;

            if (child != size && (maxHeap ? songs.get(child+1).getCategoryScore(category) < songs.get(child).getCategoryScore(category):songs.get(child+1).getCategoryScore(category) > songs.get(child).getCategoryScore(category))) {
                child++;
            }
            if (maxHeap ? songs.get(child).getCategoryScore(category) < temp.getCategoryScore(category):songs.get(child).getCategoryScore(category) > temp.getCategoryScore(category)) {
                songs.set(hole, songs.get(child));
            }
            else {
                break;
            }
            hole = child;
        }
        songs.set(hole, temp);
    }
}


