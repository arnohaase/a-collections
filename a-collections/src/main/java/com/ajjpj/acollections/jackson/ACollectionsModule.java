package com.ajjpj.acollections.jackson;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.Module;


public class ACollectionsModule extends Module {

    @Override public String getModuleName () {
        return getClass().getSimpleName();
    }

    @Override public Version version () {
        return Version.unknownVersion();
    }

    @Override public void setupModule (SetupContext setupContext) {
        setupContext.addDeserializers(new Deserializers());
    }
}
