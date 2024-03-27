package recipesearch;

import javafx.event.ActionEvent;
import se.chalmers.ait.dat215.lab2.*;

import java.util.List;

public class RecipeBackendController {
    private String difficulty;
    private int maxTime;
    private String cuisine;
    private int maxPrice;
    private String mainIngredient;

    RecipeDatabase db = RecipeDatabase.getSharedInstance();
    //finns redan i RecipeSearchController.java som vi fått från läraren

    public List<Recipe> getRecipes() {
        return db.search(new SearchFilter(difficulty, maxTime, cuisine, maxPrice, mainIngredient));
    }

    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }
    public void setMainIngredient(String mainIngredient) {
        this.mainIngredient = mainIngredient;
    }
    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
    public void setMaxPrice(int maxPrice) {
        if (maxPrice < 0) maxPrice = 0;
        this.maxPrice = maxPrice;
    }

    public void setMaxTime(int maxTime) {
        if (maxTime < 0) maxTime = 0;
        this.maxTime = maxTime;
    }

}
