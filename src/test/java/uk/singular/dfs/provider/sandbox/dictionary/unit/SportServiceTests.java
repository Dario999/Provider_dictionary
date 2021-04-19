package uk.singular.dfs.provider.sandbox.dictionary.unit;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.context.ActiveProfiles;
import uk.singular.dfs.provider.sandbox.dictionary.exceptions.InvalidDataInputException;
import uk.singular.dfs.provider.sandbox.dictionary.model.Sport;
import uk.singular.dfs.provider.sandbox.dictionary.repository.SportRepository;
import uk.singular.dfs.provider.sandbox.dictionary.services.impl.SportServiceImpl;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@RunWith(MockitoJUnitRunner.class)
public class SportServiceTests {

    @Mock
    SportRepository sportRepository;

    @InjectMocks
    SportServiceImpl sportService;

    private Sport toAdd;

    @Before
    public void init(){
        toAdd = new Sport();
        when(sportRepository.save(Mockito.any(Sport.class))).thenReturn(toAdd);
    }

    @Test
    public void when_save_sport_return_saved_sport() throws Exception{
        toAdd.setSportId(1);
        toAdd.setName("Sport test");

        Sport created = sportService.save(toAdd);
        assertThat(created.getName()).isSameAs(toAdd.getName());
    }

    @Test(expected = InvalidDataInputException.class)
    public void test_save_sport_without_id() throws InvalidDataInputException {
        toAdd.setName("Sport test");
        sportService.save(toAdd);
    }

    @Test(expected = InvalidDataInputException.class)
    public void test_empty_name() throws InvalidDataInputException {
        toAdd.setSportId(1);
        toAdd.setName("");
        sportService.save(toAdd);
    }

    @Test(expected = InvalidDataInputException.class)
    public void test_null_name() throws InvalidDataInputException {
        toAdd.setSportId(1);
        toAdd.setName(null);
        sportService.save(toAdd);
    }

}
