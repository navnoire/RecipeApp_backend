package info.navnoire.recipeappserver_springboot.domain.recipe;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Victoria Berezina on 30/04/2021 in recipeApp_spring project
 */

@Entity(name = "recipe")
@Table(name = "recipe", schema = "recipeapp_db")
public class Recipe {
    private Integer id;
    private String title;
    private String main_image_url;
    private String body_url;

    private List<Ingredient> ingredients = new ArrayList<>();
    private List<Step> steps = new ArrayList<>();
    private Set<Category> categories = new HashSet<>();

    public Recipe() {
    }

    public Recipe(Integer id, String title, String main_image_url) {
        this.id = id;
        this.title = title;
        this.main_image_url = main_image_url;
    }

    @Id
    @Column(name = "recipe_id")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(name = "main_image_url")
    @JsonIgnore
    public String getMain_image_url() {
        return main_image_url;
    }

    public void setMain_image_url(String main_image_url) {
        this.main_image_url = main_image_url;
    }

    @Column(name = "body_url")
    @JsonIgnore
    public String getBody_url() {
        return body_url;
    }
    public void setBody_url(String body_url) {
        this.body_url = body_url;
    }

    @OneToMany(mappedBy = "recipe", fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @Fetch(value = FetchMode.SUBSELECT)
    @JsonInclude(value = JsonInclude.Include.NON_EMPTY, content = JsonInclude.Include.NON_NULL)
    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients =  ingredients;
    }

    @OneToMany(mappedBy = "recipe", fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @Fetch(value = FetchMode.SUBSELECT)
    @JsonInclude(value = JsonInclude.Include.NON_EMPTY, content = JsonInclude.Include.NON_NULL)
    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    @ManyToMany
    @JoinTable(name = "category_recipe",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    @JsonIgnore
    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    @Override
    public String toString() {
        return "Recipe " + id +
                " \n * title = " + title +
                ", \n * mainImage = " + main_image_url + "\n";
    }
}

