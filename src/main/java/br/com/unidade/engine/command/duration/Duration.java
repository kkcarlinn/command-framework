package br.com.unidade.engine.command.duration;

import lombok.Data;

@Data
public final class Duration {

    private final String parsed;
    private final long time;
}
