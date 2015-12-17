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
    public Result join() {

        return ok(join.render());
    }
    public Result about() {

        return ok(about.render());
    }
    public Result gallery() {

        return ok(gallery.render());
    }

    public Result classes() {

        return ok(classes.render());
    }

    public Result boxing() {

        return ok(boxing.render());
    }
    public Result swimming() {

        return ok(swimming.render());
    }
    public Result weights() {

        return ok(weights.render());
    }
}
