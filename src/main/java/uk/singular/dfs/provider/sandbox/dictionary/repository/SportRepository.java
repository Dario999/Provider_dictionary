package uk.singular.dfs.provider.sandbox.dictionary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.singular.dfs.provider.sandbox.dictionary.model.Sport;
import uk.singular.dfs.provider.sandbox.dictionary.model.primarykeys.SportId;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface SportRepository extends JpaRepository<Sport, SportId> {

    Sport findSportBySportId(Integer id);

    @Transactional
    Integer deleteAllBySportId(Integer id);

    List<Sport> findAllBySportId(Integer id);

    List<Sport> findAllByLanguage(Integer language);

}
