package uk.singular.dfs.provider.sandbox.dictionary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uk.singular.dfs.provider.sandbox.dictionary.model.Team;
import uk.singular.dfs.provider.sandbox.dictionary.model.primarykeys.TeamId;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface TeamRepository extends JpaRepository<Team, TeamId> {

    Team findTeamByTeamId(Integer id);

    @Transactional
    Integer deleteAllByTeamId(Integer id);

    List<Team> findAllByTeamId(Integer id);

    List<Team> findAllByLanguage(Integer language);

    @Query(value = "SELECT * FROM team WHERE fk_league_id = ?1",nativeQuery = true)
    List<Team> findAllByLeagueId(Integer id);

    /** find all teams that play sport with id */
    @Query(value = "SELECT t.* FROM sport s\n" +
            "JOIN country c\n" +
            "ON s.sport_id = c.fk_sport_id AND s.language = c.fk_language\n" +
            "JOIN league l \n" +
            "ON c.country_id = l.fk_country_id AND c.language = l.fk_language\n" +
            "JOIN team t\n" +
            "ON l.league_id = t.fk_league_id AND l.language = t.fk_language\n" +
            "WHERE s.sport_id = ?1",nativeQuery = true)
    List<Team> findAllThatPlaySport(Integer sportId);

    // WITHOUT NATIVE QUERY
    @Query(value = "SELECT t FROM Sport s\n" +
            "JOIN Country c\n" +
            "ON s.sportId = c.sportId AND s.language = c.fkLanguage\n" +
            "JOIN League l \n" +
            "ON c.countryId = l.countryId AND c.language = l.fkLanguage\n" +
            "JOIN Team t\n" +
            "ON l.leagueId = t.leagueId AND l.language = t.fkLanguage\n" +
            "WHERE s.sportId = ?1")
    List<Team> findAllThatPlaySportNonNative(Integer sportId);

    /** FIND ALL TEAM THAT PLAY SPORT IN COUNTRY BY ID AND LANGUAGE */
    @Query(value = "SELECT t FROM Sport s\n" +
            "JOIN Country c\n" +
            "ON s.sportId = c.sportId AND s.language = c.fkLanguage\n" +
            "JOIN League l \n" +
            "ON c.countryId = l.countryId AND c.language = l.fkLanguage\n" +
            "JOIN Team t\n" +
            "ON l.leagueId = t.leagueId AND l.language = t.fkLanguage\n" +
            "WHERE s.sportId = ?1 AND t.language = ?2")
    List<Team> findAllThatPlaySportByLanguage(Integer sportId, Integer language);

    List<Team> findAllByLeagueIdAndLanguage(Integer leagueId,Integer languages);

}
