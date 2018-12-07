package com.andreea.bakingapp.baking_app.model;

import android.text.Html;
import android.text.Spanned;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Recipe {

    @SerializedName("id")
    private Integer id;

    @SerializedName("name")
    private String name;

    @SerializedName("ingredients")
    private List<Ingredient> ingredients;

    @SerializedName("steps")
    private List<Step> steps;

    @SerializedName("servings")
    private Integer servings;

    @SerializedName("image")
    private String image;

    public Recipe(Integer id, String name, List<Ingredient> ingredients, List<Step> steps, Integer servings, String image) {
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.steps = steps;
        this.servings = servings;
        this.image = image;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public Spanned getFormattedIngredients() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Ingredient ingredient : ingredients) {
            String formattedIngredient = String.format("- <b>%s %s</b> - %s",
                    ingredient.getQuantity(),
                    ingredient.getMeasure(),
                    ingredient.getIngredient());
            stringBuilder.append(formattedIngredient).append("<br>");
        }
        return Html.fromHtml(stringBuilder.toString());
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public int getStepIndex(Step step) {
        return getSteps().indexOf(step);
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public Integer getServings() {
        return servings;
    }

    public void setServings(Integer servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

