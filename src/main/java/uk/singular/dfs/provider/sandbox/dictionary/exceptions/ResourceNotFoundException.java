package uk.singular.dfs.provider.sandbox.dictionary.exceptions;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends Exception{

    private HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

    public ResourceNotFoundException(String message){
        super(message);
    }

    public HttpStatus getHttpStatus(){
        return httpStatus;
    }

}
