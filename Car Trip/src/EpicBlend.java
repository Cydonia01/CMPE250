import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale.Category;

public class EpicBlend {
    private final String[] categories = {"heartache", "roadtrip", "blissful"};
    private HashMap<Integer, Playlist> playlists;
    private ArrayList<HashSet<Integer>> songsInEpicBlend;
    private ArrayList<Heap> epicMinHeaps;
    private ArrayList<Heap> epicMaxHeaps;
    private int heartacheCount, roadtripCount, blissfulCount;
    private int playlistLimit;
    private int heartacheLimit;
    private int roadtripLimit;
    private int blissfulLimit;
    private FileWriter writer;
    
    public EpicBlend(int playlistLimit, int heartacheLimit, int roadtripLimit, int blissfulLimit, FileWriter writer) {
        this.playlists = new HashMap<Integer, Playlist>();
        this.songsInEpicBlend = new ArrayList<HashSet<Integer>>();
        this.epicMinHeaps = new ArrayList<Heap>();
        this.epicMaxHeaps = new ArrayList<Heap>();
        this.playlistLimit = playlistLimit;
        this.heartacheLimit = heartacheLimit;
        this.roadtripLimit = roadtripLimit;
        this.blissfulLimit = blissfulLimit;
        this.heartacheCount = 0;
        this.roadtripCount = 0;
        this.blissfulCount = 0;
        this.writer = writer;

        for (String category: categories) {
            Heap heap = new Heap(false, category);
            epicMinHeaps.add(heap);
            songsInEpicBlend.add(new HashSet<Integer>());
        }
    }

    public void addPlaylist(int playlistId, int playlistSize, Song[] items) {
        playlists.put(playlistId, new Playlist(playlistId, playlistSize, items));
    }

    public void createEpicMaxHeaps() {
        for (String category: categories) {
            Heap heap = new Heap(true, category);
            for (Playlist playlist: playlists.values()) {
                if (playlist.getMaxHeap(category).peek() != null) {
                    heap.add(playlist.getMaxHeap(category).peek());
                }
            }
            epicMaxHeaps.add(heap);
        }
    }

    public void createEpicBlend() {
        for (String category: categories) {
            while (getCategoryCount(category) < getCategoryLimit(category) && epicMaxHeaps.get(getCategoryIndex(category)).peek() != null) {
                int index = getCategoryIndex(category);
                Song song = epicMaxHeaps.get(index).pop();
                Playlist playlist = playlists.get(song.getPlaylistId());
                if (playlist.getPlaylistCategoryCount(category) < playlistLimit) {
                    epicMinHeaps.get(index).add(song);
                    playlist.getMinHeap(category).add(song);
                    playlist.getMaxHeap(category).pop();
                    songsInEpicBlend.get(index).add(song.getId());
                    playlist.incrementPlaylistCategoryCount(category);
                    incrementCategoryCount(category);
                    if (playlist.getMaxHeap(category).peek() != null) {
                        epicMaxHeaps.get(index).add(playlist.getMaxHeap(category).peek());
                    }
                }
            }
        }
    }

    public void add(int playlistId, Song song) throws IOException {
        Playlist playlist = playlists.get(playlistId);
        song.setPlaylistId(playlistId);
        String addLog = "";
        String removeLog = "";
        boolean added = false;
        Song removedSong = null;
        Song lastRoot;
        Song newRoot;
        for (String category: categories) {
            Heap maxHeap = playlist.getMaxHeap(category);
            Heap minHeap = playlist.getMinHeap(category);
            
            //playlist limiti dolmamış
            if (playlist.getPlaylistCategoryCount(category) < playlistLimit) {
                //playlist ve epic blend limiti dolmamış
                if (getCategoryCount(category) < getCategoryLimit(category)) {
                    minHeap.add(song);
                    songsInEpicBlend.get(getCategoryIndex(category)).add(song.getId());
                    epicMinHeaps.get(getCategoryIndex(category)).add(song);
                    incrementCategoryCount(category);
                    playlist.incrementPlaylistCategoryCount(category);
                    added = true;
                }

                // epic blend limiti dolmuş
                else if (epicMinHeaps.get(getCategoryIndex(category)).peek() == null || song.compareTo(epicMinHeaps.get(getCategoryIndex(category)).peek(), category) > 0) {
                    removedSong = epicMinHeaps.get(getCategoryIndex(category)).peek();

                    Playlist removedPlaylist = playlists.get(removedSong.getPlaylistId());

                    removedPlaylist.getMinHeap(category).pop();
                    epicMinHeaps.get(getCategoryIndex(category)).pop();
                    lastRoot = removedPlaylist.getMaxHeap(category).peek();
                    removedPlaylist.getMaxHeap(category).add(removedSong);

                    newRoot = removedPlaylist.getMaxHeap(category).peek();
                    if (lastRoot == null) {
                        epicMaxHeaps.get(getCategoryIndex(category)).add(newRoot);
                    }
                    if (lastRoot != null && newRoot != null && lastRoot.getId() != newRoot.getId()) {
                        epicMaxHeaps.get(getCategoryIndex(category)).remove(lastRoot.getId());
                        epicMaxHeaps.get(getCategoryIndex(category)).add(newRoot);
                    }
                    removedPlaylist.decrementPlaylistCategoryCount(category);
                    playlist.incrementPlaylistCategoryCount(category);
                    minHeap.add(song);
                    epicMinHeaps.get(getCategoryIndex(category)).add(song);
                    songsInEpicBlend.get(getCategoryIndex(category)).add(song.getId());
                    songsInEpicBlend.get(getCategoryIndex(category)).remove(removedSong.getId());
                    added = true;

                    if (removedPlaylist.getPlaylistCategoryCount(category) == playlistLimit - 1 && removedPlaylist.getMaxHeap(category).peek() != null) {
                        epicMaxHeaps.get(getCategoryIndex(category)).add(removedPlaylist.getMaxHeap(category).peek());
                    }
                }
            }

            //playlist limiti dolmuş
            else if (song.compareTo(minHeap.peek(), category) > 0) {
                removedSong = minHeap.pop();
                lastRoot = maxHeap.peek();
                maxHeap.add(removedSong);
                newRoot = maxHeap.peek();
                
                if (lastRoot == null) {
                    epicMaxHeaps.get(getCategoryIndex(category)).add(newRoot);
                }
                if (lastRoot != null && newRoot != null && lastRoot.getId() != newRoot.getId()) {
                    epicMaxHeaps.get(getCategoryIndex(category)).remove(lastRoot.getId());
                    epicMaxHeaps.get(getCategoryIndex(category)).add(newRoot);
                }
                
                minHeap.add(song);
                epicMinHeaps.get(getCategoryIndex(category)).remove(removedSong.getId());
                epicMinHeaps.get(getCategoryIndex(category)).add(song);
                songsInEpicBlend.get(getCategoryIndex(category)).add(song.getId());
                songsInEpicBlend.get(getCategoryIndex(category)).remove(removedSong.getId());
                added = true;
            }

            if (added) {
                addLog += String.format("%d ", song.getId());
                added = false;
            } else {
                addLog += "0 ";
                lastRoot = playlist.getMaxHeap(category).peek();
                maxHeap.add(song);
                newRoot = playlist.getMaxHeap(category).peek();
                if (lastRoot == null) {
                    epicMaxHeaps.get(getCategoryIndex(category)).add(newRoot);
                }
                if (lastRoot != null && newRoot != null && lastRoot.getId() != newRoot.getId()) {
                    epicMaxHeaps.get(getCategoryIndex(category)).remove(lastRoot.getId());
                    epicMaxHeaps.get(getCategoryIndex(category)).add(newRoot);
                }
            }
            
            if (removedSong != null) {
                removeLog += String.format("%d ", removedSong.getId());
                removedSong = null;
            } else
                removeLog += "0 ";
        }
        writer.write(addLog.strip() + "\n");
        writer.write(removeLog.strip() + "\n");
    }

    public void printEpicMax() {
        for (String category: categories) {
            System.out.println(category);

            System.out.println(epicMaxHeaps.get(getCategoryIndex(category)).size());
        }
    }

    public void remove(int playlistId, int songId) throws IOException {
        if (songId == 8) {
            printBlend();
            printEpicMax();
            printRemoved();

        }
        String addLog = "";
        String removeLog = "";
        for (String category: categories) {
            Playlist playlist = playlists.get(playlistId);
            Heap maxHeap = playlist.getMaxHeap(category);
            Heap minHeap = playlist.getMinHeap(category);
            Song newRoot = null;
            Song lastRoot = null;

            if (songsInEpicBlend.get(getCategoryIndex(category)).contains(songId) == false) {
                lastRoot = maxHeap.peek();
                maxHeap.remove(songId);
                newRoot = maxHeap.peek();
                
                if (newRoot != null && lastRoot != null && newRoot.getId() != lastRoot.getId()) {
                    epicMaxHeaps.get(getCategoryIndex(category)).add(newRoot);
                    epicMaxHeaps.get(getCategoryIndex(category)).remove(lastRoot.getId()); // etkiliyor bayağı
                }
                addLog += "0 ";
                removeLog += "0 ";
            }
            else {
                Song replacingSong = null;
                Playlist replacingPlaylist = null;
                playlist.decrementPlaylistCategoryCount(category);
                decrementCategoryCount(category); // etkiliyor bayağı

                if (playlist.getPlaylistCategoryCount(category) == playlistLimit - 1 && playlist.getMaxHeap(category).peek() != null) {
                    epicMaxHeaps.get(getCategoryIndex(category)).add(playlist.getMaxHeap(category).peek());
                }
                
                while (epicMaxHeaps.get(getCategoryIndex(category)).peek() != null && songsInEpicBlend.get(getCategoryIndex(category)).contains(epicMaxHeaps.get(getCategoryIndex(category)).peek().getId())) {
                    if (songId == 8) {
                        printEpicMax();
                    }
                    epicMaxHeaps.get(getCategoryIndex(category)).pop();
                }
                
                while (epicMaxHeaps.get(getCategoryIndex(category)).peek() != null) {
                    if (songId == 8) {
                        printEpicMax();
                    }
                    replacingSong = epicMaxHeaps.get(getCategoryIndex(category)).pop();
                    replacingPlaylist = playlists.get(replacingSong.getPlaylistId());
                    if (replacingPlaylist.getPlaylistCategoryCount(category) < playlistLimit) {
                        break;
                    }
                }

                if (replacingSong != null && replacingPlaylist.getPlaylistCategoryCount(category) < playlistLimit) {
                    replacingPlaylist.getMaxHeap(category).pop();
                    replacingPlaylist.getMinHeap(category).add(replacingSong);
                    epicMinHeaps.get(getCategoryIndex(category)).add(replacingSong);
                    replacingPlaylist.incrementPlaylistCategoryCount(category);

                    incrementCategoryCount(category);
                    songsInEpicBlend.get(getCategoryIndex(category)).add(replacingSong.getId());
                    
                    epicMinHeaps.get(getCategoryIndex(category)).remove(songId);
                    minHeap.remove(songId);
                    songsInEpicBlend.get(getCategoryIndex(category)).remove(songId);
                    //epicMaxHeaps.get(getCategoryIndex(category)).remove(replacingSong.getId());

                    if (replacingPlaylist.getMaxHeap(category).peek() != null && replacingPlaylist.getPlaylistCategoryCount(category) < playlistLimit) {
                        epicMaxHeaps.get(getCategoryIndex(category)).add(replacingPlaylist.getMaxHeap(category).peek());
                    }
                    
                    addLog += String.format("%d ", replacingSong.getId());
                }
                else {
                    epicMinHeaps.get(getCategoryIndex(category)).remove(songId);
                    minHeap.remove(songId);
                    songsInEpicBlend.get(getCategoryIndex(category)).remove(songId);
                    addLog += "0 ";
                }
                removeLog += String.format("%d ", songId);
            }
        }
        writer.write(addLog.strip() + "\n");
        writer.write(removeLog.strip() + "\n");
    }

    public void printBlend() {
        for (String category: categories) {
            System.out.println(category);
            for (Song song: epicMinHeaps.get(getCategoryIndex(category)).getSongs()) {
                if (song != null) {
                    if (song.getId() == 33 || song.getId() == 37) {
                        System.out.println(song.getId());
                    }
                }
            }
        }
    }

    public void printRemoved() {
        for (String category: categories) {
            System.out.println(category);
            for (int songId: epicMaxHeaps.get(getCategoryIndex(category)).getRemovedSongs()) {
                if (songId == 33 || songId == 37) {
                    System.out.println(songId);
                }
            }
        }
    }

    public int partition(ArrayList<Song> songs, int low, int high) {
        Song pivot = songs.get(high);
        int i = (low - 1);
        Song temp;
        for (int j = low; j <= high - 1; j++) {
            if (songs.get(j).getPlayCount() > pivot.getPlayCount() || (songs.get(j).getPlayCount() == pivot.getPlayCount() && songs.get(j).getName().compareTo(pivot.getName()) <= 0)) {
                i++;
                temp = songs.get(i);
                songs.set(i, songs.get(j));
                songs.set(j, temp);
            }
        }
        temp = songs.get(i + 1);
        songs.set(i + 1, songs.get(high));
        songs.set(high, temp);
        return (i + 1);
    }

    public void quickSort(ArrayList<Song> songs, int low, int high) {
        if (low < high) {
            int j = partition(songs, low, high);
            quickSort(songs, low, j - 1);
            quickSort(songs, j + 1, high);
        }
    }

    public void ask() throws IOException {
        ArrayList<Song> combinedSongs = new ArrayList<Song>();
        combinedSongs.addAll(epicMinHeaps.get(0).getSongs().subList(1, epicMinHeaps.get(0).getSongs().size()));
        combinedSongs.addAll(epicMinHeaps.get(1).getSongs().subList(1, epicMinHeaps.get(1).getSongs().size()));
        combinedSongs.addAll(epicMinHeaps.get(2).getSongs().subList(1, epicMinHeaps.get(2).getSongs().size()));
        
        quickSort(combinedSongs, 0, combinedSongs.size() - 1);

        String askLog = "";
        for (int i = 0; i < combinedSongs.size(); i++) {
            if (i > 0) {
                if (combinedSongs.get(i).getName().equals(combinedSongs.get(i - 1).getName())) {
                    continue;
                }
            }
            
            boolean inEpicBlend = false;
            for (String category: categories) {
                if (songsInEpicBlend.get(getCategoryIndex(category)).contains(combinedSongs.get(i).getId())) {
                    inEpicBlend = true;
                }
            }
            if (inEpicBlend) {
                askLog += combinedSongs.get(i).getId() + " ";
            }
        }
        writer.write(askLog.strip() + "\n");
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

    public void decrementCategoryCount(String category) {
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
}