import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Table {
    private ArrayList<Player> list;         //list of players participating in the tournament
    private ArrayList<Pair> pairs;          //list of who plays with whom during the round
    private Set<Pair> playedPairs;          //pairs that have already played together

    private int round;

    private ArrayList<Pair> helperList;     //support list
    private int iter;
    private ArrayList<Player> bannedList;   //players who are already assigned to the game in the current round
    private ArrayList<Player> pauseList;    //players who had a break
    private ArrayList<Player> bannedPlayer; //player who is already assigned in the round
    public Table() {
        list = new ArrayList<>();
        pairs = new ArrayList<>();
        playedPairs = new HashSet<>();
        round=0;
        helperList=new ArrayList<>();
        iter=0;
        bannedList=new ArrayList<>();
        pauseList=new ArrayList<>();
        bannedPlayer=new ArrayList<>();
    }
    public void start() {
        Scanner scanner = new Scanner(System.in);
        boolean flag = true;
        while (flag) {
            menu();
            int decision = scanner.nextInt();
            scanner.nextLine();
            switch (decision) {

                case 1:
                    addPlayer();
                    break;
                case 2:
                    showTable();
                    break;
                case 3:
                    round++;
                    sortedPlayerByPoints();
                    printPairs();
                    break;
                case 4:
                    printPairs();
                    break;
                case 5:
                    addPoints();
                    break;
                case 6:
                    System.out.println(".END.");
                    flag = false;
                    break;
            }
        }

    }
    private void addPlayer(){
        Scanner scanner = new Scanner(System.in);
        boolean adding = true;
        while(adding){
            System.out.println("Enter the name: ");
            String name = scanner.nextLine();
            if(name.equals("k")){
                adding=false;
            }else {
                Player player = new Player(name);
                list.add(player);
            }
        }
    }

    private void menu(){
        System.out.println();
        System.out.println("1. Enter the player");
        System.out.println("2. Show the table");
        System.out.println("3. GENERATING and DISPLAYING NEW PAIRS");
        System.out.println("4. Display pairs");
        System.out.println("5. Add points");
        System.out.println("6. End");
        System.out.println();
    }
    private void showTable(){
        for (Player player : list) {
            System.out.println(player.getName() + "\t" + player.getPoints());
        }
    }
    private void printPairs() {
        System.out.println("ROUND: "+round+"\n");
        System.out.println("******");
        if(pairs.size()==0){
            System.out.println("empty");
        }
        for (int i = 0; i < pairs.size(); i++) {
            System.out.println("Pair " + (i + 1) + ": " + pairs.get(i).getPlayer1().getName() + " vs " + pairs.get(i).getPlayer2().getName());
        }
        System.out.println("******");

    }

    private void sortedPlayerByPoints(){
        sorting();

        pairs.clear();
        if (list.size() % 2 == 1) {                     //This part of the code determines who takes a break in the round if the number of players is odd
            for (int i = list.size()-1; i >=0; i--) {
                if(!pauseList.contains(list.get(i))){
                    list.get(i).addPoints(1);
                    bannedList.add(list.get(i));
                    bannedPlayer.add(list.get(i));
                    pauseList.add(list.get(i));
                    break;
                }
            }
        }
        for (int i = 0; i < list.size(); i+=2) {
            if(i+1< list.size()){
                Pair pair = new Pair(list.get(i),list.get(i+1));
                if(playedPairs.contains(pair) || bannedPlayer.contains(list.get(i)) || bannedPlayer.contains(list.get(i+1))){
                    findNewPair(i);
                    i+=iter;
                    iter=0;
                }else{
                    helperList.add(pair);
                    bannedList.addAll(Arrays.asList(pair.getPlayer1(), pair.getPlayer2()));
                    bannedPlayer.addAll(Arrays.asList(list.get(i), list.get(i+1)));
                }
            }
        }

        pairs.addAll(helperList);
        playedPairs.addAll(helperList);
        helperList.clear();
        bannedList.clear();
        bannedPlayer.clear();
    }
    private void findNewPair(int x) {

        int num = 0;
        if (x + 3 < list.size()) {      //The table has more than 3 players to create 2 pairs simultaneously
            iter = 2;
            for (int i = 2; i < list.size(); i++) {
                try {
                    Pair fpair = new Pair(list.get(x), list.get(x + i));
                    if (!playedPairs.contains(fpair) && !bannedList.contains(list.get(x + i))&& !bannedPlayer.contains(list.get(x)) && !bannedPlayer.contains(list.get(x+i))) {    //para nie istnieje i przypisany gracz nie jest zbanowany
                        bannedList.addAll(Arrays.asList(list.get(x), list.get(x + i)));
                        bannedPlayer.addAll(Arrays.asList(list.get(x), list.get(x + i)));
                        helperList.add(fpair);
                        num = i;
                        break;
                    }
                }catch(IndexOutOfBoundsException e){
                    System.out.println("player must take a break");
                    break;
                }
            }
            for (int i = 2; i < list.size(); i++) {
                try {
                    if (x + i != num) {     //Player from the first pair
                        Pair spair = new Pair(list.get(x + 1), list.get(x + i));
                        if (!playedPairs.contains(spair) && !bannedList.contains(list.get(x + i))&& !bannedPlayer.contains(list.get(x+1)) && !bannedPlayer.contains(list.get(x+i))) {
                            bannedList.addAll(Arrays.asList(list.get(x+1), list.get(x + i)));
                            bannedPlayer.addAll(Arrays.asList(list.get(x+1), list.get(x + i)));
                            helperList.add(spair);
                            break;
                        }
                    }
                }catch(IndexOutOfBoundsException e){
                    System.out.println("player must take a break");
                    break;
                }
            }
        } else if (x + 2 == list.size()) {      //last players on the list
            Pair pair = new Pair(list.get(x), list.get(x + 1));
            helperList.add(pair);

        }

    }
    private void sorting(){
        Collections.sort(list, new Comparator<Player>() {
            @Override
            public int compare(Player p1, Player p2) {
                return Integer.compare(p2.getPoints(), p1.getPoints());     //sorting in desc order by points
            }
        });


        for (int i = 0; i < list.size() - 1; i++) {                 //shuffling the order of players with the same number of points
            if (list.get(i).getPoints() == list.get(i + 1).getPoints()) {   //same points

                int j = i;
                while (j + 1 < list.size() && list.get(j).getPoints() == list.get(j + 1).getPoints()) {
                    j++;            //checking the boundary of points
                }
                // randomly selecting elements from i to j
                for (int k = i; k <= j; k++) {
                    int randomIndex = ThreadLocalRandom.current().nextInt(i, j + 1); //swapping elements of the list at positions k and randomIndex
                    Player temp = list.get(k);
                    list.set(k, list.get(randomIndex));
                    list.set(randomIndex, temp);
                }
                i = j;
            }
        }
        for (Player player : list) {
            System.out.println(player.getName() + ":\t" + player.getPoints());
        }
        System.out.println();
    }


    private void addPoints(){

        Scanner scanner = new Scanner(System.in);
        for (int i = 0; i < pairs.size(); i++) {
            System.out.println(pairs.get(i).getPlayer1().getName()+"   "+pairs.get(i).getPlayer2().getName());
        }
        try {
            for (Pair pair : pairs) {
                System.out.println(pair.getPlayer1().getName() + ": ");
                int p = scanner.nextInt();
                pair.getPlayer1().addPoints(p);
                System.out.println(pair.getPlayer2().getName() + ": ");
                int pp = scanner.nextInt();
                pair.getPlayer2().addPoints(pp);
            }
        }catch(InputMismatchException e){
            System.out.println("Invalid value entered!!!");
            scanner.next();
        }
    }
}
