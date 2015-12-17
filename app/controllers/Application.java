package controllers;

import play.*;
import play.mvc.*;
import play.data.*;
import play.data.Form.*;
import play.mvc.Http.Context;

import views.html.*;

// Import required classes
import java.util.ArrayList;
import java.util.List;

// Import models
import models.*;

public class Application extends Controller {

//    public Result listProducts() {
//
//				return redirect(routes.ProductCtrl.listProducts(0));
//    }

    public Result index() {

        return ok(index.render());
    }


}
