package liaoudi.xmoneynote.XCurrencySys;

/**
 * Created by billliao on 2018/6/2.
 */

public class XCurrencyObject {
    private String currency_name;
    private Double currency;

    public String getCurrency_name() {
        return currency_name;
    }

    public void setCurrency_name(String currency_name) {
        this.currency_name = currency_name;
    }

    public Double getCurrency() {
        return currency;
    }

    public void setCurrency(Double currency) {
        this.currency = currency;
    }

    public String getString(){
        String name = this.getCurrency_name();
        String currency = this.getCurrency().toString();
        return name+"[LIAOYUDI]"+currency;
    }

    public void recoverFromString(String input){
        String[] splits= input.split("\\[LIAOYUDI\\]");
        this.setCurrency_name(splits[0]);
        this.setCurrency(Double.parseDouble(splits[1]));
    }
}
