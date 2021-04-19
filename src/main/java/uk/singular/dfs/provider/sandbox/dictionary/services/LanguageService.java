package uk.singular.dfs.provider.sandbox.dictionary.services;

import uk.singular.dfs.provider.sandbox.dictionary.model.Language;

import java.util.List;

public interface LanguageService {

    /** finds name for language with name
     * @param name
     * @return language id (null if language don't exist)
     */
    Integer findLanguageIdByLanguageName(String name);

    /** save new language
     * @param name
     * @return generated id for the saved language (null if language already exist)
     */
    Integer saveNewLanguage(String name);

    /** check if string is a valid language name
     * @param name
     * @return true / false
     */
    boolean isValidLanguage(String name);

    boolean isValidLanguageId(Integer id);

    /** count number if languages from db
     * using this to know how many rows to list in pagination based on number of languages
     * @return number of languages
     */
    Integer numberOfLanguages();

    /** find all languages with their id and name */
    List<Language> findAll();

    String findNameById(Integer id);

}
