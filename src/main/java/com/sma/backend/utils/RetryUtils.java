package com.sma.backend.utils;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is common class for retry solution.
 *
 * @author : Mak Sophea
 * @since : 08/20/2021
 */
public final class RetryUtils {

    private static final Logger log = LoggerFactory.getLogger(RetryUtils.class);

    public static Object withRetryObject(int maxTimes, long intervalWait, CallToRetryString call) throws Exception {
        if (maxTimes <= 0) {
            throw new IllegalArgumentException("Must run at least one time");
        }
        if (intervalWait <= 0) {
            throw new IllegalArgumentException("Initial wait must be at least 1");
        }
        Exception thrown = null;
        for (int i = 0; i < maxTimes; i++) {
            try {
                return call.processWithString();
            } catch (Exception ex) {
                thrown = ex;
                log.info("Encountered failure on {} due to {}, attempt retry {} of {}", call.getClass().getName(), ex.getMessage(), (i + 1), maxTimes,
                        ex);
            }
            try {
                Thread.sleep(intervalWait);
            } catch (InterruptedException wakeAndAbort) {
                break;
            }
        }
        throw thrown;
    }

    public static boolean withRetry(int maxTimes, long intervalWait, CallToRetry call) throws Exception {
        if (maxTimes <= 0) {
            throw new IllegalArgumentException("Must run at least one time");
        }
        if (intervalWait <= 0) {
            throw new IllegalArgumentException("Initial wait must be at least 1");
        }
        Exception thrown = null;
        for (int i = 0; i < maxTimes; i++) {
            try {
                call.process();
                return true;
            } catch (Exception ex) {
                thrown = ex;
                log.info("Encountered failure on {} due to {}, attempt retry {} of {}", call.getClass().getName(), ex.getMessage(), (i + 1), maxTimes,
                        ex);
            }
            try {
                Thread.sleep(intervalWait);
            } catch (InterruptedException wakeAndAbort) {
                break;
            }
        }
        throw thrown;
    }

    public static boolean withRetryWithIOException(int maxTimes, long intervalWait, CallToRetry call) throws IOException {
        if (maxTimes <= 0) {
            throw new IllegalArgumentException("Must run at least one time");
        }
        if (intervalWait <= 0) {
            throw new IllegalArgumentException("Initial wait must be at least 1");
        }
        IOException thrown = null;
        for (int i = 0; i < maxTimes; i++) {
            try {
                call.process();
                return true;
            } catch (IOException ex) {
                thrown = ex;
                log.info("Encountered failure on {} due to {}, attempt retry {} of {}", call.getClass().getName(), ex.getMessage(), (i + 1), maxTimes,
                        ex);
            }
            try {
                Thread.sleep(intervalWait);
            } catch (InterruptedException wakeAndAbort) {
                break;
            }
        }
        throw thrown;
    }

    /**
     * interface class.
     */
    public interface CallToRetryString {

        Object processWithString() throws Exception;
    }

    public interface CallToRetry {

        void process() throws IOException;
    }

}
