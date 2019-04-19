package pl.com.bottega.cqrs.command.builders;

import java.math.BigDecimal;

import pl.com.bottega.ecommerce.sales.domain.invoicing.Tax;
import pl.com.bottega.ecommerce.sharedkernel.Money;

public class TaxBuilder {

    private Money amount = new Money(BigDecimal.ZERO);
    private String desc = "Tax_Description";

    public TaxBuilder() {};

    public TaxBuilder description(String description) {
        this.desc = description;
        return this;
    }

    public TaxBuilder amount(Money amount) {
        this.amount = amount;
        return this;
    }

    public Tax build() {
        return new Tax(amount, desc);
    }
}
