package uk.singular.dfs.provider.sandbox.dictionary.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uk.singular.dfs.provider.sandbox.dictionary.exceptions.InvalidDataInputException;
import uk.singular.dfs.provider.sandbox.dictionary.exceptions.InvalidLanguageException;
import uk.singular.dfs.provider.sandbox.dictionary.exceptions.ResourceNotFoundException;
import uk.singular.dfs.provider.sandbox.dictionary.model.Team;
import uk.singular.dfs.provider.sandbox.dictionary.model.primarykeys.TeamId;
import uk.singular.dfs.provider.sandbox.dictionary.repository.TeamRepository;
import uk.singular.dfs.provider.sandbox.dictionary.repository.paginated.TeamPageableRepository;
import uk.singular.dfs.provider.sandbox.dictionary.services.LanguageService;
import uk.singular.dfs.provider.sandbox.dictionary.services.LeagueService;
import uk.singular.dfs.provider.sandbox.dictionary.services.TeamService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final TeamPageableRepository teamPageableRepository;
    private LanguageService languageService;
    private LeagueService leagueService;

    private static final Logger LOG = LoggerFactory.getLogger(TeamServiceImpl.class);

    public TeamServiceImpl(TeamRepository teamRepository, TeamPageableRepository teamPageableRepository) {
        this.teamRepository = teamRepository;
        this.teamPageableRepository = teamPageableRepository;
    }

    @Autowired
    public void setLeagueService(LeagueService leagueService){
        this.leagueService = leagueService;
    }

    @Autowired
    public void setLanguageService(LanguageService languageService){
        this.languageService = languageService;
    }

    @Override
    public Page<Team> findAllPageable(Pageable pageRequest) {
        Integer numOfLanguages = languageService.numberOfLanguages();
        Pageable pageable = PageRequest.of(pageRequest.getPageNumber(), pageRequest.getPageSize() * numOfLanguages);
        /** multiplying the page size with the number of languages because we need to
         *  list one entity per language.So if i have 3 languages and page size 10 i will
         *  read 30 rows. */
        Page<Team> teams = teamPageableRepository.findAllTeamsOrderByTeamId(pageable);
        return transformForAllLanguagesPageable(teams,pageRequest);
   }

    @Override
    public Page<Team> findAllByLanguagePageable(Pageable pageRequest, Integer languageId) throws InvalidLanguageException {
        if(!languageService.isValidLanguageId(languageId)){
            LOG.error("Invalid language id: " + languageId);
            throw new InvalidLanguageException(languageId);
        }
        return teamPageableRepository.findAllByLanguage(languageId,pageRequest);
    }

    @Override
    public List<Team> findAllThatPlaySport(Integer sportId) {
        /** this should be pageable but i didn't had time to repair it  */
        List<Team> teams = teamRepository.findAllThatPlaySport(sportId);
        return transformForAllLanguages(teams);
    }

    @Override
    public List<Team> findAllThatPlayInLeague(Integer leagueId) {
        List<Team> teams = teamRepository.findAllByLeagueId(leagueId);
        return transformForAllLanguages(teams);
    }

    @Override
    public List<Team> findAllThatPlayInLeagueByLanguage(Integer leagueId, Integer languageId) throws InvalidLanguageException {
        if(!languageService.isValidLanguageId(languageId)){
            LOG.error("Invalid language: " + languageId);
            throw new InvalidLanguageException(languageId);
        }
        return teamRepository.findAllByLeagueIdAndLanguage(leagueId,languageId);
    }

    @Override
    public List<Team> findAllThatPlaySportByLanguage(Integer sportId,Integer languageId) throws InvalidLanguageException {
        if(!languageService.isValidLanguageId(languageId)){
            LOG.error("Invalid language id: " + languageId);
            throw new InvalidLanguageException(languageId);
        }
        /** this should be pageable but i didn't had time to repair it  */
        return teamRepository.findAllThatPlaySportByLanguage(sportId,languageId);
    }

    /** transforming list of separate teams with one language to list
     *  of teams with all languages in one entity */
    private List<Team> transformForAllLanguages(List<Team> teams){
        Map<Integer,List<Team>> teamsMap = teams.stream().collect(Collectors.groupingBy(Team::getTeamId));
        return transformMapToList(teamsMap);
    }

    /** using this method to group list of entities by entity id */
    private Page<Team> transformForAllLanguagesPageable(Page<Team> page,Pageable pageRequest){
        Map<Integer,List<Team>> teamsMap = page.getContent().stream().collect(Collectors.groupingBy(Team::getTeamId));
        List<Team> toReturn = transformMapToList(teamsMap);
        return new PageImpl<Team>(toReturn,pageRequest,toReturn.size());
    }

    /** using this method to transform already grouped map by team id
     *  to list of entities with all languages in one */
    private List<Team> transformMapToList(Map<Integer,List<Team>> teamsMap){
        List<Team> toReturn = new ArrayList<>();
        for(Integer id : teamsMap.keySet()){
            Team team = new Team();
            team.setTeamId(id);
            Map<Integer,String> names = teamsMap.get(id).stream().collect(Collectors.toMap(Team::getLanguage,x->x.getName()));
            team.setNames(names);
            toReturn.add(team);
        }
        return toReturn;
    }

    @Override
    public Team findTeamByTeamId(Integer id) throws ResourceNotFoundException {
        Team team = new Team();
        team.setTeamId(id);
        Map<Integer,String> names = findAllByTeamId(id).stream().collect(Collectors.toMap(Team::getLanguage,x->x.getName()));
        if(names.isEmpty()){
            LOG.error("Team with id: " + id + " not found!");
            throw new ResourceNotFoundException("Team with id: " + id + " not found!");
        }
        team.setNames(names);
        return team;
    }

    @Override
    public Team findTeamByTeamIdAndLanguage(Integer id, Integer languageId) throws Exception {
        if (!languageService.isValidLanguageId(languageId)) {
            LOG.error("Invalid language id: " + languageId);
            throw new InvalidLanguageException(languageId);
        }
        TeamId teamId = new TeamId(id,languageId);
        Team team = teamRepository.findById(teamId).orElse(null);
        if(team == null){
            throw new ResourceNotFoundException("Team with id: " + id + " and language id: " + languageId + " not found!");
        }
        return team;
    }

    @Override
    public Team save(Team team) throws Exception {
        if(team.getTeamId() == null  || team.getName() == null || team.getName().isEmpty()
                || team.getLeagueId() == null || team.getFkLanguage() == null){
            StringBuilder sb = new StringBuilder();
            sb.append("{\n" +
                    "    \"teamId\": 1,\n" +
                    "    \"name\": \"Team Macedonia\",\n" +
                    "    \"fkLanguage\": 1,\n" +
                    "    \"leagueId\": 1\n" +
                    "}");
            LOG.warn("Invalid data input for team entity!");
            throw new InvalidDataInputException("Invalid data for team entity.Please enter in this format:\n" + sb.toString());
        }
        leagueService.findLeagueByLeagueIdAndLanguage(team.getLeagueId(),team.getFkLanguage());
        LOG.info("Team entity saved with id: " + team.getTeamId());
        return teamRepository.save(team);
    }

    @Override
    public List<Team> saveAll(Iterable<Team> teams) {
        return teamRepository.saveAll(teams);
    }

    @Override
    public void update(Team team,Integer teamId,Integer languageId) throws Exception {
        if(!languageService.isValidLanguageId(languageId)){
            LOG.error("Invalid language id: " + languageId);
            throw new InvalidLanguageException(languageId);
        }
        TeamId id = new TeamId(teamId,languageId);
        Team teamInDb = teamRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Team with id: " + teamId + " not found!"));
        if(team.getName() != null && !team.getName().isEmpty()) {
            teamInDb.setName(team.getName());
        }
        LOG.info("Team with id: " + teamId + " updated");
        teamRepository.save(teamInDb);
    }

    @Override
    public Integer delete(Integer id) {
        Integer numberOfRows = teamRepository.deleteAllByTeamId(id);
        LOG.info("Delete teams with id: " + id + ".Number of rows deleted: " + numberOfRows);
        return numberOfRows;
    }

    private List<Team> findAllByTeamId(Integer id){
        return teamRepository.findAllByTeamId(id);
    }

}
