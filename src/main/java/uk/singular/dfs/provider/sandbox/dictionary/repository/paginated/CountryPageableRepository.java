package uk.singular.dfs.provider.sandbox.dictionary.repository.paginated;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import uk.singular.dfs.provider.sandbox.dictionary.model.Country;
import uk.singular.dfs.provider.sandbox.dictionary.model.League;
import uk.singular.dfs.provider.sandbox.dictionary.model.primarykeys.CountryId;

@Repository
public interface CountryPageableRepository extends PagingAndSortingRepository<Country, CountryId> {

    Page<Country> findAllByLanguage(Integer language,Pageable pageable);

    @Query(value = "SELECT * FROM sandbox_dictionary.country order by country_id",nativeQuery = true)
    Page<Country> findAllCountriesOrderByCountryId(Pageable pageable);

}
