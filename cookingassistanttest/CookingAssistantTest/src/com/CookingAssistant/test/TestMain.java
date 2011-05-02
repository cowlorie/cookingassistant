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
        solo.clickOnMenuItem("Synchronize");
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
        
    }

    public void testRecipeIsSentToShoppingList(){
        solo.clickOnMenuItem("Recipes");
        solo.clickOnText("Chocolate Wontons");
        solo.sendKey(Solo.MENU);
        solo.clickOnMenuItem("Add To Shopping List");
        solo.sendKey(Solo.CLOSED);
        solo.clickOnMenuItem("Home");
        solo.clickOnButton("Shop");
        Assert.assertTrue(solo.searchText("wonton"));
    }

    public void testRecipeIsFavorited(){
        solo.clickOnMenuItem("Recipes");
        solo.clickOnText("Chocolate Wontons");
        solo.sendKey(Solo.MENU);
        solo.clickOnButton("Add To Favorites");
        solo.sendKey(Solo.CLOSED);
        solo.clickOnMenuItem("Home");
        solo.clickOnButton("Favorites");
        Assert.assertTrue(solo.searchText("Chocolate Wontons"));
    }

    public void testRecipeSearched(){
        solo.clickOnMenuItem("Search");
        solo.enterText(0, "Chocolate Wontons");
        solo.clickOnButton("Search");
        solo.clickOnText("Chocolate Wontons");
        Assert.assertTrue(solo.searchText("Chocolate Wontons"));
    }
}