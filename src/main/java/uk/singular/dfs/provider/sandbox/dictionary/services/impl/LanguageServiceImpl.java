package uk.singular.dfs.provider.sandbox.dictionary.services.impl;

import org.springframework.stereotype.Service;
import uk.singular.dfs.provider.sandbox.dictionary.model.Language;
import uk.singular.dfs.provider.sandbox.dictionary.repository.LanguageRepository;
import uk.singular.dfs.provider.sandbox.dictionary.services.LanguageService;

import java.util.HashMap;
import java.util.List;

@Service
public class LanguageServiceImpl implements LanguageService {

    private final LanguageRepository languageRepository;

    private static HashMap<Integer,String> languagesMap;

    public LanguageServiceImpl(LanguageRepository languageRepository) {
        this.languageRepository = languageRepository;
        languagesMap = new HashMap<>();
        /** using HashMap to check if language is valid cause
         *  it is faster than taking data from repository every time
         *  the user calls the function */
        updateLanguagesMap();
    }

    @Override
    public Integer findLanguageIdByLanguageName(String name) {
        Language language = languageRepository.findByLanguageName(name);
        if(language != null) {
            return language.getId();
        }else {
            return null;
        }
    }

    @Override
    public Integer saveNewLanguage(String name) {
        if(languageRepository.findByLanguageName(name) != null){
            return null;
        }
        Language language = new Language();
        language.setLanguageName(name);
        Language created = languageRepository.save(language);
        updateLanguagesMap();
        return created.getId();
    }

    @Override
    public boolean isValidLanguage(String name) {
        return languagesMap.containsValue(name);
    }

    @Override
    public String findNameById(Integer id) {
        return languagesMap.get(id);
    }

    @Override
    public boolean isValidLanguageId(Integer id) {
        return languagesMap.containsKey(id);
    }

    @Override
    public Integer numberOfLanguages() {
        return languagesMap.size();
    }

    @Override
    public List<Language> findAll() {
        return languageRepository.findAll();
    }

    private void updateLanguagesMap(){
        List<Language> allLanguages = languageRepository.findAll();
        for(Language language : allLanguages){
            if (!languagesMap.containsKey(language.getId())){
                languagesMap.put(language.getId(),language.getLanguageName());
            }
        }
    }

}
