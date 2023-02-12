package test.manager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import ru.yandex.practicum.api.KVServer;
import ru.yandex.practicum.manager.InMemoryTaskManager;

import java.io.IOException;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    KVServer kvServer;

    @BeforeEach
    void beforeEach() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        taskManager = new InMemoryTaskManager();
        super.setUp();
    }

    @AfterEach
    void afterEach() {
        kvServer.stop();
    }


}
