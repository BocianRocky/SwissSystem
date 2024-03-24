public class Player{
    private final String name;
    private int points;             //points scored by the player
    Player(String n){
        this.name=n;
        this.points=0;
    }

    public String getName() {
        return name;
    }

    public int getPoints() {
        return points;
    }
    public void addPoints(int n){
        points=points+n;
    }
}
