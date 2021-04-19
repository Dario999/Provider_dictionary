package uk.singular.dfs.provider.sandbox.dictionary.controllers.menagement;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.singular.dfs.provider.sandbox.dictionary.model.Country;
import uk.singular.dfs.provider.sandbox.dictionary.services.CountryService;
import uk.singular.dfs.provider.sandbox.dictionary.services.LanguageService;

@RestController
@RequestMapping("/api/manage/country")
@Api(value = "Country", description = "Management REST API for Country", tags = { "Management Country Controller" })
public class CountryManagementController {

    private final CountryService countryService;
    private final LanguageService languageService;

    public CountryManagementController(CountryService countryService, LanguageService languageService) {
        this.countryService = countryService;
        this.languageService = languageService;
    }

    @PostMapping("/add")
    @ApiOperation(value = "Add new country")
    public ResponseEntity<String> addCountry(@RequestBody Country country,
                                             @ApiParam(value = "Language id:",required = true)
                                             @RequestParam Integer languageId) throws Exception {
        if(languageService.isValidLanguageId(languageId)){
            country.setLanguage(languageId);
            countryService.save(country);
            return ResponseEntity.status(HttpStatus.OK).body("Success");
        }else{
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid language id");
        }
    }

    @PutMapping("/update")
    @ApiOperation(value = "Update existing country")
    public ResponseEntity<String> update(@RequestBody Country country,
                                         @ApiParam(value = "Country id to update:",required = true)
                                         @RequestParam Integer countryId,
                                         @ApiParam(value = "Language id:",required = true)
                                         @RequestParam Integer languageId) throws Exception {
        countryService.update(country,countryId,languageId);
        return ResponseEntity.status(HttpStatus.OK).body("Success");
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "Delete by id")
    public ResponseEntity<String> delete(@PathVariable Integer id){
        Integer rowDeleted = countryService.delete(id);
        if(rowDeleted != 0)
            return ResponseEntity.status(HttpStatus.OK).body("Number od rows deleted: " + rowDeleted);
        else
            return ResponseEntity.status(HttpStatus.OK).body("No entities found with id : " + id);
    }

}
