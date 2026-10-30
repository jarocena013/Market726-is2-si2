package adapter;

import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import domain.Seller;
import domain.Sale;

public class SellerAdapter extends AbstractTableModel {
	protected Seller seller;
	protected String[] columnNames = new String[] { "title", "description", "price" };
	protected Vector<Sale> sales;

	public SellerAdapter(Seller s) {
		this.seller = s;
		sales = new Vector<Sale>(s.getSales());
	}

	public int getColumnCount() {
		// Challenge!
		return columnNames.length;
	}

	public String getColumnName(int i) {
		// Challenge!
		return columnNames[i];
	}

	public int getRowCount() {
		// Challenge!
		return seller.getSales().size();
	}

	public Object getValueAt(int row, int col) {
		// Challenge!
		if (col == 0) {
			return sales.get(row).getTitle();
		} else if (col == 1) {
			return sales.get(row).getDescription();
		} else {
			return sales.get(row).getPrice();
		}
	}
}
