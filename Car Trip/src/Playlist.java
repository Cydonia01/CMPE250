import java.util.ArrayList;

public class Playlist {
    private final String[] categories = {"heartache", "roadtrip", "blissful"};
    private ArrayList<Heap> minHeaps;
    private ArrayList<Heap> maxHeaps;
    private int heartacheCount, roadtripCount, blissfulCount;
    private int id;
    private int size;

    public Playlist(int id, int size, Song[] songs) {
        this.minHeaps = new ArrayList<Heap>();
        this.maxHeaps = new ArrayList<Heap>();
        this.heartacheCount = 0;
        this.roadtripCount = 0;
        this.blissfulCount = 0;
        this.id = id;
        this.size = size;
        createHeaps(songs);
    }

    public void createHeaps(Song[] songs) {
        for (String category: categories) {
            Heap heap = new Heap(true, category, songs);
            maxHeaps.add(heap);
            for (Song song: songs) {
                song.setPlaylistId(id);
            }
        }
        for (String category: categories) {
            Heap heap = new Heap(false, category);
            minHeaps.add(heap);
        }
    }

    public int getPlaylistCategoryCount(String category) {
        switch (category) {
            case "heartache":
                return heartacheCount;
            case "roadtrip":
                return roadtripCount;
            case "blissful":
                return blissfulCount;
            default:
                return 0;
        }
    }

    public void incrementPlaylistCategoryCount(String category) {
        switch (category) {
            case "heartache":
                heartacheCount++;
                break;
            case "roadtrip":
                roadtripCount++;
                break;
            case "blissful":
                blissfulCount++;
                break;
        }
    }

    public void decrementPlaylistCategoryCount(String category) {
        switch (category) {
            case "heartache":
                heartacheCount--;
                break;
            case "roadtrip":
                roadtripCount--;
                break;
            case "blissful":
                blissfulCount--;
                break;
        }
    }

    public Heap getMaxHeap(String category) {
        switch (category) {
            case "heartache":
                return maxHeaps.get(0);
            case "roadtrip":
                return maxHeaps.get(1);
            case "blissful":
                return maxHeaps.get(2);
        }
        return null;
    }
    
    public Heap getMinHeap(String category) {
        switch (category) {
            case "heartache":
                return minHeaps.get(0);
            case "roadtrip":
                return minHeaps.get(1);
            case "blissful":
                return minHeaps.get(2);
        }
        return null;
    }

    public void addMinHeap(Heap heap) {
        minHeaps.add(heap);
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
