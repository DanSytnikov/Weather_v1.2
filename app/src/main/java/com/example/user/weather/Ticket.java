package com.example.user.weather;

public class Ticket {
    String wDate;
    Integer wTemp;
    Double wPressure;
    Integer wHumidity;
    String wDescr;
    double wWind;
    String wIcon;

    Ticket (String _wDate, Integer _wTemp, Double _wPressure, Integer _wHumidity, String _wDescr, Double _wWind, String _wIcon){
        wDate = _wDate;
        wTemp = _wTemp;
        wPressure = _wPressure;
        wHumidity =_wHumidity;
        wDescr = _wDescr;
        wWind = _wWind;
        wIcon = _wIcon;
    }

}
