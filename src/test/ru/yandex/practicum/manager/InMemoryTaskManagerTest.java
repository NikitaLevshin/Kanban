package ru.yandex.practicum.manager;

import org.junit.jupiter.api.BeforeEach;
import ru.yandex.practicum.manager.InMemoryTaskManager;

import java.io.IOException;


public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {


    @BeforeEach
    void beforeEach() throws IOException {
        taskManager = new InMemoryTaskManager();
        super.setUp();
    }
}
