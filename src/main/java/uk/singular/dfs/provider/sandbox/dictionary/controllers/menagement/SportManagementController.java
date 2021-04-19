package uk.singular.dfs.provider.sandbox.dictionary.controllers.menagement;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.singular.dfs.provider.sandbox.dictionary.exceptions.InvalidDataInputException;
import uk.singular.dfs.provider.sandbox.dictionary.model.Sport;
import uk.singular.dfs.provider.sandbox.dictionary.services.LanguageService;
import uk.singular.dfs.provider.sandbox.dictionary.services.SportService;


@RestController
@RequestMapping("/api/manage/sport")
@Api(value = "Sport", description = "Management REST API for Sport", tags = { "Management Sport Controller" })
public class SportManagementController {

    private final SportService sportService;
    private final LanguageService languageService;

    public SportManagementController(SportService sportService, LanguageService languageService) {
        this.sportService = sportService;
        this.languageService = languageService;
    }

    @PostMapping("/add")
    @ApiOperation(value = "Add new sport")
    public ResponseEntity<String> addSport(@RequestBody Sport sport,
                                           @ApiParam(value = "Language id:",required = true)
                                           @RequestParam Integer languageId) throws InvalidDataInputException {
        if(languageService.isValidLanguageId(languageId)){
            sport.setLanguage(languageId);
            sportService.save(sport);
            return ResponseEntity.status(HttpStatus.OK).body("Success");
        }else{
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid language id");
        }
    }

    @PutMapping("/update")
    @ApiOperation(value = "Update sport")
    public ResponseEntity<String> update(@RequestBody Sport sport,
                                         @ApiParam(value = "Sport id to update:",required = true)
                                         @RequestParam Integer sportId,
                                         @ApiParam(value = "Language id:",required = true)
                                         @RequestParam Integer languageId) throws Exception {
        sportService.update(sport,sportId,languageId);
        return ResponseEntity.status(HttpStatus.OK).body("Success");
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "Delete by id")
    public ResponseEntity<String> delete(@ApiParam(value = "Sport id to delete:",required = true)
                                         @PathVariable Integer id){
        Integer rowDeleted = sportService.delete(id);
        if(rowDeleted != 0)
            return ResponseEntity.status(HttpStatus.OK).body("Number od rows deleted: " + rowDeleted);
        else
            return ResponseEntity.status(HttpStatus.OK).body("No entities found with id : " + id);
    }

}
