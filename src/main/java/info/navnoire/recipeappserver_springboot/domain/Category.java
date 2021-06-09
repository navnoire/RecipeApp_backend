package info.navnoire.recipeappserver_springboot.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Victoria Berezina on 01/05/2021 in recipeApp_spring project
 */
@Entity
@Table(name ="category")
public class Category {
    private Integer id;
    private String title;
    private Integer parent_id;
    private String category_url;
    private boolean hasChild;

    private Set<Recipe> recipes = new HashSet<>();

    public Category() {
    }

    @Id
    @Column(name = "category_id")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "name")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(name = "parent_id")
    @JsonIgnore
    public Integer getParent_id() {
        return parent_id;
    }

    public void setParent_id(Integer parent_id) {
        this.parent_id = parent_id;
    }

    @Column(name = "category_url")
    @JsonIgnore
    public String getCategory_url() {
        return category_url;
    }

    public void setCategory_url(String category_url) {
        this.category_url = category_url;
    }

    @ManyToMany(mappedBy = "categories", fetch = FetchType.LAZY)
    @JsonInclude(value = JsonInclude.Include.NON_EMPTY, content = JsonInclude.Include.NON_NULL)
    public Set<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(Set<Recipe> recipes) {
        this.recipes = recipes;
    }

    @Column(name = "has_child", nullable = false, columnDefinition = "TINYINT(1)")
    public boolean isHasChild() {
        return hasChild;
    }

    public void setHasChild(boolean hasChild) {
        this.hasChild = hasChild;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", parent='" + parent_id + '\'' +
                '}';
    }
}
