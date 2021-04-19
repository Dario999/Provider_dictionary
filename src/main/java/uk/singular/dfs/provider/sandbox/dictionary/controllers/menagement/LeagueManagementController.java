package uk.singular.dfs.provider.sandbox.dictionary.controllers.menagement;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.singular.dfs.provider.sandbox.dictionary.model.League;
import uk.singular.dfs.provider.sandbox.dictionary.services.LanguageService;
import uk.singular.dfs.provider.sandbox.dictionary.services.LeagueService;

@RestController
@RequestMapping("/api/manage/league")
@Api(value = "League", description = "Management REST API for League", tags = { "Management League Controller" })
public class LeagueManagementController {

    private final LeagueService leagueService;
    private final LanguageService languageService;

    public LeagueManagementController(LeagueService leagueService, LanguageService languageService) {
        this.leagueService = leagueService;
        this.languageService = languageService;
    }

    @PostMapping("/add")
    @ApiOperation(value = "Add new league")
    public ResponseEntity<String> addLeague(@RequestBody League league,
                                            @ApiParam(value = "Language id:",required = true)
                                            @RequestParam Integer languageId) throws Exception {
        if(languageService.isValidLanguageId(languageId)){
            league.setLanguage(languageId);
            leagueService.save(league);
            return ResponseEntity.status(HttpStatus.OK).body("Success");
        }else{
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid language id");
        }
    }

    @PutMapping("/update")
    public ResponseEntity<String> update(@RequestBody League league,
                                         @ApiParam(value = "League id to update:",required = true)
                                         @RequestParam Integer leagueId,
                                         @ApiParam(value = "Language id:",required = true)
                                         @RequestParam Integer languageId) throws Exception{
        leagueService.update(league,leagueId,languageId);
        return ResponseEntity.status(HttpStatus.OK).body("Success");
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "Delete by id")
    public ResponseEntity<String> delete(@ApiParam(value = "League id to delete:",required = true)
                                         @PathVariable Integer id){
        Integer rowDeleted = leagueService.delete(id);
        if(rowDeleted != 0)
            return ResponseEntity.status(HttpStatus.OK).body("Number od rows deleted: " + rowDeleted);
        else
            return ResponseEntity.status(HttpStatus.OK).body("No entities found with id : " + id);
    }

}
