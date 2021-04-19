package uk.singular.dfs.provider.sandbox.dictionary.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidLanguageException extends Exception{

    private HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

    public InvalidLanguageException(Integer languageId){
        super("Language id: " + languageId + " is not valid.");
    }

    public HttpStatus getHttpStatus(){
        return httpStatus;
    }

}
