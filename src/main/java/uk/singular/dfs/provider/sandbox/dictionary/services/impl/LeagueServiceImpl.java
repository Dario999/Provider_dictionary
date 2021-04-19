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
import uk.singular.dfs.provider.sandbox.dictionary.model.Country;
import uk.singular.dfs.provider.sandbox.dictionary.model.League;
import uk.singular.dfs.provider.sandbox.dictionary.model.primarykeys.LeagueId;
import uk.singular.dfs.provider.sandbox.dictionary.repository.LeagueRepository;
import uk.singular.dfs.provider.sandbox.dictionary.repository.paginated.LeaguePageableRepository;
import uk.singular.dfs.provider.sandbox.dictionary.services.CountryService;
import uk.singular.dfs.provider.sandbox.dictionary.services.LanguageService;
import uk.singular.dfs.provider.sandbox.dictionary.services.LeagueService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LeagueServiceImpl implements LeagueService {

    private final LeagueRepository leagueRepository;
    private final LeaguePageableRepository leaguePageableRepository;
    private LanguageService languageService;
    private CountryService countryService;

    private static final Logger LOG = LoggerFactory.getLogger(LeagueServiceImpl.class);

    public LeagueServiceImpl(LeagueRepository leagueRepository, LeaguePageableRepository leaguePageableRepository) {
        this.leagueRepository = leagueRepository;
        this.leaguePageableRepository = leaguePageableRepository;
    }

    @Autowired
    public void setCountryService(CountryService countryService){
        this.countryService = countryService;
    }

    @Autowired
    public void setLanguageService(LanguageService languageService){
        this.languageService = languageService;
    }

    @Override
    public Page<League> findAllPageable(Pageable pageRequest) {
        Integer numOfLanguages = languageService.numberOfLanguages();
        Pageable pageable = PageRequest.of(pageRequest.getPageNumber(), pageRequest.getPageSize() * numOfLanguages);
        /** multiplying the page size with the number of languages because we need to
         *  list one entity per language.So if i have 3 languages and page size 10 i will
         *  read 30 rows. */
        Page<League> leagues = leaguePageableRepository.findAllLeaguesOrderByLeagueId(pageable);
        return transformForAllLanguagesPageable(leagues,pageRequest);
    }

    @Override
    public Page<League> findAllByLanguagePageable(Pageable pageRequest,Integer languageId) throws InvalidLanguageException {
        if(!languageService.isValidLanguageId(languageId)){
            LOG.error("Invalid language id: " + languageId);
            throw new InvalidLanguageException(languageId);
        }
        return leaguePageableRepository.findAllByLanguage(languageId,pageRequest);
    }

    @Override
    public List<League> findAllByCountryId(Integer id) {
        List<League> leagues = leagueRepository.findAllByCountryId(id);
        return transformForAllLanguages(leagues);
    }

    @Override
    public List<League> findAllByCountryIdAndLanguage(Integer id, Integer languageId) throws InvalidLanguageException {
        if(!languageService.isValidLanguageId(languageId)){
            LOG.error("Invalid language id: " + languageId);
            throw new InvalidLanguageException(languageId);
        }
        return leagueRepository.findAllByCountryIdAndLanguage(id,languageId);
    }

    /** transforming list of separate leagues with one language to list
     *  of leagues with all languages in one entity */
    private List<League> transformForAllLanguages(List<League> leagues){
        Map<Integer,List<League>> leaguesMap = leagues.stream().collect(Collectors.groupingBy(League::getLeagueId));
        return transformMapToList(leaguesMap);
    }

    /** using this method to group list of entities by entity id */
    private Page<League> transformForAllLanguagesPageable(Page<League> page,Pageable pageRequest){
        Map<Integer,List<League>> leaguesMap = page.getContent().stream().collect(Collectors.groupingBy(League::getLeagueId));
        List<League> toReturn = transformMapToList(leaguesMap);
        return new PageImpl<League>(toReturn,pageRequest,toReturn.size());
    }

    /** using this method to transform already grouped map by league id
     *  to list of entities with all languages in one */
    private List<League> transformMapToList(Map<Integer,List<League>> leaguesMap){
        List<League> toReturn = new ArrayList<>();
        for(Integer id : leaguesMap.keySet()){
            League league = new League();
            league.setLeagueId(id);
            Map<Integer,String> names = leaguesMap.get(id).stream().collect(Collectors.toMap(League::getLanguage,x->x.getName()));
            league.setNames(names);
            toReturn.add(league);
        }
        return toReturn;
    }

    @Override
    public League findLeagueByLeagueId(Integer id) throws ResourceNotFoundException {
        League league = new League();
        league.setLeagueId(id);
        Map<Integer,String> names = findAllByLeagueId(id).stream().collect(Collectors.toMap(League::getLanguage,x->x.getName()));
        if(names.isEmpty()){
            LOG.error("League with id: " + id + " not found!");
            throw new ResourceNotFoundException("League with id: " + id + " not found!");
        }
        league.setNames(names);
        return league;
    }

    @Override
    public League findLeagueByLeagueIdAndLanguage(Integer id, Integer languageId) throws Exception {
        if (!languageService.isValidLanguageId(languageId)) {
            LOG.error("Invalid language id: " + languageId);
            throw new InvalidLanguageException(languageId);
        }
        LeagueId leagueId = new LeagueId(id,languageId);
        League league = leagueRepository.findById(leagueId).orElse(null);
        if(league == null){
            LOG.error("League with id: " + id + " and language id: " + languageId + " not found!");
            throw new ResourceNotFoundException("League with id: " + id + " and language id: " + languageId + " not found!");
        }
        return league;
    }

    @Override
    public League save(League league) throws Exception {
        if(league.getLeagueId() == null  || league.getName() == null || league.getName().isEmpty()
            || league.getCountryId() == null || league.getFkLanguage() == null){
            StringBuilder sb = new StringBuilder();
            sb.append("{\n" +
                    "    \"leagueId\": 1,\n" +
                    "    \"name\": \"League1\",\n" +
                    "    \"fkLanguage\": 1,\n" +
                    "    \"countryId\": 1\n" +
                    "}\n");
            LOG.error("Invalid data input for league entity!");
            throw new InvalidDataInputException("Invalid data for league entity.Please enter in this format:\n" + sb.toString());
        }
        /** checking if country with fkLanguage and countryId exists
         *  if it does not exist this function throws ResourceNotFoundException */
        countryService.findCountryByCountryIdAndLanguage(league.getCountryId(),league.getFkLanguage());
        LOG.info("League entity saved with id: " + league.getLeagueId());
        return leagueRepository.save(league);
    }

    @Override
    public List<League> saveAll(Iterable<League> leagues) {
        return leagueRepository.saveAll(leagues);
    }

    @Override
    public void update(League league,Integer leagueId,Integer languageId) throws Exception{
        if(!languageService.isValidLanguageId(languageId)){
            LOG.error("Invalid language id: " + languageId);
            throw new InvalidLanguageException(languageId);
        }
        LeagueId id = new LeagueId(leagueId,languageId);
        League leagueInDb = leagueRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("League with id: " + leagueId + " not found!"));
        if(league.getName() != null && !league.getName().isEmpty()) {
            leagueInDb.setName(league.getName());
        }
        LOG.info("League with id: " + leagueId + " updated");
        leagueRepository.save(leagueInDb);
    }

    @Override
    public Integer delete(Integer id) {
        Integer numberOfRows = leagueRepository.deleteAllByLeagueId(id);
        LOG.info("Delete leagues with id: " + id + ".Number of rows deleted: " + numberOfRows);
        return numberOfRows;
    }

    private List<League> findAllByLeagueId(Integer id){
        return leagueRepository.findAllByLeagueId(id);
    }

}
