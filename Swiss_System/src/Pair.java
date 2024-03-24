public class Pair{
    private  final Player player1;
    private  final Player player2;

    Pair(Player p1, Player p2){
        this.player1=p1;
        this.player2=p2;
    }
    public Player getPlayer1() {
        return player1;
    }
    public Player getPlayer2() {
        return player2;
    }


    public boolean equals(Object obj){
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Pair pair = (Pair) obj;

        return (player1.equals(pair.player1) && player2.equals(pair.player2)) ||
                (player1.equals(pair.player2) && player2.equals(pair.player1));
    }
    @Override
    public int hashCode() {
        return player1.hashCode() + player2.hashCode();
    }
}
