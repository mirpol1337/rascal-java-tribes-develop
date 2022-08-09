package VetulusJava.Tribes.DTOs;

import VetulusJava.Tribes.Leaderboard.LeaderboardEntry;

import java.util.ArrayList;

public class LeaderboardDTO {
    public ArrayList<LeaderboardEntry> leaderboard;

    public LeaderboardDTO(ArrayList<LeaderboardEntry> leaderboard) {
        this.leaderboard = leaderboard;
    }
}
