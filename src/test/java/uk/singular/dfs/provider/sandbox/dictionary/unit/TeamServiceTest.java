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
import uk.singular.dfs.provider.sandbox.dictionary.model.Language;
import uk.singular.dfs.provider.sandbox.dictionary.model.Team;
import uk.singular.dfs.provider.sandbox.dictionary.repository.LanguageRepository;
import uk.singular.dfs.provider.sandbox.dictionary.repository.LeagueRepository;
import uk.singular.dfs.provider.sandbox.dictionary.repository.TeamRepository;
import uk.singular.dfs.provider.sandbox.dictionary.services.impl.LanguageServiceImpl;
import uk.singular.dfs.provider.sandbox.dictionary.services.impl.LeagueServiceImpl;
import uk.singular.dfs.provider.sandbox.dictionary.services.impl.TeamServiceImpl;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@RunWith(MockitoJUnitRunner.class)
public class TeamServiceTest {

    @Mock
    TeamRepository teamRepository;

    @InjectMocks
    TeamServiceImpl teamService;

    @Mock
    LeagueRepository leagueRepository;

    @InjectMocks
    LeagueServiceImpl leagueService;

    @Mock
    LanguageRepository languageRepository;

    @InjectMocks
    LanguageServiceImpl languageService;

    private Team team;
    private Language language;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Before
    public void init(){
        team = new Team();
        language = new Language();
        language.setLanguageName("English");
        when(teamRepository.save(Mockito.any(Team.class))).thenReturn(team);
        when(languageRepository.save(Mockito.any(Language.class))).thenReturn(language);
        languageRepository.save(language);
    }

    @Test
    public void test_team_save() throws Exception {
        team.setTeamId(1);
        team.setName("Team test name");
        team.setLeagueId(null);
        team.setFkLanguage(null);
        Team created = teamRepository.save(team);
        assertThat(created.getName()).isSameAs(team.getName());
    }

    @Test(expected = InvalidDataInputException.class)
    public void test_save_country_without_id() throws Exception {
        team.setName("Country test");
        teamService.save(team);
    }

    @Test(expected = InvalidDataInputException.class)
    public void test_empty_name() throws Exception {
        team.setTeamId(1);
        team.setName("");
        teamService.save(team);
    }

    @Test(expected = InvalidDataInputException.class)
    public void test_null_name() throws Exception {
        team.setTeamId(1);
        team.setName(null);
        teamService.save(team);
    }

}
