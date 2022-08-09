package VetulusJava.Tribes.Services.LeaderboardService;

import VetulusJava.Tribes.Leaderboard.BuildingLeaderboardEntry;
import VetulusJava.Tribes.Leaderboard.ILeaderboardable;
import VetulusJava.Tribes.Leaderboard.LeaderboardEntry;
import VetulusJava.Tribes.Leaderboard.TroopLeaderboardEntry;
import VetulusJava.Tribes.Enum.LeaderboardType;
import VetulusJava.Tribes.Repositories.IBuildingRepository;
import VetulusJava.Tribes.Repositories.ITroopRepository;
import VetulusJava.Tribes.Services.KingdomService.IKingdomService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class LeaderboardService implements ILeaderboardService{
    private IBuildingRepository buildingRepository;
    private ITroopRepository troopRepository;
    private IKingdomService kingdomService;

    public LeaderboardService(IBuildingRepository buildingRepository, ITroopRepository troopRepository, IKingdomService kingdomService) {
        this.buildingRepository = buildingRepository;
        this.troopRepository = troopRepository;
        this.kingdomService = kingdomService;
    }

    public ArrayList<LeaderboardEntry> getBuildingLeaderboard(){
        return buildLeaderboard(LeaderboardType.BUILDING);
    }

    public ArrayList<LeaderboardEntry> getTroopsLeaderboard(){
        return buildLeaderboard(LeaderboardType.TROOP);
    }

    private ArrayList<LeaderboardEntry> buildLeaderboard(LeaderboardType type){
        HashMap<Long, Integer> leaderboardMap = new HashMap<>();
        Iterable<? extends ILeaderboardable> objects;
        switch (type){
            case BUILDING:
                objects = buildingRepository.findAll();
                break;
            case TROOP:
                objects = troopRepository.findAll();
                break;
            default:
                objects = null;
        }
        for (var item: objects){
            long kingdomId = item.getKingdomId();
            int score = leaderboardMap.containsKey(kingdomId) ? leaderboardMap.get(kingdomId) : 0;
            score += item.getScore();
            leaderboardMap.put(kingdomId, score);
        }
        var leaderboard = new ArrayList<LeaderboardEntry>();
        for (var entry: leaderboardMap.entrySet()){
            var kingdomName = kingdomService.getById(entry.getKey()).getName();
            switch (type){
                case BUILDING:
                    leaderboard.add(new BuildingLeaderboardEntry(kingdomName, entry.getValue()));
                    break;
                case TROOP:
                    leaderboard.add(new TroopLeaderboardEntry(kingdomName, entry.getValue()));
                    break;
            }
        }
        Collections.sort(leaderboard);
        return leaderboard;
    }
}
