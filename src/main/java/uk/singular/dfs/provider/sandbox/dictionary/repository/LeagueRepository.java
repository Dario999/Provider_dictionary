package uk.singular.dfs.provider.sandbox.dictionary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uk.singular.dfs.provider.sandbox.dictionary.model.League;
import uk.singular.dfs.provider.sandbox.dictionary.model.primarykeys.LeagueId;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface LeagueRepository extends JpaRepository<League, LeagueId> {

    League findLeagueByLeagueId(Integer id);

    @Transactional
    Integer deleteAllByLeagueId(Integer id);

    List<League> findAllByLeagueId(Integer id);

    List<League> findAllByLanguage(Integer language);

    @Query(value = "SELECT * FROM league WHERE fk_country_id = ?1",nativeQuery = true)
    List<League> findAllByCountryId(Integer id);

    List<League> findAllByCountryIdAndLanguage(Integer id,Integer languagesEnum);

}
