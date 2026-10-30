package adapter;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import domain.*;
public class SellerTable extends JFrame {
	private Seller seller;
	private JTable tabla;

	public SellerTable(Seller seller) {
		super(seller.getName() + "’s	sales	");
		this.setBounds(100, 100, 700, 200);
		this.seller = seller;
		SellerAdapter adapt = new SellerAdapter(seller);
		tabla = new JTable(adapt);
		tabla.setPreferredScrollableViewportSize(new Dimension(500, 70));
		// Creamos un JscrollPane y le agregamos la JTable
		JScrollPane scrollPane = new JScrollPane(tabla); // Agregamos el JScrollPane al contenedor
		getContentPane().add(scrollPane, BorderLayout.CENTER);
	}
}
