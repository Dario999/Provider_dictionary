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
import uk.singular.dfs.provider.sandbox.dictionary.model.League;
import uk.singular.dfs.provider.sandbox.dictionary.repository.CountryRepository;
import uk.singular.dfs.provider.sandbox.dictionary.repository.LeagueRepository;
import uk.singular.dfs.provider.sandbox.dictionary.services.impl.CountryServiceImpl;
import uk.singular.dfs.provider.sandbox.dictionary.services.impl.LeagueServiceImpl;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@RunWith(MockitoJUnitRunner.class)
public class LeagueServiceTest {

    @Mock
    LeagueRepository leagueRepository;

    @InjectMocks
    LeagueServiceImpl leagueService;

    @Mock
    CountryRepository countryRepository;

    @InjectMocks
    CountryServiceImpl countryService;

    private League league;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Before
    public void init(){
        league = new League();
        when(leagueRepository.save(Mockito.any(League.class))).thenReturn(league);
    }

    @Test
    public void test_league_save(){
        league.setLeagueId(1);
        league.setName("Country test");
        league.setCountryId(null);
        league.setFkLanguage(null);
        League created = leagueRepository.save(league);
        assertThat(created.getName()).isSameAs(league.getName());
    }

    @Test(expected = InvalidDataInputException.class)
    public void test_save_country_without_id() throws Exception {
        league.setName("Country test");
        leagueService.save(league);
    }

    @Test(expected = InvalidDataInputException.class)
    public void test_empty_name() throws Exception {
        league.setCountryId(1);
        league.setName("");
        leagueService.save(league);
    }

    @Test(expected = InvalidDataInputException.class)
    public void test_null_name() throws Exception {
        league.setCountryId(1);
        league.setName(null);
        leagueService.save(league);
    }

}
