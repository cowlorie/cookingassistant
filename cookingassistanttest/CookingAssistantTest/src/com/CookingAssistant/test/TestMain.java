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
    
    public void testRecipeIsSentToShoppingList(){
        solo.clickOnMenuItem("Recipe");
        solo.clickOnText("Chocolate Wontons");
        solo.sendKey(Solo.MENU);
        solo.clickOnButton("Add to Shopping List");
        solo.sendKey(Solo.CLOSED);
        solo.sendKey(Solo.CLOSED);
        solo.clickOnButton("ShoppingList");
        
        Assert.assertTrue(solo.searchText("wonton"));
        
        solo.clickOnText("txt");
        solo.clearEditText(2);
        solo.enterText(2, "robotium");
        solo.clickOnButton("Save");
        solo.goBack();
        solo.clickOnText("Edit File Extensions");
        Assert.assertTrue(solo.searchText("application/robotium"));
    }
    
    public void testRecipeIsFavorited(){
        solo.clickOnMenuItem("Recipe");
        solo.clickOnText("Chocolate Wontons");
        solo.sendKey(Solo.MENU);
        solo.clickOnButton("Add to Favorites");
        solo.sendKey(Solo.CLOSED);
        solo.sendKey(Solo.CLOSED);
        solo.clickOnButton("Favorites");
        
        Assert.assertTrue(solo.searchText("Chocolate Wontons"));
        
        solo.clickOnText("txt");
        solo.clearEditText(2);
        solo.enterText(2, "robotium");
        solo.clickOnButton("Save");
        solo.goBack();
        solo.clickOnText("Edit File Extensions");
        Assert.assertTrue(solo.searchText("application/robotium"));   
    }
    
    public void testRecipeSearched(){
        solo.clickOnButton("Search");
        solo.enterText(0, "Chocolate Wontons");
        solo.clickOnButton("Search");
        solo.clickOnText("Chocolate Wontons");
        Assert.assertTrue(solo.searchText("Chocolate Wontons"));
        
        solo.clickOnText("txt");
        solo.clearEditText(2);
        solo.enterText(2, "robotium");
        solo.clickOnButton("Save");
        solo.goBack();
        solo.clickOnText("Edit File Extensions");
        Assert.assertTrue(solo.searchText("application/robotium"));   
    }


}
