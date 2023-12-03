import java.util.ArrayList;

public class Playlist {
    private final String[] categories = {"heartache", "roadtrip", "blissful"};
    //private ArrayList<Heap> minHeaps;
    private ArrayList<Heap> maxHeaps;
    private int heartacheCount;
    private int roadtripCount;
    private int blissfulCount;
    private int id;
    private int size;

    public Playlist(int id, int size, Song[] songs) {
        this.id = id;
        this.size = size;
        this.heartacheCount = 0;
        this.roadtripCount = 0;
        this.blissfulCount = 0;
        //this.minHeaps = new ArrayList<Heap>();
        this.maxHeaps = new ArrayList<Heap>();
        createHeaps(songs);
    }

    private void createHeaps(Song[] songs) {
        for (String category: categories) {
            Heap heap = new Heap(true, category, songs);
            maxHeaps.add(heap);
        }

        /*for (String category: categories) {
            Heap heap = new Heap(false, category, songs);
            minHeaps.add(heap);
        }*/
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

    

    public ArrayList<Heap> getMaxHeaps() {
        return maxHeaps;
    }
    
    /*public ArrayList<Heap> getMinHeaps() {
        return minHeaps;
    }*/

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
