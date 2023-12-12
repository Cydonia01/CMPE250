import java.util.ArrayList;
import java.util.HashMap;

public class Heap {
    private ArrayList<Song> songs;
    private HashMap<Integer, Integer> songsSet;
    private int size;
    private boolean type;
    private String category;
    
    public Heap(boolean type, String category) {
        this.songs = new ArrayList<Song>();
        songs.add(null);
        this.size = 0;
        this.type = type;
        this.category = category;
        this.songsSet = new HashMap<Integer, Integer>();
    }

    public Heap(boolean type, String category, Song[] items) {
        this(type, category);
        for (Song item: items) {
            size++;
            songs.add(item);
            songsSet.put(item.getId(), size);
        }
        buildHeap();
    }
    
    public ArrayList<Song> getSongs() {
        return songs;
    }

    public void add(Song song) {
        if (song == null) return;
        if (songsSet.containsKey(song.getId())) return;
        size++;
        songs.add(song);
        songsSet.put(song.getId(), size);
        int hole = size;
        
        while (hole > 1 && (type ? song.compareTo(songs.get(hole/2), category) > 0:song.compareTo(songs.get(hole/2), category) < 0)) {
            Song parent = songs.get(hole/2);
            songs.set(hole / 2, song);
            songs.set(hole, parent);
            songsSet.put(parent.getId(), hole);
            songsSet.put(song.getId(), hole / 2);
            hole = hole / 2;
        }
    }
    
    public Song peek() {
        if (size == 0) return null;
        return songs.get(1);
    }

    public Song pop() {
        if (size == 0) return null;

        Song root = songs.get(1);
        songs.set(1, songs.get(size));
        songsSet.put(songs.get(1).getId(), 1);
        songs.remove(size);
        songsSet.remove(root.getId());
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
        if (songsSet.containsKey(songId)) return;
        
        int index = songsSet.get(songId);
        if (index == size) {
            songs.remove(size);
            size--;
            songsSet.remove(songId);
            return;
        }
        Song last = songs.get(size);
        songs.set(index, last);
        songsSet.put(last.getId(), index);
        songs.remove(size);
        size--;
        songsSet.remove(songId);
        
        if (size > 1) {
            percolateDown(index);
            percolateUp(index);
        }
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
    private void percolateUp(int hole) {
        Song temp = songs.get(hole);
        while (hole > 1 && (type ? temp.compareTo(songs.get(hole / 2), category) > 0 : temp.compareTo(songs.get(hole / 2), category) < 0)) {
            Song parent = songs.get(hole / 2);
            songs.set(hole / 2, temp);
            songs.set(hole, parent);
            songsSet.put(parent.getId(), hole);
            songsSet.put(temp.getId(), hole / 2);
            hole = hole / 2;
        }
    }
}