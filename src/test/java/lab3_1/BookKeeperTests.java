package lab3_1;

import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Date;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.invoicing.BookKeeper;
import pl.com.bottega.ecommerce.sales.domain.invoicing.Invoice;
import pl.com.bottega.ecommerce.sales.domain.invoicing.InvoiceFactory;
import pl.com.bottega.ecommerce.sales.domain.invoicing.InvoiceRequest;
import pl.com.bottega.ecommerce.sales.domain.invoicing.RequestItem;
import pl.com.bottega.ecommerce.sales.domain.invoicing.Tax;
import pl.com.bottega.ecommerce.sales.domain.invoicing.TaxPolicy;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

public class BookKeeperTests {

    private BookKeeper bookKeeper;
    private TaxPolicy taxPolicy;
    private InvoiceRequest invoiceRequest;
    private ClientData client;
    private RequestItem item;

    private Invoice invoice;
    private Tax tax;
    private Id id;

    @Before
    public void setUp() {
        taxPolicy = mock(TaxPolicy.class);
        id = Id.generate();
        client = new ClientData(id, "client");
        invoiceRequest = new InvoiceRequest(client);
        bookKeeper = new BookKeeper(new InvoiceFactory());
    }

    @Test
    public void shouldReturnInvoiceWithOnePosition() {
        ProductData productData = new ProductData(Id.generate(), new Money(new BigDecimal(1000), Currency.getInstance("EUR")), "standard",
                ProductType.STANDARD, new Date());
        int quantity = 20;
        Money totalCost = productData.getPrice()
                                     .multiplyBy(quantity);
        RequestItem item = new RequestItem(productData, quantity, totalCost);
        invoiceRequest.add(item);
        when(taxPolicy.calculateTax(any(ProductType.class), any(Money.class))).thenReturn(
                new Tax(new Money(new BigDecimal(1000), Currency.getInstance("EUR")), "TAX"));
        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
        assertThat(invoice.getItems()
                          .size(),
                Matchers.is(1));
        assertThat(invoice.getItems()
                          .get(0)
                          .getProduct(),
                Matchers.is(productData));
    }

    @Test
    public void shouldUseCalculateTaxMethodTwoTimesForInvoiceRequestWithTwoPositions() {
        ProductData productData = new ProductData(Id.generate(), new Money(new BigDecimal(1000), Currency.getInstance("EUR")), "standard",
                ProductType.STANDARD, new Date());
        int quantity = 20;
        Money totalCost = productData.getPrice()
                                     .multiplyBy(quantity);
        RequestItem item = new RequestItem(productData, quantity, totalCost);
        invoiceRequest.add(item);
        productData = new ProductData(Id.generate(), new Money(new BigDecimal(1000), Currency.getInstance("EUR")), "standard",
                ProductType.STANDARD, new Date());
        quantity = 10;
        totalCost = productData.getPrice()
                               .multiplyBy(quantity);
        item = new RequestItem(productData, quantity, totalCost);
        invoiceRequest.add(item);
        when(taxPolicy.calculateTax(any(ProductType.class), any(Money.class))).thenReturn(
                new Tax(new Money(new BigDecimal(1000), Currency.getInstance("EUR")), "TAX"));
        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
        verify(taxPolicy, times(2)).calculateTax(any(), any());
    }

    @Test
    public void shouldUseCalculateTaxMethodZeroTimesForInvoiceRequestWithNoPositions() {

        when(taxPolicy.calculateTax(any(ProductType.class), any(Money.class))).thenReturn(
                new Tax(new Money(new BigDecimal(1000), Currency.getInstance("EUR")), "TAX"));
        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
        verify(taxPolicy, times(0)).calculateTax(any(), any());
    }

}
