module engine.core {
    requires static engine.annotations;

    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;
    requires java.datatransfer;
    requires java.desktop;
    requires java.management;
    requires static lombok;
    requires org.jspecify;

    opens com.next.engine.scene to com.fasterxml.jackson.databind;
    opens com.next.engine.system to com.fasterxml.jackson.databind;
    opens com.next.engine.dto to com.fasterxml.jackson.databind;

    exports com.next.engine;
    exports com.next.engine.data;
    exports com.next.engine.event;
    exports com.next.engine.graphics;
    exports com.next.engine.graphics.awt;
    exports com.next.engine.io;
    exports com.next.engine.sound;
    exports com.next.engine.sound.jxsound;
    exports com.next.engine.system;
    exports com.next.engine.model;
    exports com.next.engine.scene;
    exports com.next.engine.physics;
    exports com.next.engine.dto;
    exports com.next.engine.animation;
    exports com.next.engine.util;
    exports com.next.engine.debug;
    exports com.next.engine.ui;
    exports com.next.engine.ui.node;
    exports com.next.engine.uij;
}