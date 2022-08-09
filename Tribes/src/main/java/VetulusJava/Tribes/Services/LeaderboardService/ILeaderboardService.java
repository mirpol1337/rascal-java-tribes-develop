package VetulusJava.Tribes.Services.LeaderboardService;

import VetulusJava.Tribes.Leaderboard.LeaderboardEntry;

import java.util.ArrayList;

public interface ILeaderboardService {
    public ArrayList<LeaderboardEntry> getBuildingLeaderboard();
    public ArrayList<LeaderboardEntry> getTroopsLeaderboard();
}
