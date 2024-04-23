
package recipesearch;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;
import javafx.util.Callback;
import se.chalmers.ait.dat215.lab2.Ingredient;
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
    @FXML private ImageView detailedViewRecipeImage;
    @FXML private Text detailedViewRecipeTitle;
    @FXML private ImageView detailedViewIngredientIcon;
    @FXML private ImageView detailedViewCuisineIcon;
    @FXML private ImageView detailedViewTimeIcon;
    @FXML private ImageView detailedViewDifficultyIcon;
    @FXML private Text detailedViewTimeLabel;
    @FXML private Text detailedViewPriceLabel;
    @FXML private TextArea detailedViewDescription;
    @FXML private TextArea detailedViewInstruction;
    @FXML private Text detailedViewServings;
    @FXML private TextArea detailedViewIngredients;

    @FXML private Button detailedViewCloseButton;
    @FXML private AnchorPane detailedView;
    @FXML private SplitPane searchView;
    @FXML private ImageView detailedViewCloseIcon;


    RecipeDatabase db = RecipeDatabase.getSharedInstance();
    RecipeBackendController recipeBackendController = new RecipeBackendController();
    List<Recipe> recipeList;
    private Map<String, RecipeListItem> recipeListItemMap = new HashMap<String,RecipeListItem>();
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        for(Recipe recipe : recipeBackendController.getRecipes()){
            RecipeListItem recipeListItem = new RecipeListItem(recipe,this);
            recipeListItemMap.put(recipe.getName(), recipeListItem);
        }

        updateRecipeList();
        initMainIngredient();
        initCuisine();
        initDifficulty();
        initPrice();
        initTime();

        populateMainIngredientComboBox();
        populateCuisineComboBox();
    }

    private void updateRecipeList(){
        //Empty the flowpane
        recipeListFlowPane.getChildren().clear();

        //Anropa recipeBackendController för att hämta listan på alla recept (osorterad)
        recipeList = recipeBackendController.getRecipes();
        
        //För varje recept i listan skapa ett nytt recipeListitem och lägga till
        // det med metodanropet:
        for (Recipe recipe : recipeList){
            System.out.println(recipe.getName());
            System.out.println(recipeListItemMap.get(recipe.getName()));
            System.out.println(recipeListItemMap.isEmpty());
            recipeListFlowPane.getChildren().add(recipeListItemMap.get(recipe.getName()));
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
        inputMaxTime.setBlockIncrement(10);
        inputMaxTime.blockIncrementProperty();
        inputMaxTime.snapToTicksProperty().set(true);

        inputMaxTime.valueProperty().addListener(new ChangeListener<Number>(){

            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                if(newValue != null && !newValue.equals(oldValue) && !inputMaxTime.isValueChanging()) {
                    int intTime = newValue.intValue();
                    recipeBackendController.setMaxTime(intTime);
                    String timeString = newValue.toString();
                    displayMaxTime.setText(timeString.substring(0, timeString.length()-2).concat(" minuter"));
                    updateRecipeList();
                }
            }
        });
    }

    private void populateRecipeDetailView(Recipe recipe){
        String timeIconPath = "RecipeSearch/resources/icon_time.png";
        String closeIconPath = "RecipeSearch/resources/icon_close.png";

        //tar ett recept argument och som uppdaterar
        // komponenterna i denna panel baserat på receptet
        detailedViewRecipeTitle.setText(recipe.getName());
        detailedViewRecipeImage.setImage(getSquareImage(recipe.getFXImage()));
        detailedViewTimeLabel.setText(recipe.getTime() + " minuter");
        detailedViewPriceLabel.setText(recipe.getPrice() + " kr");
        detailedViewServings.setText(recipe.getServings() + " portioner");

        detailedViewDescription.setText(recipe.getDescription());
        detailedViewInstruction.setText(recipe.getInstruction());
        detailedViewIngredients.setText(formatIngredients(recipe.getIngredients()));

        detailedViewIngredientIcon.setImage(getMainIngredientImage(recipe.getMainIngredient()));
        detailedViewCuisineIcon.setImage(getCuisineImage(recipe.getCuisine()));
        detailedViewDifficultyIcon.setImage(getDifficultyImage(recipe.getDifficulty()));
        detailedViewTimeIcon.setImage(new Image(getClass().getClassLoader().getResourceAsStream(timeIconPath)));
        detailedViewCloseIcon.setImage(new Image(getClass().getClassLoader().getResourceAsStream(closeIconPath)));

    }

    @FXML
    public void closeRecipeView(){
        searchView.toFront();
    }

    public void openRecipeView(Recipe recipe){
        populateRecipeDetailView(recipe);
        detailedView.toFront();
    }

    private void populateMainIngredientComboBox(){
        Callback<ListView<String>, ListCell<String>> cellFactory = new Callback<ListView<String>, ListCell<String>>() {

            @Override public ListCell<String> call(ListView<String> p) {

                return new ListCell<String>() {

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);

                        setText(item);

                        if (item == null || empty) {
                            setGraphic(null);
                        }

                        else {
                            Image icon = null;
                            String iconPath;
                            try {
                                switch (item){

                                    case "Kött":
                                        iconPath = "RecipeSearch/resources/icon_main_meat.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Fisk":
                                        iconPath = "RecipeSearch/resources/icon_main_fish.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Kyckling":
                                        iconPath = "RecipeSearch/resources/icon_main_chicken.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Vegetarisk":
                                        iconPath = "RecipeSearch/resources/icon_main_veg.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                }
                            } catch(NullPointerException ex) {
                                //This should never happen in this lab but could load a default image in case of a NullPointer
                            }
                            ImageView iconImageView = new ImageView(icon);
                            iconImageView.setFitHeight(12);
                            iconImageView.setPreserveRatio(true);
                            setGraphic(iconImageView);
                        }
                    }
                };
            }
        };
        inputIngredient.setButtonCell(cellFactory.call(null));
        inputIngredient.setCellFactory(cellFactory);
    }

    private void populateCuisineComboBox(){
        Callback<ListView<String>, ListCell<String>> cellFactory = new Callback<ListView<String>, ListCell<String>>() {

            @Override public ListCell<String> call(ListView<String> p) {

                return new ListCell<String>() {

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);

                        setText(item);

                        if (item == null || empty) {
                            setGraphic(null);
                        }

                        else {
                            Image icon = null;
                            String iconPath;
                            try {
                                switch (item){

                                    case "Sverige":
                                        iconPath = "RecipeSearch/resources/icon_flag_sweden.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Grekland":
                                        iconPath = "RecipeSearch/resources/icon_flag_greece.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Indien":
                                        iconPath = "RecipeSearch/resources/icon_flag_india.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Asien":
                                        iconPath = "RecipeSearch/resources/icon_flag_asia.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Afrika":
                                        iconPath = "RecipeSearch/resources/icon_flag_africa.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Frankrike":
                                        iconPath = "RecipeSearch/resources/icon_flag_france.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                }
                            } catch(NullPointerException ex) {
                                //This should never happen in this lab but could load a default image in case of a NullPointer
                            }
                            ImageView iconImageView = new ImageView(icon);
                            iconImageView.setFitHeight(12);
                            iconImageView.setPreserveRatio(true);
                            setGraphic(iconImageView);
                        }
                    }
                };
            }
        };
        inputCuisine.setButtonCell(cellFactory.call(null));
        inputCuisine.setCellFactory(cellFactory);
    }


    public Image getCuisineImage(String cuisine) {
        String iconPath;
        switch (cuisine) {
            case "Sverige":
                iconPath = "RecipeSearch/resources/icon_flag_sweden.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Grekland":
                iconPath = "RecipeSearch/resources/icon_flag_greece.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Indien":
                iconPath = "RecipeSearch/resources/icon_flag_india.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Asien":
                iconPath = "RecipeSearch/resources/icon_flag_asia.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Afrika":
                iconPath = "RecipeSearch/resources/icon_flag_africa.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Frankrike":
                iconPath = "RecipeSearch/resources/icon_flag_france.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
        }
        iconPath = "";
        return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
    }


    public Image getMainIngredientImage(String mainIngredient) {
        String iconPath;
        switch (mainIngredient) {
            case "Fisk":
                iconPath = "RecipeSearch/resources/icon_main_fish.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Kött":
                iconPath = "RecipeSearch/resources/icon_main_meat.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Kyckling":
                iconPath = "RecipeSearch/resources/icon_main_chicken.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Vegetarisk":
                iconPath = "RecipeSearch/resources/icon_main_veg.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
        }
        iconPath = "";
        return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
    }

    public Image getDifficultyImage(String mainIngredient) {
        String iconPath;
        switch (mainIngredient) {
            case "Lätt":
                iconPath = "RecipeSearch/resources/icon_difficulty_easy.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Mellan":
                iconPath = "RecipeSearch/resources/icon_difficulty_medium.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Svår":
                iconPath = "RecipeSearch/resources/icon_difficulty_hard.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
        }
        iconPath = "";
        return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
    }

    private String formatIngredients(List<Ingredient> ingredients){
        StringBuilder result = new StringBuilder();
        for (Ingredient ingredient : ingredients) {
            result.append(ingredient);
            result.append("\n");
        }
        return result.toString();
    }

    @FXML
    public void closeButtonMouseEntered(){
        detailedViewCloseIcon.setImage(new Image(getClass().getClassLoader().getResourceAsStream(
                "RecipeSearch/resources/icon_close_hover.png")));
    }

    @FXML
    public void closeButtonMousePressed(){
        detailedViewCloseIcon.setImage(new Image(getClass().getClassLoader().getResourceAsStream(
                "RecipeSearch/resources/icon_close_pressed.png")));
    }

    @FXML
    public void closeButtonMouseExited(){
        detailedViewCloseIcon.setImage(new Image(getClass().getClassLoader().getResourceAsStream(
                "RecipeSearch/resources/icon_close.png")));
    }



    public Image getSquareImage(Image image){

        int x = 0;
        int y = 0;
        int width = 0;
        int height = 0;

        if(image.getWidth() > image.getHeight()){
            width = (int) image.getHeight();
            height = (int) image.getHeight();
            x = (int)(image.getWidth() - width)/2;
            y = 0;
        }

        else if(image.getHeight() > image.getWidth()){
            width = (int) image.getWidth();
            height = (int) image.getWidth();
            x = 0;
            y = (int) (image.getHeight() - height)/2;
        }

        else{
            //Width equals Height, return original image
            return image;
        }
        return new WritableImage(image.getPixelReader(), x, y, width, height);
    }
}

