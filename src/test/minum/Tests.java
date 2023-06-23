package minum;

import minum.database.SimpleDatabaseTests;
import minum.sampledomain.LruCacheTests;
import minum.testing.TestLogger;
import minum.utils.*;
import minum.web.*;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;

public class Tests {

  public static void main(String[] args) {
    try {
      unitAndIntegrationTests();
      clearTestDatabase();
//      testFullSystem_Soup_To_Nuts();
//      clearTestDatabase();
      indicateTestsFinished();
    } catch (Exception ex) {
      MyThread.sleep(100);
      ex.printStackTrace();
    }
  }

  private static void indicateTestsFinished() {
    System.out.println();
    System.out.println("-------------------------");
    System.out.println("----  Tests finished ---- ");
    System.out.println("-------------------------");
  }

  /**
   * These tests range in size from focusing on very small elements (unit tests)
   * to larger combinations of methods and classes (integration tests) but
   * stop short of running {@link FullSystem}.
   */
  private static void unitAndIntegrationTests() throws Exception {
    TestLogger logger = TestLogger.makeTestLogger();
    var es = logger.getExecutorService();
//    new WebTests(logger).tests(es);
//    new SimpleDatabaseTests(logger).tests(es);
//    new LruCacheTests(logger).tests(es);
//    new StringUtilsTests(logger).tests();
//    new TemplatingTests(logger).tests();
//    new Http2Tests(logger).test(es);
//    new FullSystemTests(logger).tests(es);
//    new StaticFilesCacheTests(logger).tests(es);
    new HtmlParserTests(logger).tests(es);
    runShutdownSequence(es);
  }

  /**
   * Run a test of the entire system.  In particular, runs code
   * from {@link FullSystem}
   */
  private static void testFullSystem_Soup_To_Nuts() throws Exception {
    System.out.println("Starting a soup-to-nuts tests of the full system");
    var wf = FullSystem.initialize();
    TheRegister.registerDomains(wf);
    new FunctionalTests(wf).test();
    wf.getFullSystem().removeShutdownHook();
    wf.getFullSystem().close();
    wf.getFullSystem().getExecutorService().shutdownNow();
  }

  private static void clearTestDatabase() throws IOException {
      TestLogger logger = TestLogger.makeTestLogger();
      FileUtils.deleteDirectoryRecursivelyIfExists(Path.of("out/simple_db"), logger);
      runShutdownSequence(logger.getExecutorService());
  }

  private static void runShutdownSequence(ExecutorService es) {
    ActionQueue.killAllQueues();
    es.shutdown();
  }

}
