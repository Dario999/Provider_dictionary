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
import uk.singular.dfs.provider.sandbox.dictionary.model.Country;
import uk.singular.dfs.provider.sandbox.dictionary.services.CountryService;

import java.util.List;

@RestController
@RequestMapping("/api/country")
@Api(value = "Country", description = "Client REST API for Country", tags = { "Client Country Controller" })
public class CountryController {

    private final CountryService countryService;

    @Value("${max.paging.size}")
    private Integer maxPageSize;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping("/all")
    @ApiOperation(value = "Find all Countries pageable",response = Country[].class)
    public Page<Country> findAll(@ApiParam(value = "Language id",required = false)
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
            return countryService.findAllPageable(pageRequest);
        else
            return countryService.findAllByLanguagePageable(pageRequest,languageId);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Find Country by id",response = Country.class)
    public ResponseEntity<Country> findByCountryId(@ApiParam(value = "Country id to search",required = true)
                                 @PathVariable Integer id,
                                                @ApiParam(value = "Language id",required = false)
                                 @RequestParam(required = false) Integer languageId) throws Exception{
        if(languageId == null)
            return ResponseEntity.status(HttpStatus.OK).body(countryService.findCountryByCountryId(id));
        else
            return ResponseEntity.status(HttpStatus.OK).body(countryService.findCountryByCountryIdAndLanguage(id,languageId));
    }

    @GetMapping("/findBySportId/{sportId}")
    @ApiOperation(value = "Find all countries for sport with id",response = Country[].class)
    public ResponseEntity<List<Country>> findAllBySportId(@ApiParam(value = "Sport id",required = true)
                                                       @PathVariable Integer sportId,
                                                       @ApiParam(value = "Language id",required = false)
                                                       @RequestParam(required = false) Integer languageId) throws InvalidLanguageException {
        if(languageId == null)
            return ResponseEntity.status(HttpStatus.OK).body(countryService.findAllBySportId(sportId));
        else
            return ResponseEntity.status(HttpStatus.OK).body(countryService.findAllBySportIdAndLanguage(sportId,languageId));
    }

}
