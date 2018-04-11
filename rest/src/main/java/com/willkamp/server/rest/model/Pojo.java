package com.willkamp.server.rest.model;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

@ParametersAreNonnullByDefault
public class Pojo {
    private final String title;
    @Nullable
    private final String description;

    public Pojo(String title) {
        this(title, null);
    }

    public Pojo(String title, @Nullable String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    @Nonnull
    public Optional<String> getDescription() {
        return Optional.ofNullable(description);
    }
}
