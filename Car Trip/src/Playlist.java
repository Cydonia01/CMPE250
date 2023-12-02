import java.util.ArrayList;

public class Playlist {
    private final String[] categories = {"heartache", "roadtrip", "blissful"};
    private ArrayList<Heap> minHeaps;
    private ArrayList<Heap> maxHeaps;
    private int id;
    private int size;

    public Playlist(int id, int size) {
        this.id = id;
        this.size = size;
        this.minHeaps = new ArrayList<Heap>();
        this.maxHeaps = new ArrayList<Heap>();
        createHeaps();
    }

    private void createHeaps() {
        for (String category: categories) {
            Heap heap = new Heap(true, category);
            maxHeaps.add(heap);
        }

        for (String category: categories) {
            Heap heap = new Heap(false, category);
            minHeaps.add(heap);
        }
    }

    public HashMap<Integer, Song> getSongs() {
        return songs;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
