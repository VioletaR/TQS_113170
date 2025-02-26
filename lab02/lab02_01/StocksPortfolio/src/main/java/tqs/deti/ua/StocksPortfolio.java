package tqs.deti.ua;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class StocksPortfolio {
    private IStockmarketService stockMarket;
    private List<Stock> stocks;

    public StocksPortfolio(IStockmarketService stockMarket) {
        this.stockMarket = stockMarket;
        stocks = new ArrayList<Stock>();
    }

    public void addStock(Stock s) {
        stocks.add(s);
    }

    public double totalValue() {
        double value = 0.0;
        for (Stock s : stocks) {
            value += stockMarket.lookUpStock(s.getLabel()) * s.getQuantity();
        }
        return value;
    }


    public List<Stock> mostValuableStocks(int topN) {
        if (topN <= 0) return new ArrayList<>(); // Return empty list for invalid input

        List<Stock> copy = new ArrayList<>(stocks);

        // Sort stocks by value in descending order
        copy.sort((s1, s2) -> Double.compare(
                stockMarket.lookUpStock(s2.getLabel()) * s2.getQuantity(),
                stockMarket.lookUpStock(s1.getLabel()) * s1.getQuantity()
        ));

        // Return the top N stocks (or all available if topN > stocks.size())
        return copy.subList(0, Math.min(topN, copy.size()));
    }


}
