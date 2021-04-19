package uk.singular.dfs.provider.sandbox.dictionary.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uk.singular.dfs.provider.sandbox.dictionary.exceptions.InvalidDataInputException;
import uk.singular.dfs.provider.sandbox.dictionary.exceptions.InvalidLanguageException;
import uk.singular.dfs.provider.sandbox.dictionary.exceptions.ResourceNotFoundException;
import uk.singular.dfs.provider.sandbox.dictionary.model.Sport;

import java.util.List;

public interface SportService {

    /** find all sports pageable */
    Page<Sport> findAllPageable(Pageable pageRequest);

    /** find all sports by language pageable */
    Page<Sport> findAllByLanguagePageable(Pageable pageRequest,Integer language) throws InvalidLanguageException;

    /** find all sports by sport_id id */
    Sport findSportBySportId(Integer id) throws ResourceNotFoundException;

    /** find all sports by sport_id id and language */
    Sport findSportBySportIdAndLanguage(Integer id, Integer language) throws Exception;

    Sport save(Sport sport) throws InvalidDataInputException;

    List<Sport> saveAll(Iterable<Sport> sports);

    void update(Sport sport,Integer sportId,Integer language) throws Exception;

    Integer delete(Integer id);

}
