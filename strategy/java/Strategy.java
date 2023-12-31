import java.util.ArrayList;
import java.util.List;

enum Currency {
    USD,
    CNY
}

// ============================================================

// 支付策略
interface PayStrategy {
    boolean shouldBeChosen(Currency curr, float amount);

    void pay(Currency curr, float amount);
}

class PayByDebitCard implements PayStrategy {
    @Override
    public boolean shouldBeChosen(Currency curr, float amount) {
        return amount <= 1000;
    }

    @Override
    public void pay(Currency curr, float amount) {
        System.out.printf("【DebitCard】支付款项 %s %.2f。\n", curr, amount);
    }
}

class PayByPayPal implements PayStrategy {
    @Override
    public boolean shouldBeChosen(Currency curr, float amount) {
        return curr == Currency.USD && amount > 1000 && amount < 3000;
    }

    @Override
    public void pay(Currency curr, float amount) {
        System.out.printf("【PayPal】支付款项 %s %.2f。\n", curr, amount);
    }
}

class PayByAliPay implements PayStrategy {
    @Override
    public boolean shouldBeChosen(Currency curr, float amount) {
        return curr == Currency.CNY && amount > 1000 && amount < 3000;
    }

    @Override
    public void pay(Currency curr, float amount) {
        System.out.printf("【AliPay】支付款项 %s %.2f。\n", curr, amount);
    }
}

class PayByCreditCard implements PayStrategy {
    @Override
    public boolean shouldBeChosen(Currency curr, float amount) {
        return amount >= 3000;
    }

    @Override
    public void pay(Currency curr, float amount) {
        System.out.printf("【CreditCard】支付款项 %s %.2f。\n", curr, amount);
    }
}

class PayByCash implements PayStrategy {
    @Override
    public boolean shouldBeChosen(Currency curr, float amount) {
        return false;
    }

    @Override
    public void pay(Currency curr, float amount) {
        System.out.printf("【Cash】支付款项 %s %.2f。\n", curr, amount);
    }
}

// ============================================================

class PaySelector {
    private static final List<PayStrategy> payStrategyList = new ArrayList<>(4);

    static {
        payStrategyList.add(new PayByDebitCard());
        payStrategyList.add(new PayByPayPal());
        payStrategyList.add(new PayByAliPay());
        payStrategyList.add(new PayByCreditCard());
        payStrategyList.add(new PayByCash());
    }

    public PayStrategy getPayStrategy(Currency curr, float amount) {
        for (PayStrategy payStrategy : payStrategyList) {
            if (payStrategy.shouldBeChosen(curr, amount)) {
                return payStrategy;
            }
        }
        return payStrategyList.getLast();
    }
}