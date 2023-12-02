import java.util.ArrayList;
public class EpicBlend {
    private ArrayList<Playlist> playlists;
    private Heap songs;
    private int playlistLimit;
    private int heartacheLimit;
    private int roadtripLimit;
    private int blissfulLimit ;
    
    public EpicBlend(int playlistLimit, int heartacheLimit, int roadtripLimit, int blissfulLimit) {
        this.playlists = new ArrayList<Playlist>();
        this.playlistLimit = playlistLimit;
        this.heartacheLimit = heartacheLimit;
        this.roadtripLimit = roadtripLimit;
        this.blissfulLimit = blissfulLimit;
        this.songs = new Heap(true, "heartache");
    }

    public void addPlaylist(int playlistId, int playlistSize) {
        playlists.add(new Playlist(playlistId, playlistSize));
    }



    public void printBlend() {
        for (Playlist playlist: playlists) {
            System.out.println(playlist.getId() + " " + playlist.getSongs().size());
            for (Song song: playlist.getSongs()) {
                System.out.print(song.getId() + " ");
            }
            System.out.println();
        }
    }

    public ArrayList<Playlist> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(ArrayList<Playlist> playlists) {
        this.playlists = playlists;
    }

    public int getPlaylistLimit() {
        return playlistLimit;
    }

    public void setPlaylistLimit(int playlistLimit) {
        this.playlistLimit = playlistLimit;
    }

    public int getHeartacheLimit() {
        return heartacheLimit;
    }

    public void setHeartacheLimit(int heartacheLimit) {
        this.heartacheLimit = heartacheLimit;
    }

    public int getRoadtripLimit() {
        return roadtripLimit;
    }

    public void setRoadtripLimit(int roadtripLimit) {
        this.roadtripLimit = roadtripLimit;
    }

    public int getBlissfulLimit() {
        return blissfulLimit;
    }

    public void setBlissfulLimit(int blissfulLimit) {
        this.blissfulLimit = blissfulLimit;
    } 
}
