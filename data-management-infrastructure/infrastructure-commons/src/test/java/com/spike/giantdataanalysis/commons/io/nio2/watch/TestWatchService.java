package com.spike.giantdataanalysis.commons.io.nio2.watch;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

/**
 * Watch Server is a thread safe service that capable of<br/>
 * watching objects for changes and events
 * @author zhoujiagen
 */
public class TestWatchService {

  public static void main(String[] args) throws Exception {
    TestWatchService service = new TestWatchService();
    Path path = Paths.get(System.getProperty("user.home"));
    service.watch(path);
  }

  private void watch(Path path) throws Exception {
    try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
      // register
      path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE,
        StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE);

      while (true) {
        System.out.println("take agin...");
        // get and remove next watch key
        WatchKey key = watchService.take();

        // get pending events of key
        for (WatchEvent<?> watchEvent : key.pollEvents()) {
          Kind<?> kind = watchEvent.kind();

          if (kind == StandardWatchEventKinds.OVERFLOW) {
            continue;
          }

          // get filename from event
          @SuppressWarnings("unchecked")
          WatchEvent<Path> watchEventPath = (WatchEvent<Path>) watchEvent;
          Path filename = watchEventPath.context();

          System.out.println(kind + ": " + filename);
        }

        boolean valid = key.reset();// reset, IMPORTANT!!!
        if (!valid) {// in case of path be deleted
          break;
        }

      } // end of loop

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
