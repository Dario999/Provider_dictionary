package uk.singular.dfs.provider.sandbox.dictionary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uk.singular.dfs.provider.sandbox.dictionary.model.Country;
import uk.singular.dfs.provider.sandbox.dictionary.model.primarykeys.CountryId;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface CountryRepository extends JpaRepository<Country, CountryId> {

    Country findCountryByCountryId(Integer id);

    @Transactional
    Integer deleteAllByCountryId(Integer id);

    List<Country> findAllByCountryId(Integer id);

    List<Country> findAllByLanguage(Integer language);

    @Query(value = "SELECT * FROM country WHERE fk_sport_id = ?1",nativeQuery = true)
    List<Country> findAllBySportId(Integer id);

    List<Country> findAllBySportIdAndLanguage(Integer id,Integer language);

}
