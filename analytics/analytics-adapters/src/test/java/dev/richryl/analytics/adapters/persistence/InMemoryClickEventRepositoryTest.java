package dev.richryl.analytics.adapters.persistence;

import dev.richryl.analytics.application.ports.out.ClickEventRepository;

public class InMemoryClickEventRepositoryTest extends ClickEventRepositoryTest {
    @Override
    protected ClickEventRepository getClickEventRepository() {
        return new InMemoryClickEventRepository();
    }
}
