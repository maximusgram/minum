package com.renomad.minum.utils;

import com.renomad.minum.Context;
import com.renomad.minum.logging.TestLogger;

import static com.renomad.minum.testing.TestFramework.assertEquals;
import static com.renomad.minum.testing.TestFramework.assertTrue;

public class StackTraceUtilsTests {
    private final TestLogger logger;

    public StackTraceUtilsTests(Context context) {
        this.logger = (TestLogger)context.getLogger();
        logger.testSuite("StackTraceUtilsTests");
    }

    public void tests() {

        logger.test("convert a stacktrace to a string"); {
            String result = StacktraceUtils.stackTraceToString(new RuntimeException("foo"));
            assertTrue(result.contains("java.lang.RuntimeException: foo"));
        }

        logger.test("convert an array of stack trace elements to a string"); {
            String result = StacktraceUtils.stackTraceToString(new RuntimeException("foo").getStackTrace());
            assertTrue(result.contains("minum.utils.StackTraceUtilsTests.tests(StackTraceUtilsTests.java:25);"));
        }
    }
}