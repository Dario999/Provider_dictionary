package uk.singular.dfs.provider.sandbox.dictionary.exceptions;

public class InvalidFileTypeException extends Exception{

    public InvalidFileTypeException(){
        super("File must be in .csv format!");
    }

}
