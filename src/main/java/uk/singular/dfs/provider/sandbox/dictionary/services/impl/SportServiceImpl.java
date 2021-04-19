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
import uk.singular.dfs.provider.sandbox.dictionary.model.Sport;
import uk.singular.dfs.provider.sandbox.dictionary.model.primarykeys.SportId;
import uk.singular.dfs.provider.sandbox.dictionary.repository.SportRepository;
import uk.singular.dfs.provider.sandbox.dictionary.repository.paginated.SportPageableRepository;
import uk.singular.dfs.provider.sandbox.dictionary.services.LanguageService;
import uk.singular.dfs.provider.sandbox.dictionary.services.SportService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SportServiceImpl implements SportService {

    private final SportRepository sportRepository;
    private final SportPageableRepository sportPageableRepository;
    private LanguageService languageService;

    private static final Logger LOG = LoggerFactory.getLogger(SportServiceImpl.class);

    public SportServiceImpl(SportRepository sportRepository, SportPageableRepository sportPageableRepository, LanguageService languageService) {
        this.sportRepository = sportRepository;
        this.sportPageableRepository = sportPageableRepository;
        this.languageService = languageService;
    }

    @Autowired
    public void setLanguageService(LanguageService languageService) {
        this.languageService = languageService;
    }

    @Override
    public Page<Sport> findAllPageable(Pageable pageRequest) {
        Integer numOfLanguages = languageService.numberOfLanguages();
        Pageable pageable = PageRequest.of(pageRequest.getPageNumber(), pageRequest.getPageSize() * numOfLanguages);
        /** multiplying the page size with the number of languages because we need to
         *  list one entity per language.So if i have 3 languages and page size 10 i will
         *  read 30 rows. */
        Page<Sport> sports = sportPageableRepository.findAllSportsOrderBySportId(pageable);
        return transformForAllLanguagesPageable(sports,pageRequest);
    }

    @Override
    public Page<Sport> findAllByLanguagePageable(Pageable pageRequest,Integer languageId) throws InvalidLanguageException {
        if(!languageService.isValidLanguageId(languageId)){
            LOG.error("Invalid language id: " + languageId);
            throw new InvalidLanguageException(languageId);
        }
        return sportPageableRepository.findAllByLanguage(languageId,pageRequest);
    }

    @Override
    public Sport findSportBySportId(Integer id) throws ResourceNotFoundException {
        Sport sport = new Sport();
        sport.setSportId(id);
        Map<Integer,String> names = findAllBySportId(id).stream().collect(Collectors.toMap(Sport::getLanguage, x -> x.getName()));
        if(names.isEmpty()){
            LOG.error("Sport with id: " + id + " not found!");
            throw new ResourceNotFoundException("Sport with id: " + id + " not found!");
        }
        sport.setNames(names);
        return sport;
    }

    /** transforming list of separate sports with one language to list
     *  of sports with all languages in one entity */
    private List<Sport> transformForAllLanguages(List<Sport> sports){
        Map<Integer,List<Sport>> sportsMap = sports.stream().collect(Collectors.groupingBy(Sport::getSportId));
        return transformMapToList(sportsMap);
    }

    /** using this method to group list of entities by entity id */
    private Page<Sport> transformForAllLanguagesPageable(Page<Sport> page,Pageable pageRequest){
        Map<Integer,List<Sport>> sportsMap = page.getContent().stream().collect(Collectors.groupingBy(Sport::getSportId));
        List<Sport> toReturn = transformMapToList(sportsMap);
        return new PageImpl<Sport>(toReturn,pageRequest,toReturn.size());
    }

    /** using this method to transform already grouped map by sport id
     *  to list of entities with all languages in one */
    private List<Sport> transformMapToList(Map<Integer,List<Sport>> leaguesMap){
        List<Sport> toReturn = new ArrayList<>();
        for(Integer id : leaguesMap.keySet()){
            Sport sport = new Sport();
            sport.setSportId(id);
            Map<Integer,String> names = leaguesMap.get(id).stream().collect(Collectors.toMap(Sport::getLanguage,x->x.getName()));
            sport.setNames(names);
            toReturn.add(sport);
        }
        return toReturn;
    }

    @Override
    public Sport findSportBySportIdAndLanguage(Integer id, Integer languageId) throws Exception {
        if (!languageService.isValidLanguageId(languageId)) {
            LOG.error("Invalid language id: " + languageId);
            throw new InvalidLanguageException(languageId);
        }
        SportId sportId = new SportId(id,languageId);
        Sport sport = sportRepository.findById(sportId).orElse(null);
        if(sport == null){
            LOG.warn("Sport with id: " + id + " and language: " + languageId + " not found!");
            throw new ResourceNotFoundException("Sport with id: " + id + " and language id: " + languageId + " not found!");
        }
        return sport;
    }

    private List<Sport> findAllBySportId(Integer id){
        return sportRepository.findAllBySportId(id);
    }

    @Override
    public Sport save(Sport sport) throws InvalidDataInputException {
        if(sport.getSportId() == null  || sport.getName() == null || sport.getName().isEmpty()){
            StringBuilder sb = new StringBuilder();
            sb.append("{\n" +
                    "    \"sportId\": 1,\n" +
                    "    \"name\": \"Football\"\n" +
                    "}");
            LOG.error("Invalid data input for sport entity!");
            throw new InvalidDataInputException("Invalid data for sport entity.Please enter in this format:\n" + sb.toString());
        }
        LOG.info("Sport entity saved with id: " + sport.getSportId());
        return sportRepository.save(sport);
    }

    @Override
    public List<Sport> saveAll(Iterable<Sport> sports) {
        return sportRepository.saveAll(sports);
    }

    @Override
    public void update(Sport sport,Integer sportId,Integer languageId) throws Exception {
        if(!languageService.isValidLanguageId(languageId)){
            LOG.error("Invalid language id: " + languageId);
            throw new InvalidLanguageException(languageId);
        }
        SportId id = new SportId(sportId,languageId);
        Sport sportInDb = sportRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Sport with id: " + sportId + " not found!"));
        if(sport.getName() != null && !sport.getName().isEmpty()) {
            sportInDb.setName(sport.getName());
        }
        LOG.info("Sport with id: " + sportId + " updated");
        sportRepository.save(sportInDb);
    }

    @Override
    public Integer delete(Integer id) {
        Integer numberOfRows = sportRepository.deleteAllBySportId(id);
        LOG.info("Delete sports with id: " + id + ".Number of rows deleted: " + numberOfRows);
        return numberOfRows;
    }

}
