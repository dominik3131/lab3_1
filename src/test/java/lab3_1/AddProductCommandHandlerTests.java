package lab3_1;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.reflection.Whitebox;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.application.api.command.AddProductCommand;
import pl.com.bottega.ecommerce.sales.application.api.handler.AddProductCommandHandler;
import pl.com.bottega.ecommerce.sales.domain.client.Client;
import pl.com.bottega.ecommerce.sales.domain.client.ClientRepository;
import pl.com.bottega.ecommerce.sales.domain.equivalent.SuggestionService;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.Product;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductRepository;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sales.domain.reservation.Reservation;
import pl.com.bottega.ecommerce.sales.domain.reservation.ReservationRepository;
import pl.com.bottega.ecommerce.sharedkernel.Money;
import pl.com.bottega.ecommerce.system.application.SystemContext;

public class AddProductCommandHandlerTests {

    private ReservationRepository reservationRepository;
    private ProductRepository productRepository;
    private Reservation reservation;
    private Reservation reservationSpy;
    private Product product;
    private AddProductCommandHandler handler;
    private AddProductCommand productCommand;
    private ClientRepository clientRepository;
    private Client client;
    private SuggestionService suggestionService;
    private Product equivalent;
    private SystemContext systemContext;

    @Before
    public void setUp() {
        handler = new AddProductCommandHandler();
        productCommand = new AddProductCommand(new Id("1"), new Id("2"), 5);
        reservationRepository = mock(ReservationRepository.class);
        productRepository = mock(ProductRepository.class);
        clientRepository = mock(ClientRepository.class);
        suggestionService = mock(SuggestionService.class);
        reservation = new Reservation(Id.generate(), Reservation.ReservationStatus.OPENED, new ClientData(Id.generate(), "Client"),
                new Date());
        product = spy(new Product(Id.generate(), new Money(10), "Product", ProductType.STANDARD));
        equivalent = new Product(Id.generate(), new Money(20), "Equivalent", ProductType.STANDARD);
        reservationSpy = spy(reservation);
        systemContext = new SystemContext();
        Whitebox.setInternalState(handler, "reservationRepository", reservationRepository);
        Whitebox.setInternalState(handler, "productRepository", productRepository);
        Whitebox.setInternalState(handler, "suggestionService", suggestionService);
        Whitebox.setInternalState(handler, "clientRepository", clientRepository);
        Whitebox.setInternalState(handler, "systemContext", systemContext);
        client = new Client();
    }

    @Test
    public void shouldLoadReservationOnce() {
        when(reservationRepository.load(any(Id.class))).thenReturn(reservationSpy);
        when(productRepository.load(any(Id.class))).thenReturn(product);
        handler.handle(productCommand);
        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    public void shouldUseAddMethodOnce() {
        when(reservationRepository.load(any(Id.class))).thenReturn(reservationSpy);
        when(productRepository.load(any(Id.class))).thenReturn(product);
        handler.handle(productCommand);
        verify(reservationSpy, times(1)).add(product, 5);
    }
}
