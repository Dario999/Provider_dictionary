package uk.singular.dfs.provider.sandbox.dictionary.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface DataImportService {

    /** load the initial data from files for EN and MKD */
    void loadInitialFiles() throws IOException;

    /** upload new csv file and save the data */
    void save(MultipartFile file,Integer languageId) throws Exception;

}
