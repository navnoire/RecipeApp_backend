package info.navnoire.recipeappserver_springboot.domain.recipe;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

/**
 * Created by Victoria Berezina on 11/05/2021 in RecipeAppServer_SpringBoot project
 */
@Entity(name = "ingredient")
@Table(name = "ingredient", schema = "recipeapp_db")
public class Ingredient {
    private Long id;
    private String name;
    private String amount;
    private Integer type;
    private Recipe recipe;

    public Ingredient() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ingredient_id")
    @JsonIgnore
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

    @Column(name = "type")
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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
                "name=" + name + ",\n" +
                "  type=" + type + ",\n" +
                "  amount=" + amount + "'}";
    }
}
