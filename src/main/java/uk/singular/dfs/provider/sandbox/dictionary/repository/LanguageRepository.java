package uk.singular.dfs.provider.sandbox.dictionary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uk.singular.dfs.provider.sandbox.dictionary.model.Language;

@Repository
public interface LanguageRepository extends JpaRepository<Language,Integer> {

    Language findByLanguageName(String name);


}
