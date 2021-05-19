package info.navnoire.recipeappserver_springboot.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

/**
 * Created by Victoria Berezina on 11/05/2021 in RecipeAppServer_SpringBoot project
 */
@Entity(name = "ingredient")
@Table(name = "ingredient")
public class Ingredient {
    private Long id;
    private String name;
    private String amount;
    private Recipe recipe;

    public Ingredient() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ingredient_id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "amount")
    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "recipe", nullable = false)
    @JsonIgnore
    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (!(o instanceof Ingredient))
            return false;

        return
                id != null &&
                        id.equals(((Ingredient) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "\n* Ingredient{" +
                "name='" + name + '\'' +
                ", amount='" + amount + "'}";
    }
}