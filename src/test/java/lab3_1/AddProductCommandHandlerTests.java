package lab3_1;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import java.util.Date;

import org.junit.Before;
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
    private Reservation spy;
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
        productCommand = new AddProductCommand(new Id("1"), new Id("2"), 3);
        reservationRepository = mock(ReservationRepository.class);
        productRepository = mock(ProductRepository.class);
        clientRepository = mock(ClientRepository.class);
        suggestionService = mock(SuggestionService.class);
        reservation = new Reservation(Id.generate(), Reservation.ReservationStatus.OPENED, new ClientData(Id.generate(), "Client"),
                new Date());
        product = spy(new Product(Id.generate(), new Money(10), "Product", ProductType.STANDARD));
        equivalent = new Product(Id.generate(), new Money(20), "Equivalent", ProductType.STANDARD);
        spy = spy(reservation);
        systemContext = new SystemContext();
        Whitebox.setInternalState(handler, "reservationRepository", reservationRepository);
        Whitebox.setInternalState(handler, "productRepository", productRepository);
        Whitebox.setInternalState(handler, "suggestionService", suggestionService);
        Whitebox.setInternalState(handler, "clientRepository", clientRepository);
        Whitebox.setInternalState(handler, "systemContext", systemContext);
        client = new Client();
    }
}
