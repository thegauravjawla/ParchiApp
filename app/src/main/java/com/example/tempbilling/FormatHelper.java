package com.example.tempbilling;

public class FormatHelper {

    public String money(Float amount) {
        return Integer.toString(Math.round(amount));
    }

    public String total(Float amount) {
        return Integer.toString(roundMoney(amount));
    }

    public String weight(Float wt) {
        return String.format("%.3f", roundToOneDecimal(wt));
    }

    public String rate(Float rt) {
        return String.format("%.1f", roundToOneDecimal(rt));
    }

    //0 at once place
    private int roundMoney(float x) {
        int n = Math.round(x);
        int a = (n / 10) * 10;
        int b = a + 10;
        return (n - a > b - n)? b : a;
    }

    private float roundToOneDecimal(float x) {
        int n = Math.round(x*10);
        float a = n/10.0f;
        float b = a + 0.1f;
        return (x - a > b - x)? b : a;
    }
}
