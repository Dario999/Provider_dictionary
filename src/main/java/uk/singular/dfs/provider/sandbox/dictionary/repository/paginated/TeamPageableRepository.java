package uk.singular.dfs.provider.sandbox.dictionary.repository.paginated;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import uk.singular.dfs.provider.sandbox.dictionary.model.Team;
import uk.singular.dfs.provider.sandbox.dictionary.model.primarykeys.TeamId;

@Repository
public interface TeamPageableRepository extends PagingAndSortingRepository<Team, TeamId> {

    Page<Team> findAllByLanguage(Integer language,Pageable pageable);

    @Query(value = "SELECT * FROM sandbox_dictionary.team order by team_id",nativeQuery = true)
    Page<Team> findAllTeamsOrderByTeamId(Pageable pageable);

}
