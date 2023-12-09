import java.util.ArrayList;
import java.util.HashMap;
public class EpicBlend {
    private final String[] categories = {"heartache", "roadtrip", "blissful"};
    private HashMap<Integer, Playlist> playlists;
    private ArrayList<Heap> epicMinHeaps;
    private ArrayList<Heap> epicMaxHeaps;
    private ArrayList<Heap> tempEpicMaxHeaps;
    private int heartacheCount, roadtripCount, blissfulCount;
    private int playlistLimit;
    private int heartacheLimit;
    private int roadtripLimit;
    private int blissfulLimit;
    
    public EpicBlend(int playlistLimit, int heartacheLimit, int roadtripLimit, int blissfulLimit) {
        this.playlists = new HashMap<Integer, Playlist>();
        this.epicMinHeaps = new ArrayList<Heap>();
        this.epicMaxHeaps = new ArrayList<Heap>();
        this.tempEpicMaxHeaps = new ArrayList<Heap>();
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

    public void createEpicMaxHeaps() {
        for (String category: categories) {
            Heap heap = new Heap(true, category);
            for (Playlist playlist: playlists.values()) {
                if (playlist.getMaxHeap(category).size() > 0) {
                    heap.add(playlist.getMaxHeap(category).peek());
                }
            }
            epicMaxHeaps.add(heap);
            tempEpicMaxHeaps.add(heap);
        }
    }

    public void createEpicBlend() {
        for (String category: categories) {
            while (getCategoryCount(category) < getCategoryLimit(category) && tempEpicMaxHeaps.get(getCategoryIndex(category)).size() > 0) {
                int index = getCategoryIndex(category);
                Song song = tempEpicMaxHeaps.get(index).pop();
                Playlist playlist = playlists.get(song.getPlaylistId());
                if (playlist.getPlaylistCategoryCount(category) < playlistLimit + 1) {
                    epicMinHeaps.get(index).add(song);
                    playlist.getMinHeap(category).add(song);
                    playlist.getMaxHeap(category).pop();
                    playlist.incrementPlaylistCategoryCount(category);
                    incrementCategoryCount(category);
                    if (playlist.getMaxHeap(category).size() > 0) {
                        tempEpicMaxHeaps.get(index).add(playlist.getMaxHeap(category).peek());
                    }
                }
            }
        }
    }

    public void add(int playlistId, Song song) {
        Playlist playlist = playlists.get(playlistId);
        song.setPlaylistId(playlistId);
        String addLog = "";
        String removeLog = "";
        boolean added = false;
        Song removedSong = null;
        for (String category: categories) {
            Heap maxHeap = playlist.getMaxHeap(category);
            Heap minHeap = playlist.getMinHeap(category);

            //playlist limiti dolmamış
            if (playlist.getPlaylistCategoryCount(category) < playlistLimit) {
                //playlist ve epic blend limiti dolmamış

                if (getCategoryCount(category) < getCategoryLimit(category)) {
                    minHeap.add(song);
                    epicMinHeaps.get(getCategoryIndex(category)).add(song);
                    added = true;
                    incrementCategoryCount(category);
                    playlist.incrementPlaylistCategoryCount(category);
                }
                // epic blend limiti dolmuş
                else if (epicMinHeaps.get(getCategoryIndex(category)).peek() == null || song.compareTo(epicMinHeaps.get(getCategoryIndex(category)).peek(), category) > 0) {
                    minHeap.add(song);
                    removedSong = epicMinHeaps.get(getCategoryIndex(category)).pop();
                    epicMinHeaps.get(getCategoryIndex(category)).add(song);
                    added = true;
                    playlist.incrementPlaylistCategoryCount(category);
                    playlists.get(removedSong.getPlaylistId()).decrementPlaylistCategoryCount(category);
                }
            }

            //playlist limiti dolmuş
            else if (song.compareTo(minHeap.peek(), category) > 0) {
                removedSong = minHeap.pop();
                minHeap.add(song);
                maxHeap.add(removedSong);
                epicMinHeaps.get(getCategoryIndex(category)).remove(removedSong.getId());
                epicMinHeaps.get(getCategoryIndex(category)).add(song);
                added = true;
            }

            if (added) {
                maxHeap.remove(song.getId());
                addLog += String.format("%d ", song.getId());
                added = false;
            } else {
                addLog += "0 ";
                maxHeap.add(song);
            }
            
            if (removedSong != null) {
                removeLog += String.format("%d ", removedSong.getId());
                removedSong = null;
            } else
                removeLog += "0 ";
        }
        System.out.println(addLog.strip());
        System.out.println(removeLog.strip());
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
                break;
            case "roadtrip":
                roadtripCount++;
                break;
            case "blissful":
                blissfulCount++;
                break;
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