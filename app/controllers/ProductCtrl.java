package controllers;

import play.*;
import play.mvc.*;
import play.data.*;
import play.data.Form.*;
import play.mvc.Http.Context;

import play.mvc.Http.*;
import play.mvc.Http.MultipartFormData.FilePart;
import java.io.*;
import java.io.File;
import javax.activation.MimetypesFileTypeMap;

import org.im4java.process.ProcessStarter;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IMOperation;

import views.html.*;

// Import required classes
import java.util.ArrayList;
import java.util.List;

// http://superuser.com/questions/163818/how-to-install-rmagick-on-ubuntu-10-04
// http://im4java.sourceforge.net/

// Import models
import models.*;

public class ProductCtrl extends Controller {

		// Show a list of all products
    public Result listProducts(Long cat) {
			// Get list of categories
			List<Category> categories = Category.find.where().orderBy("name asc").findList();
			// Instansiate products, an Arraylist of products			
			List<Product> products = new ArrayList<Product>();
		
			if (cat == 0) {
				// Get the list of ALL products
				products = Product.findAll();
			}
			else {
				// Get products for the selected category
				// Each category object contains a list of products
				for (int i = 0; i < categories.size(); i++) {
					if (categories.get(i).id == cat) {
						products = categories.get(i).products;
						break;
					}
				}
			}
			// Pass the list to the index view and render
		  return ok(listProducts.render(categories, products, User.getLoggedIn(session().get("email"))));
    }

		// Load the add product view
		// Display an empty form in the view
		@Security.Authenticated(Secured.class)
		public Result addProduct() {
			// Instantiate a form object based on the Product class
			Form<Product> addProductForm = Form.form(Product.class);
			
			// Render the Add Product View, passing the form object
			return ok(addProduct.render(addProductForm, User.getLoggedIn(session().get("email"))));
		}

		// Handle the form data when a new product is submitted
		@Security.Authenticated(Secured.class)
		public Result addProductSubmit() {

			String saveImageMsg;

			// Create a product form object (to hold submitted data)
			// 'Bind' the object to the submitted form (this copies the filled form)
			Form<Product> newProductForm = Form.form(Product.class).bindFromRequest();	

				// Check for errors (based on Product class annotations)	
        if(newProductForm.hasErrors()) {
						// Display the form again
            return badRequest(addProduct.render(newProductForm, User.getLoggedIn(session().get("email"))));
        }
				// Save the Product using Ebean (remember Product extends Model)
        newProductForm.get().save();

				// Get image data
				MultipartFormData data = request().body().asMultipartFormData();
				FilePart uploaded = data.getFile("upload");
				
				// Call the save file method, passing the new product id and the uploaded data
				saveImageMsg = saveFile(newProductForm.get().id, uploaded);

        flash("success", "Product " + newProductForm.get().name + " has been created" + " " + saveImageMsg);
			
			// Render the Add Product View, passing the form object
			return redirect(routes.ProductCtrl.listProducts(0));
		}

		// Update a product by ID
		// called when edit button is pressed
		@Security.Authenticated(Secured.class)
		public Result updateProduct(Long id) {

				// Create a form based on the Product class
				// Fill the form with product object matching id
				// Use the finder to find the object by ID in the DB
        Form<Product> productForm = Form.form(Product.class).fill(
						Product.find.byId(id)
        );
				// Render the updateProduct view
				// pass the id and form as parameters
        return ok(updateProduct.render(id, productForm, User.getLoggedIn(session().get("email"))));					
		}

		// Handle the form data when an updated product is submitted
		@Security.Authenticated(Secured.class)
		public Result updateProductSubmit(Long id) {

			String saveImageMsg;

			// Create a product form object (to hold submitted data)
			// 'Bind' the object to the submitted form (this copies the filled form)
			Form<Product> updateProductForm = Form.form(Product.class).bindFromRequest();	

				// Check for errors (based on Product class annotations)	
        if(updateProductForm.hasErrors()) {
						// Display the form again
            return badRequest(updateProduct.render(id, updateProductForm, User.getLoggedIn(session().get("email"))));
        }
				// Update the Product (using its ID to select the existing object))
        Product p = updateProductForm.get();
				p.id = id;
				p.update();

				// Get image data
				MultipartFormData data = request().body().asMultipartFormData();
				FilePart image = data.getFile("upload");

				saveImageMsg = saveFile(p.id, image);

				// Add a success message to the flash session
        flash("success", "Product " + updateProductForm.get().name + " has been updates" + " " + saveImageMsg);
			
			// Render the Add Product View, passing the form object
			return redirect(routes.ProductCtrl.listProducts(0));
		}

    // Delete Product
		@Security.Authenticated(Secured.class)
    public Result deleteProduct(Long id) {
				// Call delete method
        Product.find.ref(id).delete();
        
				// Add message to flash session 
				flash("success", "Product has been deleted");

				// Redirect home
				return redirect(routes.ProductCtrl.listProducts(0));
    }

		public String saveFile(Long id, FilePart image) {

				// If the image exists
				if (image != null) {
					
					// Get mimetype (file type) from the image
					String mimeType = image.getContentType();
					
					// Check if uploaded file is an image
					if (mimeType.startsWith("image/")) {

						// Create file from uploaded image
						File file = image.getFile();

						// create ImageMagick command instance
						ConvertCmd cmd = new ConvertCmd();

						// create the operation, add images and operators/options
						IMOperation op = new IMOperation();

							// Get the uploaded image file
							op.addImage(file.getAbsolutePath());

							// Resize using height and width constraints
							op.resize(300,200);

							// Save the  image
							op.addImage("public/images/productImages/" + id + ".jpg");

						// thumbnail
						IMOperation thumb = new IMOperation();
							// Get the uploaded image file
							thumb.addImage(file.getAbsolutePath());
							thumb.thumbnail(60);
							// Save the  image
							thumb.addImage("public/images/productImages/thumbnails/" + id + ".jpg");

						// execute the operation
						try{
							cmd.run(op);
							cmd.run(thumb);
						}
						catch(Exception e){
							e.printStackTrace();
						}				
						return " and image saved";
					}
				}
				return "image file missing";
		} 
}
