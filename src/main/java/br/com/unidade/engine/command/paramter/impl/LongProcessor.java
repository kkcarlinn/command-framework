package br.com.pentamc.core.engine.command.paramter.impl;

import br.com.pentamc.core.engine.command.paramter.Processor;
import br.com.pentamc.core.engine.command.utils.CommandReflection;

public final class LongProcessor extends Processor<Long> {

    public Long process(Object sender, String supplied) {
        try {
            return Long.parseLong(supplied);
        } catch(Exception ex) {
            CommandReflection.sendMessage(sender, "§cO valor Long '" + supplied + "' é inválido.");
            return 0L;
        }
    }
}
