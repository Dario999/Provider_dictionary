package uk.singular.dfs.provider.sandbox.dictionary.repository.paginated;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import uk.singular.dfs.provider.sandbox.dictionary.model.Sport;
import uk.singular.dfs.provider.sandbox.dictionary.model.Team;
import uk.singular.dfs.provider.sandbox.dictionary.model.primarykeys.SportId;

@Repository
public interface SportPageableRepository extends PagingAndSortingRepository<Sport, SportId> {

    Page<Sport> findAllByLanguage(Integer language,Pageable pageable);

    @Query(value = "SELECT * FROM sandbox_dictionary.sport order by sport_id",nativeQuery = true)
    Page<Sport> findAllSportsOrderBySportId(Pageable pageable);

}
