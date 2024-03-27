
package recipesearch;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.FlowPane;
import se.chalmers.ait.dat215.lab2.Recipe;
import se.chalmers.ait.dat215.lab2.RecipeDatabase;
import se.chalmers.ait.dat215.lab2.SearchFilter;


public class RecipeSearchController implements Initializable {

    @FXML private ScrollPane resultsPane;
    @FXML private FlowPane recipeListFlowPane;
    @FXML private ComboBox inputIngredient;
    @FXML private ComboBox inputCuisine;
    @FXML private RadioButton inputDifficultyAll;
    @FXML private RadioButton inputDifficultyEasy;
    @FXML private RadioButton inputDifficultyMedium;
    @FXML private RadioButton inputDifficultyHard;
    @FXML private Spinner inputMaxPrice;
    @FXML private Slider inputMaxTime;

    RecipeDatabase db = RecipeDatabase.getSharedInstance();
    RecipeBackendController recipeBackendController = new RecipeBackendController();
    List<Recipe> recipeList;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        updateRecipeList();
    }

    private void updateRecipeList(){
        //Empty the flowpane
        if(recipeListFlowPane != null){
            recipeListFlowPane.getChildren().clear();
        }

        //Anropa recipeBackendController för att hämta listan på alla recept (osorterad)
        recipeList = recipeBackendController.getRecipes();

        //För varje recept i listan skapa ett nytt recipeListitem och lägga till
        // det med metodanropet:
        for (int i = 0; i < recipeList.size(); i++){
            recipeListFlowPane.getChildren().add(new RecipeListItem(
                    recipeList.get(i), this));
        }

    }



}