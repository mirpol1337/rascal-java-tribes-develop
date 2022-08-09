package VetulusJava.Tribes.Leaderboard;

public class TroopLeaderboardEntry extends LeaderboardEntry {
    public int soldiers;

    public TroopLeaderboardEntry(String kingdomName, int soldiers) {
        this.kingdomName = kingdomName;
        this.soldiers = soldiers;
    }

    @Override
    public int compareTo(Object compared) {
        return ((TroopLeaderboardEntry)compared).soldiers - this.soldiers;
    }
}
