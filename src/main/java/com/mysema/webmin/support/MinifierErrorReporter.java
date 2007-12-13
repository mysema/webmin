/*
 * Copyright (c) 2007 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.webmin.support;

import org.mozilla.javascript.ErrorReporter;
import org.mozilla.javascript.EvaluatorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * MinifierErrorReporter provides
 *
 * @author Timo Westkamper
 * @version $Id$
 */
class MinifierErrorReporter implements ErrorReporter {
    private static final Logger logger = LoggerFactory.getLogger(MinifierErrorReporter.class);

    public void warning(String message, String sourceName,
                        int line, String lineSource, int lineOffset) {
        if (line < 0) {
            logger.warn(message);
        } else {
            logger.warn(line + ':' + lineOffset + ':' + message);
        }
    }

    public void error(String message, String sourceName,
                      int line, String lineSource, int lineOffset) {
        if (line < 0) {
            logger.error(message);
        } else {
            logger.error(line + ':' + lineOffset + ':' + message);
        }
    }

    public EvaluatorException runtimeError(String message, String sourceName,
                                           int line, String lineSource, int lineOffset) {
        error(message, sourceName, line, lineSource, lineOffset);
        return new EvaluatorException(message);
    }

}
