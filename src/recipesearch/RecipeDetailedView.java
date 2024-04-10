package recipesearch;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import se.chalmers.ait.dat215.lab2.Recipe;

import java.io.IOException;

public class RecipeDetailedView extends AnchorPane {

    private RecipeSearchController parentController;
    private Recipe recipe;
    @FXML
    private ImageView listItemImage;
    @FXML private Text listItemTitle;

    public RecipeDetailedView(Recipe recipe, RecipeSearchController recipeSearchController){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("recipe_listitem.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.recipe = recipe;
        this.parentController = recipeSearchController;

        this.listItemImage.setImage(this.recipe.getFXImage());
        this.listItemTitle.setText(this.recipe.getName());
    }
}
