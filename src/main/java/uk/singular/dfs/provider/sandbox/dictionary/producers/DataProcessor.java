package uk.singular.dfs.provider.sandbox.dictionary.producers;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.singular.dfs.provider.sandbox.dictionary.model.Country;
import uk.singular.dfs.provider.sandbox.dictionary.model.League;
import uk.singular.dfs.provider.sandbox.dictionary.model.Sport;
import uk.singular.dfs.provider.sandbox.dictionary.model.Team;
import uk.singular.dfs.provider.sandbox.dictionary.services.CountryService;
import uk.singular.dfs.provider.sandbox.dictionary.services.LeagueService;
import uk.singular.dfs.provider.sandbox.dictionary.services.SportService;
import uk.singular.dfs.provider.sandbox.dictionary.services.TeamService;
import uk.singular.dfs.provider.sandbox.dictionary.services.impl.DataImportServiceImpl;

import java.util.HashMap;

@Data
@Component
public class DataProcessor implements Runnable{

    @Autowired
    private SportService sportService;
    @Autowired
    private CountryService countryService;
    @Autowired
    private LeagueService leagueService;
    @Autowired
    private TeamService teamService;

    HashMap<Integer, Sport> sportMap = new HashMap<>();
    HashMap<Integer, Country> countryMap = new HashMap<>();
    HashMap<Integer, League> leagueMap = new HashMap<>();
    HashMap<Integer,Team> teamMap = new HashMap<>();

    private static final Logger LOG = LoggerFactory.getLogger(DataProcessor.class);

    @Override
    public void run() {
        /** I am using saveAll() instead of save() because of better performance
         *  Batching is enabled with size 3000 which improves the saving time */
        if(!sportMap.isEmpty()){
            sportService.saveAll(sportMap.values());
            LOG.info("Finished saving sports");
        }
        if(!countryMap.isEmpty()){
            countryService.saveAll(countryMap.values());
            LOG.info("Finished saving countries");
        }
        if(!leagueMap.isEmpty()){
            leagueService.saveAll(leagueMap.values());
            LOG.info("Finished saving leagues");
        }
        if(!teamMap.isEmpty()){
            teamService.saveAll(teamMap.values());
            LOG.info("Finished saving teams");
        }
    }


}
