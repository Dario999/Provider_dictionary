package uk.singular.dfs.provider.sandbox.dictionary.exceptions.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ErrorHandler;

public class SampleErrorHandler implements ErrorHandler {

    private static final Logger LOG = LoggerFactory.getLogger(SampleErrorHandler.class);

    @Override
    public void handleError(Throwable t) {
        LOG.warn("In default jms error handler...");
        LOG.error("Error Message : {}", t.getMessage());
    }

}