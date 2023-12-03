import java.util.ArrayList;
import java.util.HashMap;
public class EpicBlend {
    private final String[] categories = {"heartache", "roadtrip", "blissful"};
    private HashMap<Integer, Playlist> playlists;
    private ArrayList<Heap> minHeaps;
    private int heartacheCount, roadtripCount, blissfulCount;
    private int playlistLimit;
    private int heartacheLimit;
    private int roadtripLimit;
    private int blissfulLimit;
    
    public EpicBlend(int playlistLimit, int heartacheLimit, int roadtripLimit, int blissfulLimit) {
        this.playlists = new HashMap<Integer, Playlist>();
        this.playlistLimit = playlistLimit;
        this.heartacheLimit = heartacheLimit;
        this.roadtripLimit = roadtripLimit;
        this.blissfulLimit = blissfulLimit;
        this.heartacheCount = 0;
        this.roadtripCount = 0;
        this.blissfulCount = 0;
        this.minHeaps = new ArrayList<Heap>();
    }

    public void addPlaylist(int playlistId, int playlistSize, Song[] items) {
        playlists.put(playlistId, new Playlist(playlistId, playlistSize, items));
    }

    public void createEpicBlend() {
        for (String category: categories) {
            Heap heap = new Heap(false, category);
            for (Playlist playlist: playlists.values()) {
                switch (category) {
                    case "heartache":
                        for (Song song: playlist.getMaxHeaps().get(1).getSongs()) {
                            firstAdd(song, heap, category, playlist);
                        }
                        break;
                    case "roadtrip":
                        for (Song song: playlist.getMaxHeaps().get(1).getSongs()) {
                            firstAdd(song, heap, category, playlist);
                        }
                        break;
                    case "blissful":
                        for (Song song: playlist.getMaxHeaps().get(2).getSongs()) {
                            firstAdd(song, heap, category, playlist);
                        }
                        break;
                }
            }
            minHeaps.add(heap);
        }
    }

    private void firstAdd(Song song, Heap heap, String category, Playlist playlist) {
        if (heap.size() == 0) {
            heap.add(song);
            incrementCategoryCount(category);
            playlist.incrementPlaylistCategoryCount(category);
        }

        else if (getCategoryCount(category) < getCategoryLimit(category)) {
            if (playlist.getPlaylistCategoryCount(category) < playlistLimit) {
                if (song.getCategoryScore(category) > heap.peek().getCategoryScore(category)) {
                    heap.add(song);
                    incrementCategoryCount(category);
                    playlist.incrementPlaylistCategoryCount(category);
                }
            }
        }
        else {
            if (playlist.getPlaylistCategoryCount(category) < playlistLimit) {
                if (song.getCategoryScore(category) > heap.peek().getCategoryScore(category)) {
                    heap.pop();
                    heap.add(song);
                    incrementCategoryCount(category);
                    playlist.incrementPlaylistCategoryCount(category);
                }
            }
        }
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

    public void printBlend() {
        for (Playlist playlist: playlists.values()) {
            for (Song song: playlist.getMaxHeaps().get(0).getSongs()) {
                System.out.print(song.getId() + " " + song.getName());
            }
            System.out.println();
        }
    }

    public HashMap<Integer, Playlist> getPlaylists() {
        return playlists;
    }

}
