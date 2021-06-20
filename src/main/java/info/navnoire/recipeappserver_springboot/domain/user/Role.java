package info.navnoire.recipeappserver_springboot.domain.user;

import javax.persistence.*;

/**
 * Created by Victoria Berezina on 17/06/2021 in RecipeApp project
 */

@Entity
@Table(schema = "recipeapp_users", name = "roles")
public class Role {
    public enum ERole {
        ADMIN, USER
    }

    @Id
    @Column(name = "role_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_name",length = 20)
    private ERole name;

    public Role() {
    }

    public Role(ERole name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ERole getName() {
        return name;
    }

    public void setName(ERole name) {
        this.name = name;
    }
}
