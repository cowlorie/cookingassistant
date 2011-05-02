package com.CookingAssistant.test;

import junit.framework.Assert;
import android.test.ActivityInstrumentationTestCase2;
import com.CookingAssistant.Mainpage;
import com.jayway.android.robotium.solo.Solo;

public class TestMain extends ActivityInstrumentationTestCase2<Mainpage> {
    private Solo solo;

    public TestMain() {
        super("com.CookingAssistant", Mainpage.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
        solo = new Solo(getInstrumentation(), getActivity());
    }

    protected void tearDown() throws Exception {
        try {
            solo.finalize();
        } catch (Throwable e) {
            e.printStackTrace();
        }

        getActivity().finish();
        super.tearDown();
    }
    
    public void testRecipeSynchronized() {
	// On Home screen
        solo.clickOnMenuItem("Synchronize");

	// Navigate to Recipe page to search for newly added recipe
        solo.clickOnMenuItem("Recipes");
        Assert.assertTrue(solo.searchText("wonton"));
    }
    
    public void testRecipeAccountInfo() {
        // Since Account info does not do anything at the moment
        Assert.assertTrue(solo.searchText("Account Info"));
    }
    
    public void testRecipeSettings() {
        // Since Settings does not do anything at the moment
        Assert.assertTrue(solo.searchText("Settings"));
    }
    
    public void testRecipeSearchOnline() {
	// On Home screen
        solo.clickOnMenuItem("Search Online");

	// Enters text on the first index of a NoteField
	solo.enterText(0, "chicken");
	solo.clickOnButton("Search");

	// Finds an example recipe, in this case Chicken Katsu
	solo.clickOnText("Chicken Katsu");
	Assert.assertTrue(solo.searchText("Japanese"));
    }

    public void testRecipeIsSentToShoppingList(){
	// On Home screen
        solo.clickOnMenuItem("Recipes");
        solo.clickOnText("Chocolate Wontons");

	// Brings up button to add to the shopping list
        solo.sendKey(Solo.MENU);
        solo.clickOnMenuItem("Add To Shopping List");
        solo.sendKey(Solo.CLOSED);

	// Returns to the home page and click on the button tab to check the
	// shopping list
        solo.clickOnMenuItem("Home");
        solo.clickOnButton("Shop");
        Assert.assertTrue(solo.searchText("wonton"));
    }

    public void testRecipeIsSentToShoppingListAndCheckedRemoved() {
	// On Home screen
        solo.clickOnMenuItem("Recipes");
        solo.clickOnText("Chocolate Wontons");
        solo.sendKey(Solo.MENU);
        solo.clickOnMenuItem("Add To Shopping List");
        solo.sendKey(Solo.CLOSED);

	// Returns to the home page and clicks on the shopping list tab
        solo.clickOnMenuItem("Home");
        solo.clickOnButton("Shop");

	// Clicks on the first clickable box
	solo.clickOnCheckBox(0);

	// Brings up button to clear the checked recipes
	solo.sendKey(Solo.MENU);
	solo.clickOnButton("Clear Checked")
	Assert.assertFalse(solo.searchText("Chocolate Wontons"));
    }

    public void testRecipeIsSentToShoppingListAndAllRemoved() {
	// On Home screen
        solo.clickOnMenuItem("Recipes");
        solo.clickOnText("Chocolate Wontons");
        solo.sendKey(Solo.MENU);
        solo.clickOnMenuItem("Add To Shopping List");
        solo.sendKey(Solo.CLOSED);

	// Returns to the home page and clicks on the shopping list tab
        solo.clickOnMenuItem("Home");
        solo.clickOnButton("Shop");
	solo.clickOnCheckBox(0);
	solo.sendKey(Solo.MENU);

	// Removes the entire shopping list instead of checked recipes
	solo.clickOnButton("Clear Shopping List")
	Assert.assertFalse(solo.searchText("Chocolate Wontons"));
    }

    public void testRecipeIsFavorited(){
	// On Home screen
        solo.clickOnMenuItem("Recipes");
        solo.clickOnText("Chocolate Wontons");
        solo.sendKey(Solo.MENU);

	// Brings up menu to add to favorites list
        solo.clickOnButton("Add To Favorites");
        solo.sendKey(Solo.CLOSED);
        solo.clickOnMenuItem("Home");

	// Checks Favorites tab to find the favorited recipe
        solo.clickOnButton("Favorites");
        Assert.assertTrue(solo.searchText("Chocolate Wontons"));
    }

    public void testRecipeIsFavoritedAndSelectedRemoved() {
	// On Home screen
	solo.clickOnMenuItem("Recipes");
        solo.clickOnText("Chocolate Wontons");
        solo.sendKey(Solo.MENU);
        solo.clickOnButton("Add To Favorites");
        solo.sendKey(Solo.CLOSED);

	// On Home screen, and navigates to Favorites tab
        solo.clickOnMenuItem("Home");
        solo.clickOnButton("Favorites");
	solo.clickOnText("Chocolate Wontons");

	// Removes specific favorite-d recipe
	solo.clickOnButton("Remove Favorite");
	Assert.assertFalse(solo.searchText("Chocolate Wontons"));
    }

    public void testRecipeIsFavoritedAndAllRemoved() {
	// On Home screen
	solo.clickOnMenuItem("Recipes");
        solo.clickOnText("Chocolate Wontons");
        solo.sendKey(Solo.MENU);
        solo.clickOnButton("Add To Favorites");
        solo.sendKey(Solo.CLOSED);

	// On Home screen, and navigates to Favorites tab
        solo.clickOnMenuItem("Home");
        solo.clickOnButton("Favorites");
	solo.sendKey(Solo.MENU);

	// Removes all favorite-d recipes
	solo.clickOnButton("Clear Favorites");
	Assert.assertFalse(solo.searchText("Chocolate Wontons"));
    }

    public void testRecipeSearched(){
	// On Home screen
        solo.clickOnMenuItem("Search");
        solo.enterText(0, "Chocolate Wontons");

	// Searches particular recipe, in this case for chocolate wontons
        solo.clickOnButton("Search");
        solo.clickOnText("Chocolate Wontons");
        Assert.assertTrue(solo.searchText("Chocolate Wontons"));
    }
}