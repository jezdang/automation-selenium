package org.openweathermap.api.apiobjects;

public interface StationFieldsEnum {

    enum stationFields { EXTERNAL_ID("external_id"),
        NAME("name"),
        LATITUDE("latitude"),
        LONGITUDE("longitude"),
        ALTITUDE("altitude");

    private String associatedText;

        stationFields(String associatedText) { this.associatedText = associatedText; }

        @Override
        public String toString() {
            return this.associatedText;
        }

    };
}
