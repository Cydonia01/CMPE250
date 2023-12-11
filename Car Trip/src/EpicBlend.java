import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class EpicBlend {
    private final String[] categories = {"heartache", "roadtrip", "blissful"};
    private HashMap<Integer, Playlist> playlists;
    private ArrayList<HashSet<Integer>> songsInEpicBlend;
    private ArrayList<Heap> epicMinHeaps;
    private ArrayList<Heap> epicMaxHeaps;
    private ArrayList<Heap> tempEpicMaxHeaps;
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
        this.tempEpicMaxHeaps = new ArrayList<Heap>();
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
                if (playlist.getPlaylistCategoryCount(category) < playlistLimit) {
                    epicMinHeaps.get(index).add(song);
                    playlist.getMinHeap(category).add(song);
                    playlist.getMaxHeap(category).pop();
                    songsInEpicBlend.get(index).add(song.getId());
                    playlist.incrementPlaylistCategoryCount(category);
                    incrementCategoryCount(category);
                    if (playlist.getMaxHeap(category).size() > 0) {
                        tempEpicMaxHeaps.get(index).add(playlist.getMaxHeap(category).peek());
                    }
                }
            }
        }
    }

    public void printBlend() {
        for (String category: categories) {
            System.out.println(category);
            for (Song song: epicMinHeaps.get(getCategoryIndex(category)).getSongs()) {
                if (song != null) {
                    System.out.println(song.getId());
                }
            }
        }
    }

    public void remove(int playlistId, int songId) throws IOException {
        String addLog = "";
        String removeLog = "";
        for (String category: categories) {
            Playlist playlist = playlists.get(playlistId);
            Heap maxHeap = playlist.getMaxHeap(category);
            Heap minHeap = playlist.getMinHeap(category);
            Song newRoot = null;
            Song lastRoot = maxHeap.peek();

            if (songsInEpicBlend.get(getCategoryIndex(category)).contains(songId) == false) {
                maxHeap.remove(songId);
                if (maxHeap.size() > 0) {
                    newRoot = maxHeap.peek();
                }
                if (newRoot != null && lastRoot != null && newRoot.getId() != lastRoot.getId()) {
                    epicMaxHeaps.get(getCategoryIndex(category)).remove(lastRoot.getId());
                    epicMaxHeaps.get(getCategoryIndex(category)).add(newRoot);
                }
                addLog += "0 ";
                removeLog += "0 ";
            }
            else {
                playlist.decrementPlaylistCategoryCount(category);
                Song replacingSong = null;
                Playlist replacingPlaylist = null;
                int level = 0;
                int maxLevel = (int) (Math.log(epicMaxHeaps.get(getCategoryIndex(category)).size()) / Math.log(2));
                
                while (level <= maxLevel) {
                    Song bestSong = epicMaxHeaps.get(getCategoryIndex(category)).getSongs().get((int) Math.pow(2, level));
                    Playlist bestPlaylist = playlists.get(bestSong.getPlaylistId());
                    replacingPlaylist = bestPlaylist;

                    for (int i = (int) Math.pow(2, level); i < (int) Math.pow(2, level + 1); i++) {
                        Song currentSong = null;
                        if (i < epicMaxHeaps.get(getCategoryIndex(category)).getSongs().size()) {
                            currentSong = epicMaxHeaps.get(getCategoryIndex(category)).getSongs().get(i);
                        }
                        
                        if (currentSong == null) break;
                        
                        Playlist currentPlaylist = playlists.get(currentSong.getPlaylistId());

                        if (currentSong.compareTo(bestSong, category) > 0 && currentPlaylist.getPlaylistCategoryCount(category) < playlistLimit) {
                            bestSong = currentSong;
                        }
                    }
                    replacingSong = bestSong;
                    replacingPlaylist = bestPlaylist;
                    level++;
                }
                if (replacingPlaylist != null) {
                    if (replacingPlaylist.getMaxHeap(category).size() > 0) {
                        lastRoot = replacingPlaylist.getMaxHeap(category).peek();
                    }
                }

                minHeap.remove(songId);
                songsInEpicBlend.get(getCategoryIndex(category)).remove(songId);
                epicMinHeaps.get(getCategoryIndex(category)).remove(songId);
                if (replacingSong != null) {
                    replacingPlaylist.getMaxHeap(category).remove(replacingSong.getId());
                    replacingPlaylist.getMinHeap(category).add(replacingSong);
                    songsInEpicBlend.get(getCategoryIndex(category)).add(replacingSong.getId());
                    replacingPlaylist.incrementPlaylistCategoryCount(category);
                    epicMinHeaps.get(getCategoryIndex(category)).add(replacingSong);

                    if (replacingPlaylist.getMaxHeap(category).size() > 0) {
                        newRoot = replacingPlaylist.getMaxHeap(category).peek();
                    }
                    if (newRoot != null && lastRoot != null && newRoot.getId() != lastRoot.getId()) {
                        epicMaxHeaps.get(getCategoryIndex(category)).remove(lastRoot.getId());
                        epicMaxHeaps.get(getCategoryIndex(category)).add(newRoot);
                    }
                    addLog += String.format("%d ", replacingSong.getId());
                }
                else {
                    addLog += "0 ";
                }
                removeLog += String.format("%d ", songId);
            }
        }
        writer.write(addLog.strip() + "\n");
        writer.write(removeLog.strip() + "\n");
        //printBlend();
        //printRemoved();
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
            lastRoot = maxHeap.peek();
            
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
                    songsInEpicBlend.get(getCategoryIndex(category)).remove(removedSong.getId());
                    epicMinHeaps.get(getCategoryIndex(category)).add(song);
                    added = true;
                    playlist.incrementPlaylistCategoryCount(category);
                    playlists.get(removedSong.getPlaylistId()).decrementPlaylistCategoryCount(category);
                    playlists.get(removedSong.getPlaylistId()).getMaxHeap(category).add(removedSong);
                    epicMaxHeaps.get(getCategoryIndex(category)).add(removedSong);//rastgele ekleyemeyiz ki
                }
            }

            //playlist limiti dolmuş
            else if (song.compareTo(minHeap.peek(), category) > 0) {
                removedSong = minHeap.pop();
                minHeap.add(song);
                maxHeap.add(removedSong);
                songsInEpicBlend.get(getCategoryIndex(category)).remove(removedSong.getId());
                epicMinHeaps.get(getCategoryIndex(category)).remove(removedSong.getId());
                epicMinHeaps.get(getCategoryIndex(category)).add(song);
                added = true;
            }

            if (added) {
                maxHeap.remove(song.getId());
                songsInEpicBlend.get(getCategoryIndex(category)).add(song.getId());
                addLog += String.format("%d ", song.getId());
                added = false;
            } else {
                addLog += "0 ";
                maxHeap.add(song);
            }
            newRoot = maxHeap.peek();
            
            if (removedSong != null) {
                removeLog += String.format("%d ", removedSong.getId());
                removedSong = null;
            } else
                removeLog += "0 ";
            
            if (lastRoot != null && newRoot != null && lastRoot.getId() != newRoot.getId()) {
                epicMaxHeaps.get(getCategoryIndex(category)).remove(lastRoot.getId());
                epicMaxHeaps.get(getCategoryIndex(category)).add(newRoot);
                newRoot = null;
            }
        }
        writer.write(addLog.strip() + "\n");
        writer.write(removeLog.strip() + "\n");
    }

    public void printRemoved() {
        for (String category: categories) {
            System.out.println(category);
            for (Integer song: songsInEpicBlend.get(getCategoryIndex(category))) {
                System.out.print(song + " ");
            }
            System.out.println();
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