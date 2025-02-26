package tqs.deti.ua;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StocksPortfolioTest {
    @Mock
    private IStockmarketService stockMarket;

    @InjectMocks
    private StocksPortfolio stocksPortfolio;

    private Stock stock1;
    private Stock stock2;

    @BeforeEach
    public void setUp() {
        stock1 = new Stock("1", 10);
        stock2 = new Stock("2", 20);
        stocksPortfolio = new StocksPortfolio(stockMarket);
        stocksPortfolio.addStock(stock1);
        stocksPortfolio.addStock(stock2);
    }

    @AfterEach
    public void tearDown() {
        stocksPortfolio = null;
        stock1 = null;
        stock2 = null;
    }

    @DisplayName("Test the total value of the stocks in the portfolio")
    @Test
    void testTotalValue() {
        // mock the behavior of the stockMarket
        when(stockMarket.lookUpStock(stock1.getLabel())).thenReturn(10.0);

        double result = stocksPortfolio.totalValue();

        assertThat(result, equalTo(10 * 10.0));
        verify(stockMarket, times(1)).lookUpStock(stock1.getLabel());

    }

    @DisplayName("Test the Most Valuable Stocks")
    @Test
    void testMostValuableStocks() {
        // mock the behavior of the stockMarket
        when(stockMarket.lookUpStock(stock1.getLabel())).thenReturn(10.0);
        when(stockMarket.lookUpStock(stock2.getLabel())).thenReturn(20.0);


        assertThat(stocksPortfolio.mostValuableStocks(1).getFirst(), equalTo(stock2));
        assertThat(stocksPortfolio.mostValuableStocks(2).getFirst(), equalTo(stock2));


        // verify(stockMarket, times(2)).lookUpStock(anyString()); // not realy a issue, but we can do better
    }

    @DisplayName("Test the bounds in the  Most Valuable Stocks")
    @Test
    void testMostValuableStocksBounds() {
        // mock the behavior of the stockMarket
        when(stockMarket.lookUpStock(stock1.getLabel())).thenReturn(10.0);
        when(stockMarket.lookUpStock(stock2.getLabel())).thenReturn(20.0);

        assertThat(stocksPortfolio.mostValuableStocks(0).size(), equalTo(0));

        assertThat(stocksPortfolio.mostValuableStocks(4).size(), equalTo(2));
        assertThat(stocksPortfolio.mostValuableStocks(-4).size(), equalTo(0));
    }




}
