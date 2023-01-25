package Test;

import org.junit.jupiter.api.BeforeEach;
import ru.yandex.practicum.historymanager.InMemoryHistoryManager;

public class InMemoryHistoryManagerTest extends HistoryManagerTest<InMemoryHistoryManager> {

    @BeforeEach
    void beforeEach() {
        historyManager = new InMemoryHistoryManager();
        super.setUp();
    }
}
