package uk.singular.dfs.provider.sandbox.dictionary.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import uk.singular.dfs.provider.sandbox.dictionary.model.primarykeys.SportId;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@Entity
@Table(name = "sport")
@IdClass(SportId.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Sport implements Serializable {

    @Id
    private Integer sportId;
    @Id
    private Integer language;

    private String name;

    @Transient
    private Map<Integer,String> names;

    @JsonIgnore
    @OneToMany(mappedBy = "sport",fetch = FetchType.LAZY)
    @Cascade(CascadeType.DELETE)
    private List<Country> countries;

    @CreationTimestamp
    @Column(name= "create_date", nullable = false, updatable = false)
    private LocalDateTime createDate;

    @UpdateTimestamp
    @Column(name= "update_date")
    private LocalDateTime updateDate;

    public Sport(){
        countries = new ArrayList<>();
    }

    public Sport(Integer sportId,Integer language,String name){
        this.sportId = sportId;
        this.language = language;
        this.name = name;
    }

}
