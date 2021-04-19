package uk.singular.dfs.provider.sandbox.dictionary.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uk.singular.dfs.provider.sandbox.dictionary.exceptions.InvalidLanguageException;
import uk.singular.dfs.provider.sandbox.dictionary.exceptions.ResourceNotFoundException;
import uk.singular.dfs.provider.sandbox.dictionary.model.League;

import java.util.List;

public interface LeagueService {

    /** find all leagues pageable */
    Page<League> findAllPageable(Pageable pageRequest);

    /** find all leagues by language pageable */
    Page<League> findAllByLanguagePageable(Pageable pageRequest, Integer languageId) throws InvalidLanguageException;

    /** find all leagues by country_id */
    List<League> findAllByCountryId(Integer id);

    /** find all leagues by country_id and language */
    List<League> findAllByCountryIdAndLanguage(Integer id,Integer languageId) throws InvalidLanguageException;

    /** find all by league_id */
    League findLeagueByLeagueId(Integer id) throws ResourceNotFoundException;

    /** find all by league_id and language */
    League findLeagueByLeagueIdAndLanguage(Integer id, Integer languageId) throws Exception;

    League save(League league) throws Exception;

    List<League> saveAll(Iterable<League> leagues);

    void update(League league,Integer leagueId,Integer languageId) throws Exception;

    Integer delete(Integer id);

}
