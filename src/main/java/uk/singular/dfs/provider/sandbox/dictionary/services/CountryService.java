package uk.singular.dfs.provider.sandbox.dictionary.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uk.singular.dfs.provider.sandbox.dictionary.exceptions.InvalidLanguageException;
import uk.singular.dfs.provider.sandbox.dictionary.exceptions.ResourceNotFoundException;
import uk.singular.dfs.provider.sandbox.dictionary.model.Country;

import java.util.List;

public interface CountryService {

    /** find all countries pageable */
    Page<Country> findAllPageable(Pageable pageRequest);

    /** find all countries by language pageable */
    Page<Country> findAllByLanguagePageable(Pageable pageRequest, Integer languageId) throws InvalidLanguageException;

    /** find all countries by sport id */
    List<Country> findAllBySportId(Integer id);

    /** find all countries by sport id and language*/
    List<Country> findAllBySportIdAndLanguage(Integer id,Integer languageId) throws InvalidLanguageException;

    Country findCountryByCountryId(Integer id) throws ResourceNotFoundException;

    Country findCountryByCountryIdAndLanguage(Integer id, Integer languageId) throws Exception;

    Country save(Country country) throws Exception;

    List<Country> saveAll(Iterable<Country> countries);

    void update(Country country,Integer countryId,Integer languageId) throws Exception;

    Integer delete(Integer id);

}
