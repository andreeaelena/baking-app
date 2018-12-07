package com.andreea.bakingapp.baking_app.network;

import com.andreea.bakingapp.baking_app.model.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RecipesApi {
    @GET("/topher/2017/May/59121517_baking/baking.json")
    Call<List<Recipe>> getRecipes();
}
