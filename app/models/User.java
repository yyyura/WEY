package models;

import java.util.*;
import javax.persistence.*;
import play.data.format.*;
import play.data.validation.*;
import com.avaje.ebean.*;

//https://www.playframework.com/documentation/2.2.x/JavaGuide4

// Product entity managed by Ebean
@Entity
public class User extends Model {

    @Id
    public String email;

    @Constraints.Required
    public String name;

    @Constraints.Required
    public String password;

    @Constraints.Required
    public String	role;

    // Default constructor
    public  User() {
    }
    // Constructor to initialise object
    public  User(String email, String name, String password, String role) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.role = role;
    }

		//Generic query helper for entity User with unique id String
    public static Finder<String,User> find = new Finder<String,User>(String.class, User.class);

		//Find all Products in the database
		public static List<User> findAll() {
			return User.find.all();
		}
		// Static method to authenticate based on username and password
		// Returns user object if found, otherwise NULL
		public	static User authenticate(String email, String password) {
			// If found return the user object with matching username and password
			return find.where().eq("email", email).eq("password", password).findUnique();
		}

		// Check if a user is logged in (by id)
		public static User getLoggedIn(String id) {
			if (id == null)
					return null;
			else
				// Find user by id and return object
				return find.byId(id);
		}
}

