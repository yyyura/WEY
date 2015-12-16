package models;

import java.util.*;
import javax.persistence.*;

import play.data.format.*;
import play.data.validation.*;

import com.avaje.ebean.*;

// Product entity managed by Ebean
@Entity
public class Product extends Model {

    // Fields - note that these are defined as public and not private
    // During compile time, The Play Framework
    // automatically generates getters and setters


    @Id
    public Long id;

    @ManyToOne
    public Category category;

    @Constraints.Required
    public String name;

    @Constraints.Required
    public String description;

    @Constraints.Required
    public int	stock;

    @Constraints.Required
    public double price;

    // Default constructor
    public  Product() {
    }

    // Constructor to initialise object
    public  Product(Long id, Category category, String name, String description, int stock, double price){
        this.id = id;
        this.category = category;
        this.name = name;
        this.description = description;
        this.stock = stock;
        this.price = price;
    }
	
		//Generic query helper for entity Computer with id Long
    public static Finder<Long,Product> find = new Finder<Long,Product>(Long.class, Product.class);

		//Find all Products in the database
		public static List<Product> findAll() {
			return Product.find.all();
		}
	
}

