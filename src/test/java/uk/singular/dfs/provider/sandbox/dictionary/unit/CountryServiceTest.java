package uk.singular.dfs.provider.sandbox.dictionary.unit;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.context.ActiveProfiles;
import uk.singular.dfs.provider.sandbox.dictionary.exceptions.InvalidDataInputException;
import uk.singular.dfs.provider.sandbox.dictionary.exceptions.ResourceNotFoundException;
import uk.singular.dfs.provider.sandbox.dictionary.model.Country;
import uk.singular.dfs.provider.sandbox.dictionary.model.Language;
import uk.singular.dfs.provider.sandbox.dictionary.repository.CountryRepository;
import uk.singular.dfs.provider.sandbox.dictionary.repository.LanguageRepository;
import uk.singular.dfs.provider.sandbox.dictionary.repository.SportRepository;
import uk.singular.dfs.provider.sandbox.dictionary.services.impl.CountryServiceImpl;
import uk.singular.dfs.provider.sandbox.dictionary.services.impl.LanguageServiceImpl;
import uk.singular.dfs.provider.sandbox.dictionary.services.impl.SportServiceImpl;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@RunWith(MockitoJUnitRunner.class)
public class CountryServiceTest {

    @Mock
    CountryRepository countryRepository;

    @InjectMocks
    CountryServiceImpl countryService;

    @Mock
    SportRepository sportRepository;

    @InjectMocks
    SportServiceImpl sportService;

    @Mock
    LanguageRepository languageRepository;

    @InjectMocks
    LanguageServiceImpl languageService;

    private Country toAdd;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Before
    public void init(){
        toAdd = new Country();
        when(countryRepository.save(Mockito.any(Country.class))).thenReturn(toAdd);
    }

    @Test
    public void test_country_save(){
        toAdd.setCountryId(1);
        toAdd.setName("Country test");
        toAdd.setSportId(null);
        toAdd.setFkLanguage(null);
        Country created = countryRepository.save(toAdd);
        assertThat(created.getName()).isSameAs(toAdd.getName());
    }

    @Test(expected = InvalidDataInputException.class)
    public void test_save_country_without_id() throws Exception {
        toAdd.setName("Country test");
        countryService.save(toAdd);
    }

    @Test(expected = InvalidDataInputException.class)
    public void test_empty_name() throws Exception {
        toAdd.setCountryId(1);
        toAdd.setName("");
        countryService.save(toAdd);
    }

    @Test(expected = InvalidDataInputException.class)
    public void test_null_name() throws Exception {
        toAdd.setCountryId(1);
        toAdd.setName(null);
        countryService.save(toAdd);
    }

    //@Test
    public void test_save_country_with_invalid_fk() throws Exception{
        exceptionRule.expect(ResourceNotFoundException.class);
        exceptionRule.expectMessage("Sport with id: 1 and language: English not found!");
        toAdd.setCountryId(1);
        toAdd.setName("Country test");
        toAdd.setSportId(1);
        toAdd.setFkLanguage(1);

        // language adding problem
        Language language = new Language();
        language.setLanguageName("Test");
        languageRepository.save(language);
        sportService.setLanguageService(languageService);
        countryService.setLanguageService(languageService);
        countryService.setSportService(sportService);
        countryService.save(toAdd);
    }

}
