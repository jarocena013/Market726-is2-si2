package dataAccess;

import java.io.File;
import java.util.Date;

public class CreateSaleParameter {
	public String title;
	public String description;
	public int status;
	public float price;
	public Date pubDate;
	public String sellerEmail;
	public File file;

	public CreateSaleParameter(String title, String description, int status, float price, Date pubDate,
			String sellerEmail, File file) {
		this.title = title;
		this.description = description;
		this.status = status;
		this.price = price;
		this.pubDate = pubDate;
		this.sellerEmail = sellerEmail;
		this.file = file;
	}
}