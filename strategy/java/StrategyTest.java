public class StrategyTest {
    public static void main(String[] args) {
        PaySelector paySelector = new PaySelector();

        Currency curr = Currency.CNY;
        float amount = 500.3F;
        paySelector.getPayStrategy(curr, amount).pay(curr, amount);

        curr = Currency.USD;
        amount = 1500.67F;
        paySelector.getPayStrategy(curr, amount).pay(curr, amount);

        curr = Currency.CNY;
        amount = 2934.33F;
        paySelector.getPayStrategy(curr, amount).pay(curr, amount);

        curr = Currency.USD;
        amount = 5201;
        paySelector.getPayStrategy(curr, amount).pay(curr, amount);

        curr = Currency.CNY;
        amount = 10000;
        paySelector.getPayStrategy(curr, amount).pay(curr, amount);
    }
}
