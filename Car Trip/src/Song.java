public class Song {
    private int id;
    private String name;
    private int playCount;
    private int heartacheScore;
    private int roadtripScore;
    private int blissfulScore;

    public Song(int id, String name, int playCount, int heartacheScore, int roadtripScore, int blissfulScore) {
        this.id = id;
        this.name = name;
        this.playCount = playCount;
        this.heartacheScore = heartacheScore;
        this.roadtripScore = roadtripScore;
        this.blissfulScore = blissfulScore;
    }


    public int getCategoryScore(String category) {
        switch (category) {
            case "heartache":
                return heartacheScore;
            case "roadtrip":
                return roadtripScore;
            case "blissful":
                return blissfulScore;
            default:
                return 0;
        }
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPlayCount() {
        return playCount;
    }

}
