package uk.singular.dfs.provider.sandbox.dictionary.controllers.menagement;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import uk.singular.dfs.provider.sandbox.dictionary.services.DataImportService;
import uk.singular.dfs.provider.sandbox.dictionary.services.LanguageService;

import java.io.IOException;

@RestController
@RequestMapping("/api/manage/import")
@Api(value = "Data", description = "REST API for importing data from file", tags = { "Import Data Controller" })
public class DataImportController {

    private final DataImportService dataImportService;
    private final LanguageService languageService;

    public DataImportController(DataImportService dataImportService, LanguageService languageService) {
        this.dataImportService = dataImportService;
        this.languageService = languageService;
    }

    @PostMapping("/upload")
    @ApiOperation(value = "Upload a csv file for")
    public ResponseEntity<String> uploadFile(@ApiParam(value = "File to upload",required = true)
                                             @RequestParam("file") MultipartFile file,
                                             @ApiParam(value = "Language id for the data",required = true)
                                             @RequestParam("languageId") Integer languageId) throws Exception {
        dataImportService.save(file,languageId);
        return ResponseEntity.status(HttpStatus.OK).body("File uploaded.Processing in background...");
    }

    @PostMapping("/load")
    @ApiOperation(value = "Load the initial data for EN and MKD")
    public ResponseEntity<String> loadInitialData() throws IOException {
        dataImportService.loadInitialFiles();
        return ResponseEntity.status(HttpStatus.OK).body("Success");
    }

}
