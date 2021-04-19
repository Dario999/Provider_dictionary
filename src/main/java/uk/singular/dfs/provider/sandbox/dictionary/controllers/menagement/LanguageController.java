package uk.singular.dfs.provider.sandbox.dictionary.controllers.menagement;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.singular.dfs.provider.sandbox.dictionary.model.Language;
import uk.singular.dfs.provider.sandbox.dictionary.services.LanguageService;

import java.util.List;

@RestController
@RequestMapping("/api/languages")
@Api(value = "Language", description = "REST API for languages", tags = { "Languages Controller" })
public class LanguageController {

    private final LanguageService languageService;

    public LanguageController(LanguageService languageService) {
        this.languageService = languageService;
    }

    @GetMapping("/all")
    @ApiOperation(value = "Find all languages with their id and name")
    public ResponseEntity<List<Language>> findAll(){
        return ResponseEntity.status(HttpStatus.OK).body(languageService.findAll());
    }

    @GetMapping("/findName")
    @ApiOperation(value = "Find the name of the language with id")
    public ResponseEntity<String> findName(@ApiParam(value = "Language id:",required = true)
                                           @RequestParam Integer id){
        String name = languageService.findNameById(id);
        return ResponseEntity.status(HttpStatus.OK).body(name != null ? name : "Language with given id dont exist");
    }

    @GetMapping("/findId")
    @ApiOperation(value = "Find the id for language with name")
    public ResponseEntity<String> findId(@ApiParam(value = "Language name:",required = true)
                                         @RequestParam String name){
        Integer id = languageService.findLanguageIdByLanguageName(name);
        return ResponseEntity.status(HttpStatus.OK).body(id != null ? id.toString() : "Language with given name dont exist");
    }

    @PostMapping("/addLanguage")
    @ApiOperation(value = "Add new language")
    public ResponseEntity<String> addNewLanguage(@ApiParam(value = "Language name:",required = true)
                                                 @RequestParam String name){
        Integer generatedId = languageService.saveNewLanguage(name);
        if(generatedId != null){
            return ResponseEntity.status(HttpStatus.OK).body("New language created with id: " + generatedId);
        }else{
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Language with name " + name + " already exist");
        }
    }

}
