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
import uk.singular.dfs.provider.sandbox.dictionary.model.Team;
import uk.singular.dfs.provider.sandbox.dictionary.services.TeamService;

import java.util.List;

@RestController
@RequestMapping("/api/team")
@Api(value = "Team", description = "Client REST API for Team", tags = { "Client Team Controller" })
public class TeamController {

    private final TeamService teamService;

    @Value("${max.paging.size}")
    private Integer maxPageSize;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @GetMapping("/all")
    @ApiOperation(value = "Find all Teams pageable",response = Team[].class)
    public Page<Team> findAll(@ApiParam(value = "Language id",required = false)
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
            return teamService.findAllPageable(pageRequest);
        else
            return teamService.findAllByLanguagePageable(pageRequest,languageId);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Find Team by id",response = Team.class)
    public ResponseEntity<Team> findByTeamId(@ApiParam(value = "Team id to search",required = true)
                              @PathVariable Integer id,
                              @ApiParam(value = "Language id",required = false)
                              @RequestParam(required = false) Integer languageId) throws Exception {
        if(languageId == null)
            return ResponseEntity.status(HttpStatus.OK).body(teamService.findTeamByTeamId(id));
        else
            return ResponseEntity.status(HttpStatus.OK).body(teamService.findTeamByTeamIdAndLanguage(id,languageId));
    }

    @GetMapping("/plays")
    @ApiOperation(value = "Find all teams that play sport with id",response = Team[].class)
    public List<Team> findBySportIdAndCountryId(@ApiParam(value = "Sport id ",required = true)
                                    @RequestParam Integer sportId,
                                    @ApiParam(value = "Language id",required = false)
                                    @RequestParam(required = false) Integer languageId) throws InvalidLanguageException {
        if(languageId == null){
            return teamService.findAllThatPlaySport(sportId);
        }else{
            return teamService.findAllThatPlaySportByLanguage(sportId, languageId);
        }
    }

    @GetMapping("/findByLeagueId/{leagueId}")
    @ApiOperation(value = "Find all teams that play in league with id",response = Team[].class)
    public ResponseEntity<List<Team>> findAllByLeagueId(@ApiParam(value = "League id ",required = true)
                                    @PathVariable Integer leagueId,
                                    @ApiParam(value = "Language id",required = false)
                                    @RequestParam(required = false) Integer languageId) throws InvalidLanguageException {
        if(languageId == null){
            return ResponseEntity.status(HttpStatus.OK).body(teamService.findAllThatPlayInLeague(leagueId));
        }else{
            return ResponseEntity.status(HttpStatus.OK).body(teamService.findAllThatPlayInLeagueByLanguage(leagueId, languageId));
        }
    }
}
