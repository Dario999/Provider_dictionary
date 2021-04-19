package uk.singular.dfs.provider.sandbox.dictionary.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uk.singular.dfs.provider.sandbox.dictionary.exceptions.InvalidFileTypeException;
import uk.singular.dfs.provider.sandbox.dictionary.exceptions.InvalidLanguageException;
import uk.singular.dfs.provider.sandbox.dictionary.model.Country;
import uk.singular.dfs.provider.sandbox.dictionary.model.League;
import uk.singular.dfs.provider.sandbox.dictionary.model.Sport;
import uk.singular.dfs.provider.sandbox.dictionary.model.Team;
import uk.singular.dfs.provider.sandbox.dictionary.producers.DataProcessor;
import uk.singular.dfs.provider.sandbox.dictionary.services.*;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;

@Service
public class DataImportServiceImpl implements DataImportService {

    private final ExecutorService executorService;
    private final SportService sportService;
    private final CountryService countryService;
    private final LeagueService leagueService;
    private final TeamService teamService;
    private final LanguageService languageService;

    public static String TYPE = "text/csv";

    private static final Logger LOG = LoggerFactory.getLogger(DataImportServiceImpl.class);

    public DataImportServiceImpl(ExecutorService executorService, SportService sportService, CountryService countryService, LeagueService leagueService, TeamService teamService, LanguageService languageService) {
        this.executorService = executorService;
        this.sportService = sportService;
        this.countryService = countryService;
        this.leagueService = leagueService;
        this.teamService = teamService;
        this.languageService = languageService;
    }

    @PostConstruct
    public void init() {
        /** saving English and Macedonian language on startup */
        languageService.saveNewLanguage("English");
        languageService.saveNewLanguage("Macedonian");
    }

    @Override
    public void loadInitialFiles() throws IOException {
        String pathEn = "src/main/resources/data/AllTournamentsIDsEN.csv";
        String pathMk = "src/main/resources/data/AllTournamentsIDsMA.csv";

        MultipartFile multipartFileEn = new MockMultipartFile("test.csv", new FileInputStream(new File(pathEn)));
        loadAndImportFile(multipartFileEn,1);

        MultipartFile multipartFileMk = new MockMultipartFile("test.csv", new FileInputStream(new File(pathMk)));
        loadAndImportFile(multipartFileMk,2);

    }

    @Override
    public void save(MultipartFile file,Integer languageId) throws Exception {
        if(!languageService.isValidLanguageId(languageId)){
            throw new InvalidLanguageException(languageId);
        }
        if(TYPE.equals(file.getContentType())){
            loadAndImportFile(file,languageId);
        }else{
            throw new InvalidFileTypeException();
        }
    }

    private void loadAndImportFile(MultipartFile file,Integer languageId){
        HashMap<Integer,Sport> sportMap = new HashMap<>();
        HashMap<Integer,Country> countryMap = new HashMap<>();
        HashMap<Integer,League> leagueMap = new HashMap<>();
        HashMap<Integer,Team> teamMap = new HashMap<>();

        try(BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))){
            String line = br.readLine();
            while ((line = br.readLine()) != null){
                try {
                    String[] values = line.split(";");
                    Integer sportId = Integer.parseInt(values[0]);
                    Integer countryId = Integer.parseInt(values[1]);
                    String sportName = values[4].replace("\"","");
                    String countryName = values[5].replace("\"","");
                    Sport sport = new Sport(sportId,languageId,sportName);
                    sportMap.put(sportId,sport);
                    Country country = new Country(countryId,languageId,countryName,languageId,sportId);
                    countryMap.put(countryId,country);
                    if(!values[2].equals("") && !values[6].equals("")){
                        Integer leagueId = Integer.parseInt(values[2]);
                        String leagueName = values[6].replace("\"","");
                        League league = new League(leagueId,languageId,leagueName,languageId,countryId);
                        leagueMap.put(leagueId,league);
                    }
                    if(values.length == 11){
                        Integer teamId = Integer.parseInt(values[8]);
                        String teamName = values[9].replace("\"","");
                        Team team = new Team(teamId,languageId,teamName,languageId);
                        if(!values[2].equals("")){
                            team.setLeagueId(Integer.parseInt(values[2]));
                        }
                        teamMap.put(teamId,team);
                    }

                }catch (Exception e){
                    LOG.error("Exception while parsing! " + line);
                }
            }
        }catch (IOException e){
            LOG.warn(e.getMessage());
        }
        /** processing the data in 4 separate threads for asynchronous saving */
        DataProcessor processor1 = new DataProcessor();
        processor1.setSportMap(sportMap);
        processor1.setSportService(sportService);
        executorService.execute(processor1);

        DataProcessor processor2 = new DataProcessor();
        processor2.setCountryMap(countryMap);
        processor2.setCountryService(countryService);
        executorService.execute(processor2);

        DataProcessor processor3 = new DataProcessor();
        processor3.setLeagueMap(leagueMap);
        processor3.setLeagueService(leagueService);
        executorService.execute(processor3);

        DataProcessor processor4 = new DataProcessor();
        processor4.setTeamMap(teamMap);
        processor4.setTeamService(teamService);
        executorService.execute(processor4);

    }

}
