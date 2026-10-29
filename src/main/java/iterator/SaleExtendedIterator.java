package iterator;

import java.util.List;
import domain.Sale;

public class SaleExtendedIterator implements ExtendedIterator<Sale> {
    private List<Sale> sales;
    private int position;

    public SaleExtendedIterator(List<Sale> sales) {
        this.sales = sales;
        this.position = 0; 
    }

    @Override
    public boolean hasNext() {
        return position < sales.size();
    }

    @Override
    public Sale next() {
        return sales.get(position++);
    }

    @Override
    public boolean hasPrevious() {
        return position >= 0;
    }

    @Override
    public Sale previous() {
        return sales.get(position--);
    }

    @Override
    public void goFirst() {
        position = 0;
    }

    @Override
    public void goLast() {
        if (sales != null && !sales.isEmpty()) {
            position = sales.size() - 1;
        } else {
            position = 0;
        }
    }
}
