package recipesearch;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import se.chalmers.ait.dat215.lab2.Recipe;
import javafx.util.*;

import javax.swing.*;
import java.io.IOException;

public class RecipeListItem extends AnchorPane {
    private RecipeSearchController parentController;
    private Recipe recipe;

//    FXML private AnchorPane listItem;
    @FXML private ImageView listItemImage;
    @FXML private ImageView listItemCuisineIcon;
    @FXML private ImageView listItemIngredientIcon;
    @FXML private ImageView listItemDifficultyIcon;
    @FXML private ImageView listItemTimeIcon;
    @FXML private Text listItemTitle;
    @FXML private Text listItemTime;
    @FXML private Text listItemPrice;
    @FXML private Text listItemDescription;


    public RecipeListItem(Recipe recipe, RecipeSearchController recipeSearchController){
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

        this.listItemImage.setImage(this.parentController.getSquareImage(this.recipe.getFXImage()));
        this.listItemCuisineIcon.setImage(this.parentController.getCuisineImage(this.recipe.getCuisine()));
        this.listItemIngredientIcon.setImage(this.parentController.getMainIngredientImage(this.recipe.getMainIngredient()));
        this.listItemDifficultyIcon.setImage(this.parentController.getDifficultyImage(this.recipe.getDifficulty()));

        String iconPath = "RecipeSearch/resources/icon_time.png";
        Image icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
        this.listItemTimeIcon.setImage(icon);

        this.listItemTitle.setText(this.recipe.getName());
        this.listItemTime.setText(recipe.getTime() + " minuter");
        this.listItemPrice.setText(recipe.getPrice() + " kr");
        this.listItemDescription.setText(recipe.getDescription());
    }

    @FXML
    protected void onClick(Event event){
        parentController.openRecipeView(recipe);
    }

}