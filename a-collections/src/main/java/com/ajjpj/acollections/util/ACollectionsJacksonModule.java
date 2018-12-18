package com.ajjpj.acollections.util;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.Module;


public class ACollectionsJacksonModule extends Module {
    @Override public String getModuleName () {
        return getClass().getSimpleName();
    }

    @Override public Version version () {
        return null;
    }

    @Override public void setupModule (SetupContext setupContext) {
//        setupContext.addDeserializers();
    }
}
