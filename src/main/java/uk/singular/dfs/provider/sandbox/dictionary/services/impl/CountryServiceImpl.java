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
import uk.singular.dfs.provider.sandbox.dictionary.model.primarykeys.CountryId;
import uk.singular.dfs.provider.sandbox.dictionary.repository.CountryRepository;
import uk.singular.dfs.provider.sandbox.dictionary.repository.paginated.CountryPageableRepository;
import uk.singular.dfs.provider.sandbox.dictionary.services.CountryService;
import uk.singular.dfs.provider.sandbox.dictionary.services.LanguageService;
import uk.singular.dfs.provider.sandbox.dictionary.services.SportService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class CountryServiceImpl implements CountryService {

    private final CountryRepository countryRepository;
    private final CountryPageableRepository countryPageableRepository;
    private LanguageService languageService;
    private SportService sportService;

    private static final Logger LOG = LoggerFactory.getLogger(CountryServiceImpl.class);

    public CountryServiceImpl(CountryRepository countryRepository, CountryPageableRepository countryPageableRepository) {
        this.countryRepository = countryRepository;
        this.countryPageableRepository = countryPageableRepository;
    }

    /** using setter based di for SportService cause it
     *  is easier to inject bean while testing */
    @Autowired
    public void setSportService(SportService sportService){
        this.sportService = sportService;
    }

    @Autowired
    public void setLanguageService(LanguageService languageService){
        this.languageService = languageService;
    }

    @Override
    public Page<Country> findAllPageable(Pageable pageRequest) {
        Integer numOfLanguages = languageService.numberOfLanguages();
        Pageable pageable = PageRequest.of(pageRequest.getPageNumber(), pageRequest.getPageSize() * numOfLanguages);
        /** multiplying the page size with the number of languages because we need to
         *  list one entity per language.So if i have 3 languages and page size 10 I will
         *  read 30 rows. */
        Page<Country> countries = countryPageableRepository.findAllCountriesOrderByCountryId(pageable);
        return transformForAllLanguagesPageable(countries,pageRequest);
    }

    @Override
    public Page<Country> findAllByLanguagePageable(Pageable pageRequest,Integer languageId) throws InvalidLanguageException {
        if(!languageService.isValidLanguageId(languageId)){
            LOG.error("Invalid language id: " + languageId);
            throw new InvalidLanguageException(languageId);
        }
        return countryPageableRepository.findAllByLanguage(languageId,pageRequest);
    }

    @Override
    public List<Country> findAllBySportId(Integer id) {
        List<Country> countries = countryRepository.findAllBySportId(id);
        return transformForAllLanguages(countries);
    }

    @Override
    public List<Country> findAllBySportIdAndLanguage(Integer id, Integer languageId) throws InvalidLanguageException {
        if (!languageService.isValidLanguageId(languageId)) {
            LOG.error("Invalid language id: " + languageId);
            throw new InvalidLanguageException(languageId);
        }
        return countryRepository.findAllBySportIdAndLanguage(id,languageId);
    }

    /** transforming list of separate countries with one language to list
     *  of countries with all languages in one entity */
    private List<Country> transformForAllLanguages(List<Country> countries){
        Map<Integer,List<Country>> countriesMap = countries.stream().collect(Collectors.groupingBy(Country::getCountryId));
        return transformMapToList(countriesMap);
    }

    /** using this method to group list of entities by entity id */
    private Page<Country> transformForAllLanguagesPageable(Page<Country> page,Pageable pageRequest){
        Map<Integer,List<Country>> countriesMap = page.getContent().stream().collect(Collectors.groupingBy(Country::getCountryId));
        List<Country> toReturn = transformMapToList(countriesMap);
        return new PageImpl<Country>(toReturn,pageRequest,toReturn.size());
    }

    /** using this method to transform already grouped map by country id
     *  to list of entities with all languages in one */
    private List<Country> transformMapToList(Map<Integer,List<Country>> countriesMap){
        List<Country> toReturn = new ArrayList<>();
        for(Integer id : countriesMap.keySet()){
            Country country = new Country();
            country.setCountryId(id);
            Map<Integer,String> names = countriesMap.get(id).stream().collect(Collectors.toMap(Country::getLanguage,x->x.getName()));
            country.setNames(names);
            toReturn.add(country);
        }
        return toReturn;
    }

    @Override
    public Country findCountryByCountryId(Integer id) throws ResourceNotFoundException {
        Country country = new Country();
        country.setCountryId(id);
        Map<Integer,String> names = findAllByCountryId(id).stream().collect(Collectors.toMap(Country::getLanguage, x -> x.getName()));
        if(names.isEmpty()){
            LOG.error("Country with id: " + id + " not found!");
            throw new ResourceNotFoundException("Country with id: " + id + " not found!");
        }
        country.setNames(names);
        return country;
    }

    @Override
    public Country findCountryByCountryIdAndLanguage(Integer id, Integer languageId) throws Exception {
        if (!languageService.isValidLanguageId(languageId)) {
            LOG.error("Invalid language id: " + languageId);
            throw new InvalidLanguageException(languageId);
        }
        CountryId countryId = new CountryId(id,languageId);
        Country country = countryRepository.findById(countryId).orElse(null);
        if(country == null){
            LOG.error("Country with id: " + id + " and language id: " + languageId + " not found!");
            throw new ResourceNotFoundException("Country with id: " + id + " and language id: " + languageId + " not found!");
        }
        return country;
    }

    @Override
    public Country save(Country country) throws Exception {
        if(country.getCountryId() == null ||  country.getName() == null || country.getName().isEmpty()
            || country.getSportId() == null || country.getFkLanguage() == null){
            StringBuilder sb = new StringBuilder();
            sb.append("{\n" +
                    "    \"countryId\": 1,\n" +
                    "    \"name\": \"Macedonia\",\n" +
                    "    \"fkLanguage\": 1,\n" +
                    "    \"sportId\": 1\n" +
                    "}");
            LOG.error("Invalid data input for country entity!");
            throw new InvalidDataInputException("Invalid data for country entity.Please enter in this format:\n" + sb.toString());
        }
        /** checking if sport with fkLanguage and sportId exists
         *  if it does not exist this function throws ResourceNotFoundException */
        sportService.findSportBySportIdAndLanguage(country.getSportId(),country.getFkLanguage());
        LOG.info("Country entity saved with id: " + country.getCountryId());
        return countryRepository.save(country);
    }

    @Override
    public List<Country> saveAll(Iterable<Country> countries) {
        return countryRepository.saveAll(countries);
    }

    @Override
    public void update(Country country,Integer countryId,Integer languageId) throws Exception {
        if(!languageService.isValidLanguageId(languageId)){
            LOG.error("Invalid language id: " + languageId);
            throw new InvalidLanguageException(languageId);
        }
        CountryId id = new CountryId(countryId,languageId);
        Country countryInDb = countryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Country with id: " + countryId + " not found!"));
        if(country.getName() != null && !country.getName().isEmpty()) {
            countryInDb.setName(country.getName());
        }
        LOG.info("Country with id: " + countryId + " updated");
        countryRepository.save(countryInDb);
    }

    @Override
    public Integer delete(Integer id) {
        Integer numberOfRows = countryRepository.deleteAllByCountryId(id);
        LOG.info("Delete country with id: " + id + ".Number of rows deleted: " + numberOfRows);
        return numberOfRows;
    }

    private List<Country> findAllByCountryId(Integer id){
        return countryRepository.findAllByCountryId(id);
    }

}
