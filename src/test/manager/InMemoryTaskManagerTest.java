package test.manager;

import org.junit.jupiter.api.BeforeEach;
import ru.yandex.practicum.manager.InMemoryTaskManager;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    void beforeEach() {
        taskManager = new InMemoryTaskManager();
        super.setUp();
    }


}
