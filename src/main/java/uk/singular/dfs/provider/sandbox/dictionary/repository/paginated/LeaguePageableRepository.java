package uk.singular.dfs.provider.sandbox.dictionary.repository.paginated;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import uk.singular.dfs.provider.sandbox.dictionary.model.League;
import uk.singular.dfs.provider.sandbox.dictionary.model.Team;
import uk.singular.dfs.provider.sandbox.dictionary.model.primarykeys.LeagueId;

@Repository
public interface LeaguePageableRepository extends PagingAndSortingRepository<League, LeagueId> {

    Page<League> findAllByLanguage(Integer language, Pageable pageable);

    @Query(value = "SELECT * FROM sandbox_dictionary.league order by league_id",nativeQuery = true)
    Page<League> findAllLeaguesOrderByLeagueId(Pageable pageable);

}
