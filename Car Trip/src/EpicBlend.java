import java.util.ArrayList;
import java.util.HashMap;
public class EpicBlend {
    private final String[] categories = {"heartache", "roadtrip", "blissful"};
    private HashMap<Integer, Playlist> playlists;
    private ArrayList<Heap> epicMinHeaps;
    private ArrayList<Heap> epicMaxHeaps;
    private int heartacheCount, roadtripCount, blissfulCount;
    private int playlistLimit;
    private int heartacheLimit;
    private int roadtripLimit;
    private int blissfulLimit;
    private boolean added;
    
    public EpicBlend(int playlistLimit, int heartacheLimit, int roadtripLimit, int blissfulLimit) {
        this.playlists = new HashMap<Integer, Playlist>();
        this.epicMinHeaps = new ArrayList<Heap>();
        this.epicMaxHeaps = new ArrayList<Heap>();
        this.playlistLimit = playlistLimit;
        this.heartacheLimit = heartacheLimit;
        this.roadtripLimit = roadtripLimit;
        this.blissfulLimit = blissfulLimit;
        this.heartacheCount = 0;
        this.roadtripCount = 0;
        this.blissfulCount = 0;

        for (String category: categories) {
            Heap heap = new Heap(false, category);
            epicMinHeaps.add(heap);
        }
    }

    public void createEpicMaxHeap() {
        for (String category: categories) {
            Heap heap = new Heap(true, category);
            for (Playlist playlist: playlists.values()) {
                heap.add(playlist.getMaxHeap(category).peek());
            }
            epicMaxHeaps.add(heap);
        }
    }

    public void createEpicBlend() {
        for (String category: categories) {
            while (getCategoryCount(category) < getCategoryLimit(category)) {
                int index = getCategoryIndex(category);
                Song song = epicMaxHeaps.get(index).peek();
                Playlist playlist = playlists.get(song.getPlaylistId());
                if (playlist.getPlaylistCategoryCount(category) < playlistLimit) {
                    epicMinHeaps.get(index).add(song);
                    playlist.getMinHeap(category).add(song);
                    playlist.getMaxHeap(category).pop();
                    playlist.incrementPlaylistCategoryCount(category);
                    incrementCategoryCount(category);
                    if (playlist.getMaxHeap(category).size() > 0) {
                        epicMaxHeaps.get(index).add(playlist.getMaxHeap(category).peek());
                    }
                }
                else {
                    epicMaxHeaps.get(index).pop();
                }
            }
        }
        printEpicBlend();
        //printBlend();
    }

    public void add(int playlistId, Song song) {
        Playlist playlist = playlists.get(playlistId);
        song.setPlaylistId(playlistId);
        String log = "";
        for (String category: categories) {
            added = false;
            Heap maxHeap = playlist.getMaxHeap(category);
            Heap minHeap = playlist.getMinHeap(category);
            maxHeap.add(song);
            if (minHeap.size() == 0) {
                minHeap.add(song);
                epicMinHeaps.get((getCategoryIndex(category))).add(song);
                added = true;
                incrementCategoryCount(category);
                playlist.incrementPlaylistCategoryCount(category);
            }

            if (playlist.getPlaylistCategoryCount(category) < playlistLimit) {
                if (getCategoryCount(category) < getCategoryLimit(category)) {
                    minHeap.add(song);
                    epicMinHeaps.get((getCategoryIndex(category))).add(song);
                    added = true;
                    incrementCategoryCount(category);
                    playlist.incrementPlaylistCategoryCount(category);
                }
                else if (song.getCategoryScore(category) > minHeap.peek().getCategoryScore(category)) {
                    minHeap.add(song);
                    epicMinHeaps.get((getCategoryIndex(category))).pop();
                    epicMinHeaps.get((getCategoryIndex(category))).add(song);
                    added = true;
                    incrementCategoryCount(category);
                    playlist.incrementPlaylistCategoryCount(category);
                }
            }
            else if (song.getCategoryScore(category) > minHeap.peek().getCategoryScore(category)) {
                minHeap.pop();
                minHeap.add(song);
                epicMinHeaps.get((getCategoryIndex(category))).add(song);
                added = true;
                incrementCategoryCount(category);
                playlist.incrementPlaylistCategoryCount(category);
            }
            if (added) {
                maxHeap.remove(song.getId());
                log += String.format("%d ", song.getId());
            } else {
                log += "0 ";
            }
        }
        //System.out.println(log);
    }

    public void remove(int songId, int playlistId) {}

    public void printBlend() {
        for (String category: categories) {
            System.out.println(category + ":");
            for (Playlist playlist: playlists.values()) {
                for (Song song: playlist.getMinHeap(category).getSongs()) {
                    if (song != null) {
                        System.out.print(song.getId() + " " + song.getName());
                    }
                }
                System.out.println();
            }
        }
    }

    public void printEpicBlend() {
        for (String category: categories) {
            System.out.println(category + ":");
            for (Song song: epicMinHeaps.get(getCategoryIndex(category)).getSongs()) {
                if (song != null) {
                    System.out.print(song.getId() + " " + song.getName() + " ");
                }
            }
            System.out.println();
        }
    }

    public void addPlaylist(int playlistId, int playlistSize, Song[] items) {
        playlists.put(playlistId, new Playlist(playlistId, playlistSize, items));
    }

    public int getCategoryIndex(String category) {
        switch (category) {
            case "heartache":
                return 0;
            case "roadtrip":
                return 1;
            case "blissful":
                return 2;
        }
        return -1;
    }

    private void incrementCategoryCount(String category) {
        switch (category) {
            case "heartache":
                heartacheCount++;
            case "roadtrip":
                roadtripCount++;
            case "blissful":
                blissfulCount++;
        }
    }

    private int getCategoryLimit(String category) {
        switch (category) {
            case "heartache":
                return heartacheLimit;
            case "roadtrip":
                return roadtripLimit;
            case "blissful":
                return blissfulLimit;
            default:
                return 0;
        }
    }

    private int getCategoryCount(String category) {
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
}
