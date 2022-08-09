package VetulusJava.Tribes.Leaderboard;

public class BuildingLeaderboardEntry extends LeaderboardEntry{
    public int buildings;

    public BuildingLeaderboardEntry(String kingdomName, int buildings) {
        this.kingdomName = kingdomName;
        this.buildings = buildings;
    }

    @Override
    public int compareTo(Object compared) {
        return ((BuildingLeaderboardEntry)compared).buildings - this.buildings;
    }
}