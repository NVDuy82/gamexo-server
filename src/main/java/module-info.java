module com.example.xoserver {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.common;
    requires com.google.gson;
    requires java.sql;
    
    
    opens com.example.xoserver to javafx.fxml;
    exports com.example.xoserver;
    exports com.example.xoserver.messageprocessor;
    opens com.example.xoserver.messageprocessor to javafx.fxml;
    exports com.example.xoserver.messageprocessor.factory;
    opens com.example.xoserver.messageprocessor.factory to javafx.fxml;
    exports com.example.shared;
    opens com.example.shared to javafx.fxml;
    exports com.example.xoserver.cache;
    opens com.example.xoserver.cache to javafx.fxml;
    exports com.example.xoserver.messageprocessor.room;
    opens com.example.xoserver.messageprocessor.room to javafx.fxml;
    exports com.example.xoserver.messageprocessor.log;
    opens com.example.xoserver.messageprocessor.log to javafx.fxml;
    exports com.example.xoserver.messageprocessor.match;
    opens com.example.xoserver.messageprocessor.match to javafx.fxml, com.google.gson;
    exports com.example.xoserver.data;
    opens com.example.xoserver.data to com.google.gson;
    exports com.example.xoserver.model;
    opens com.example.xoserver.model to javafx.fxml, com.google.gson;
}