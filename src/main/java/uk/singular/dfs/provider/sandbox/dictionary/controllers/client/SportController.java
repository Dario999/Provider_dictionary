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
import uk.singular.dfs.provider.sandbox.dictionary.model.Sport;
import uk.singular.dfs.provider.sandbox.dictionary.services.LanguageService;
import uk.singular.dfs.provider.sandbox.dictionary.services.SportService;

@RestController
@RequestMapping("/api/sport")
@Api(value = "Sport", description = "Client REST API for Sport", tags = { "Client Sport Controller" })
public class SportController {

    private final SportService sportService;
    private final LanguageService languageService;

    @Value("${max.paging.size}")
    private Integer maxPageSize;

    public SportController(SportService sportService, LanguageService languageService) {
        this.sportService = sportService;
        this.languageService = languageService;
    }

    @GetMapping("/all")
    @ApiOperation(value = "Find all Sports pageable",response = Sport[].class)
    public Page<Sport> findAll(@ApiParam(value = "Language id:",required = false)
                               @RequestParam(required = false) Integer languageId,
                               @ApiParam(value = "Page number:",required = true)
                               @RequestParam Integer pageNumber,
                               @ApiParam(value = "Page size:",required = true)
                               @RequestParam Integer pageSize) throws InvalidLanguageException {
        if(pageSize > maxPageSize)
        {
            pageSize = maxPageSize;
        }
        Pageable pageRequest = PageRequest.of(pageNumber,pageSize);
        if(languageId == null)
            return sportService.findAllPageable(pageRequest);
        else
            return sportService.findAllByLanguagePageable(pageRequest, languageId);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Find Sport by id",response = Sport.class)
    public ResponseEntity<Sport> findBySportId(@ApiParam(value = "Sport id to search:",required = true)
                                               @PathVariable Integer id,
                                               @ApiParam(value = "Language id:",required = false)
                                               @RequestParam(required = false) Integer languageId) throws Exception {
        if(languageId == null)
            return ResponseEntity.status(HttpStatus.OK).body(sportService.findSportBySportId(id));
        else
            return ResponseEntity.status(HttpStatus.OK).body(sportService.findSportBySportIdAndLanguage(id,languageId));
    }



}
