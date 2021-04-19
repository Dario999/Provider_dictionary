package uk.singular.dfs.provider.sandbox.dictionary.model;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.CascadeType;
import uk.singular.dfs.provider.sandbox.dictionary.model.primarykeys.CountryId;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@Entity
@Table(name = "country")
@IdClass(CountryId.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Country implements Serializable {

    @Id
    private Integer countryId;
    @Id
    private Integer language;

    private String name;

    @Transient
    private Map<Integer,String> names;

    @JsonIgnore
    @OneToMany(mappedBy = "country",fetch = FetchType.LAZY)
    @Cascade(CascadeType.DELETE)
    private List<League> leagues;

    @JsonIgnore
    @MapsId("sportId")
    @JoinColumns({
            @JoinColumn(name = "fk_sport_id",referencedColumnName = "SportId"),
            @JoinColumn(name = "fk_language",referencedColumnName = "Language")
    })
    @ManyToOne
    private Sport sport;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "fk_language")
    private Integer fkLanguage;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "fk_sport_id")
    private Integer sportId;

    @CreationTimestamp
    @Column(name= "create_date", nullable = false, updatable = false)
    private LocalDateTime createDate;

    @UpdateTimestamp
    @Column(name= "update_date")
    private LocalDateTime updateDate;

    public Country(){
        leagues = new ArrayList<>();
    }

    public Country(Integer countryId,Integer language,String name,Integer fkLanguage,Integer sportId){
        this.countryId = countryId;
        this.language = language;
        this.name = name;
        this.fkLanguage = fkLanguage;
        this.sportId = sportId;
    }

}
