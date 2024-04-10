
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
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;
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
    @FXML private Text displayMaxTime;

    RecipeDatabase db = RecipeDatabase.getSharedInstance();
    RecipeBackendController recipeBackendController = new RecipeBackendController();
    List<Recipe> recipeList;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        updateRecipeList();
        initMainIngredient();
        initCuisine();
        initDifficulty();
        initPrice();
        initTime();
    }

    private void updateRecipeList(){
        //Empty the flowpane
        recipeListFlowPane.getChildren().clear();

        //Anropa recipeBackendController för att hämta listan på alla recept (osorterad)
        recipeList = recipeBackendController.getRecipes();

        //För varje recept i listan skapa ett nytt recipeListitem och lägga till
        // det med metodanropet:
        for (int i = 0; i < recipeList.size(); i++){
            recipeListFlowPane.getChildren().add(new RecipeListItem(
                    recipeList.get(i), this));
        }
    }

    private void initMainIngredient() {
        //Set available items
        inputIngredient.getItems().addAll("Välj huvudingrediens", "Kött", "Fisk", "Kyckling", "Vegetarisk");
        //Choose default alternative to display
        inputIngredient.getSelectionModel().select("Välj huvudingrediens");

        inputIngredient.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                recipeBackendController.setMainIngredient(newValue);
                updateRecipeList();
            }
        });
    }

    private void initCuisine() {
        inputCuisine.getItems().addAll("Välj kök", "Sverige", "Grekland", "Indien", "Asien", "Afrika", "Frankrike");
        inputCuisine.getSelectionModel().select("Välj kök");

        inputCuisine.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                recipeBackendController.setCuisine(newValue);
                updateRecipeList();
            }
        });
    }

    private void initDifficulty() {
        //Combine all radio-buttons to one toggle group
        ToggleGroup difficultyToggleGroup = new ToggleGroup();
        inputDifficultyAll.setToggleGroup(difficultyToggleGroup);
        inputDifficultyEasy.setToggleGroup(difficultyToggleGroup);
        inputDifficultyMedium.setToggleGroup(difficultyToggleGroup);
        inputDifficultyHard.setToggleGroup(difficultyToggleGroup);
        //Default:
        inputDifficultyAll.setSelected(true);

        // Add listener to update backend when radio buttons are toggled
        difficultyToggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {

                if (difficultyToggleGroup.getSelectedToggle() != null) {
                    RadioButton selected = (RadioButton) difficultyToggleGroup.getSelectedToggle();
                    recipeBackendController.setDifficulty(selected.getText());
                    updateRecipeList();
                }
            }
        });
    }

    private void initPrice(){
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10000, 100, 10);

        inputMaxPrice.setValueFactory(valueFactory);

        inputMaxPrice.valueProperty().addListener(new ChangeListener<Integer>(){

            @Override
            public void changed(ObservableValue<? extends Integer> observableValue, Integer integer, Integer newValue) {
                recipeBackendController.setMaxPrice(newValue);
                updateRecipeList();
            }
        });

        inputMaxPrice.focusedProperty().addListener(new ChangeListener<Boolean>() {

            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {

                if(newValue){
                    //focusgained - do nothing
                }
                else{
                    Integer value = Integer.valueOf(inputMaxPrice.getEditor().getText());
                    recipeBackendController.setMaxPrice(value);
                    updateRecipeList();
                }

            }
        });
    }

    private void initTime(){
        // set default value
        inputMaxTime.setValue(60);
        inputMaxTime.setBlockIncrement(10f);
        inputMaxTime.blockIncrementProperty();
        inputMaxTime.snapToTicksProperty().set(true);

        inputMaxTime.valueProperty().addListener(new ChangeListener<Number>(){

            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                if(newValue != null && !newValue.equals(oldValue) && !inputMaxTime.isValueChanging()) {
                    int intTime = newValue.intValue();
                    recipeBackendController.setMaxTime(intTime);
                    System.out.println(inputMaxTime.getValue());
                    displayMaxTime.setText(newValue.toString().concat(" minuter"));
                    updateRecipeList();
                }
                if(newValue != null){

                }
            }
        });


    }

}