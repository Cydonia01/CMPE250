import java.util.ArrayList;
import java.util.HashSet;

public class Heap {
    private ArrayList<Song> songs;
    private HashSet<Integer> removedSongs;
    private int size;
    private boolean type;
    private String category;
    
    public Heap(boolean type, String category) {
        this.songs = new ArrayList<Song>();
        songs.add(null);
        this.size = 0;
        this.type = type;
        this.category = category;
        this.removedSongs = new HashSet<Integer>();
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
        
        while (hole > 1 && (type ? song.compareTo(songs.get(hole/2), category) > 0:song.compareTo(songs.get(hole/2), category) < 0)) {
            Song parent = songs.get(hole/2);
            songs.set(hole / 2, song);
            songs.set(hole, parent);
            hole = hole / 2;
        }
    }
    
    public Song peek() {
        if (size == 0) return null;
        Song root = songs.get(1);
        while (removedSongs.contains(root.getId())) {
            songs.set(1, songs.get(size));
            songs.remove(size);
            size--;
            removedSongs.remove(root.getId());
            if (size > 1)
                percolateDown(1);
            if (size == 0)
                return null;
            root = songs.get(1);
        }
        return songs.get(1);
    }

    public Song pop() {
        if (size == 0) return null;
        Song root = songs.get(1);
        while (removedSongs.contains(root.getId())) {
            songs.set(1, songs.get(size));
            songs.remove(size);
            size--;
            removedSongs.remove(root.getId());
            if (size > 1)
                percolateDown(1);
            if (size == 0)
                return null;
            root = songs.get(1);
        }
        songs.set(1, songs.get(size));
        songs.remove(size);
        size--;
        if (size > 1)
            percolateDown(1);

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

    public void remove(int songId) {
        removedSongs.add(songId);
    }

    public void percolateDown(int hole) {
        int child;
        Song temp = songs.get(hole);
        
        while (hole * 2 <= size) {
            child = hole * 2;

            if (child != size && (type ? songs.get(child + 1).compareTo(songs.get(child), category) > 0:songs.get(child + 1).compareTo(songs.get(child), category) < 0)) {
                child++;
            }
            if (type ? songs.get(child).compareTo(temp, category) > 0:songs.get(child).compareTo(temp, category) < 0) {
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