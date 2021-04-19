package uk.singular.dfs.provider.sandbox.dictionary.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uk.singular.dfs.provider.sandbox.dictionary.exceptions.InvalidLanguageException;
import uk.singular.dfs.provider.sandbox.dictionary.exceptions.ResourceNotFoundException;
import uk.singular.dfs.provider.sandbox.dictionary.model.Team;

import java.util.List;

public interface TeamService {

    /** find all teams pageable */
    Page<Team> findAllPageable(Pageable pageRequest);

    /** find all teams by language pageable */
    Page<Team> findAllByLanguagePageable(Pageable pageRequest, Integer languageId) throws InvalidLanguageException;

    /** find all teams that play sport with sport_id */
    List<Team> findAllThatPlaySport(Integer sportId);

    /** find all teams that play sport with sport_id by language */
    List<Team> findAllThatPlaySportByLanguage(Integer sportId, Integer languageId) throws InvalidLanguageException;

    /** find all teams that play in league by league_id */
    List<Team> findAllThatPlayInLeague(Integer leagueId);

    /** find all teams that play in league by league_id and language */
    List<Team> findAllThatPlayInLeagueByLanguage(Integer leagueId,Integer languageId) throws InvalidLanguageException;

    /** find all by team_id id */
    Team findTeamByTeamId(Integer id) throws ResourceNotFoundException;

    /** find all by team_id id and language */
    Team findTeamByTeamIdAndLanguage(Integer id, Integer languageId) throws Exception;

    Team save(Team team) throws Exception;

    List<Team> saveAll(Iterable<Team> teams);

    void update(Team team,Integer teamId,Integer languageId) throws Exception;

    Integer delete(Integer id);

}
