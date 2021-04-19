package uk.singular.dfs.provider.sandbox.dictionary.controllers.menagement;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.singular.dfs.provider.sandbox.dictionary.model.Team;
import uk.singular.dfs.provider.sandbox.dictionary.services.LanguageService;
import uk.singular.dfs.provider.sandbox.dictionary.services.TeamService;

@RestController
@RequestMapping("/api/manage/team")
@Api(value = "Team", description = "Management REST API for Team", tags = { "Management Team Controller" })
public class TeamManagementController {

    private final TeamService teamService;
    private final LanguageService languageService;

    public TeamManagementController(TeamService teamService, LanguageService languageService) {
        this.teamService = teamService;
        this.languageService = languageService;
    }

    @PostMapping("/add")
    @ApiOperation(value = "Add new team")
    public ResponseEntity<String> addTeam(@RequestBody Team team,
                                          @ApiParam(value = "Language id:",required = true)
                                          @RequestParam Integer languageId) throws Exception {
        if(languageService.isValidLanguageId(languageId)){
            team.setLanguage(languageId);
            teamService.save(team);
            return ResponseEntity.status(HttpStatus.OK).body("Success");
        }else{
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid language");
        }
    }

    @PutMapping("/update")
    public ResponseEntity<String> update(@RequestBody Team team,
                                         @ApiParam(value = "Team id to update:",required = true)
                                         @RequestParam Integer teamId,
                                         @ApiParam(value = "Language id:",required = true)
                                         @RequestParam Integer languageId) throws Exception {
        teamService.update(team,teamId,languageId);
        return ResponseEntity.status(HttpStatus.OK).body("Success");
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "Delete by id")
    public ResponseEntity<String> delete(@ApiParam(value = "Team id to delete:",required = true)
                                         @PathVariable Integer id){
        Integer rowDeleted = teamService.delete(id);
        if(rowDeleted != 0)
            return ResponseEntity.status(HttpStatus.OK).body("Number od rows deleted: " + rowDeleted);
        else
            return ResponseEntity.status(HttpStatus.OK).body("No entities found with id : " + id);
    }

}
