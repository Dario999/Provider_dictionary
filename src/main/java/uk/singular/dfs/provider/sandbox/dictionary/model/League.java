package uk.singular.dfs.provider.sandbox.dictionary.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import uk.singular.dfs.provider.sandbox.dictionary.model.primarykeys.LeagueId;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@Entity
@Table(name = "league")
@IdClass(LeagueId.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class League implements Serializable {

    @Id
    private Integer leagueId;
    @Id
    private Integer language;

    private String name;

    @Transient
    private Map<Integer,String> names;

    @JsonIgnore
    @OneToMany(mappedBy = "league",fetch = FetchType.LAZY)
    @Cascade(CascadeType.DELETE)
    private List<Team> teams;

    @JsonIgnore
    @MapsId("countryId")
    @JoinColumns({
            @JoinColumn(name = "fk_country_id",referencedColumnName = "CountryId"),
            @JoinColumn(name = "fk_language",referencedColumnName = "Language")
    })
    @ManyToOne
    private Country country;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "fk_language")
    private Integer fkLanguage;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "fk_country_id")
    private Integer countryId;

    @CreationTimestamp
    @Column(name= "create_date", nullable = false, updatable = false)
    private LocalDateTime createDate;

    @UpdateTimestamp
    @Column(name= "update_date")
    private LocalDateTime updateDate;

    public League(){
        teams = new ArrayList<>();
    }

    public League(Integer leagueId, Integer language, String name, Integer fkLanguage, Integer countryId) {
        this.leagueId = leagueId;
        this.language = language;
        this.name = name;
        this.fkLanguage = fkLanguage;
        this.countryId = countryId;
    }

}
