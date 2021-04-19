package uk.singular.dfs.provider.sandbox.dictionary.controllers.client;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.singular.dfs.provider.sandbox.dictionary.exceptions.InvalidLanguageException;
import uk.singular.dfs.provider.sandbox.dictionary.model.League;
import uk.singular.dfs.provider.sandbox.dictionary.services.LeagueService;

import java.util.List;

@RestController
@RequestMapping("/api/league")
@Api(value = "League", description = "Client REST API for League", tags = { "Client League Controller" })
public class LeagueController {

    private final LeagueService leagueService;

    @Value("${max.paging.size}")
    private Integer maxPageSize;

    public LeagueController(LeagueService leagueService) {
        this.leagueService = leagueService;
    }

    @GetMapping("/all")
    @ApiOperation(value = "Find all Leagues pageable",response = League[].class)
    public Page<League> findAll(@ApiParam(value = "Language id",required = false)
                                @RequestParam(required = false) Integer languageId,
                                @ApiParam(value = "Page number",required = true)
                                @RequestParam Integer pageNumber,
                                @ApiParam(value = "Page size",required = true)
                                @RequestParam Integer pageSize) throws InvalidLanguageException {
        if(pageSize > maxPageSize)
        {
            pageSize = maxPageSize;
        }
        Pageable pageRequest = PageRequest.of(pageNumber,pageSize);
        if(languageId == null)
            return leagueService.findAllPageable(pageRequest);
        else
            return leagueService.findAllByLanguagePageable(pageRequest,languageId);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Find League by id",response = League.class)
    public ResponseEntity<League> findByLeagueId(@ApiParam(value = "League id to search",required = true)
                                                 @PathVariable Integer id,
                                                 @ApiParam(value = "Language id",required = false)
                                                 @RequestParam(required = false) Integer languageId) throws Exception{
        if(languageId == null)
            return ResponseEntity.status(HttpStatus.OK).body(leagueService.findLeagueByLeagueId(id));
        else
            return ResponseEntity.status(HttpStatus.OK).body(leagueService.findLeagueByLeagueIdAndLanguage(id,languageId));
    }

    @GetMapping("/findByCountryId/{countryId}")
    @ApiOperation(value = "Find all leagues in country with id",response = League[].class)
    public ResponseEntity<List<League>> findAllByCountryId(@ApiParam(value = "Country id",required = true)
                                                          @PathVariable Integer countryId,
                                                          @ApiParam(value = "Language id",required = false)
                                                          @RequestParam(required = false) Integer languageId) throws InvalidLanguageException {
        if(languageId == null)
            return ResponseEntity.status(HttpStatus.OK).body(leagueService.findAllByCountryId(countryId));
        else
            return ResponseEntity.status(HttpStatus.OK).body(leagueService.findAllByCountryIdAndLanguage(countryId,languageId));
    }

}
