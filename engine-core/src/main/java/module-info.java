module engine.core {
    requires static engine.annotations;

    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;
    requires java.datatransfer;
    requires java.desktop;
    requires java.management;
    requires static lombok;
    requires org.jspecify;

    exports com.next.system to com.fasterxml.jackson.databind;
    exports com.next.world to com.fasterxml.jackson.databind;
}