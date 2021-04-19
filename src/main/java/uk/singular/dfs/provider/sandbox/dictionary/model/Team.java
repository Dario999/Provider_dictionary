package uk.singular.dfs.provider.sandbox.dictionary.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import uk.singular.dfs.provider.sandbox.dictionary.model.primarykeys.TeamId;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@Entity
@Table(name = "team")
@IdClass(TeamId.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Team implements Serializable {

    @Id
    private Integer teamId;
    @Id
    private Integer language;

    private String name;

    @Transient
    private Map<Integer,String> names;

    @JsonIgnore
    @MapsId("leagueId")
    @JoinColumns({
            @JoinColumn(name = "fk_league_id",referencedColumnName = "LeagueId"),
            @JoinColumn(name = "fk_language",referencedColumnName = "Language")
    })
    @ManyToOne
    private League league;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "fk_language")
    private Integer fkLanguage;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "fk_league_id")
    private Integer leagueId;

    @CreationTimestamp
    @Column(name= "create_date", nullable = false, updatable = false)
    private LocalDateTime createDate;

    @UpdateTimestamp
    @Column(name= "update_date")
    private LocalDateTime updateDate;

    public Team(Integer teamId, Integer language, String name, Integer fkLanguage, Integer leagueId) {
        this.teamId = teamId;
        this.language = language;
        this.name = name;
        this.fkLanguage = fkLanguage;
        this.leagueId = leagueId;
    }

    public Team(Integer teamId, Integer language, String name, Integer fkLanguage) {
        this.teamId = teamId;
        this.language = language;
        this.name = name;
        this.fkLanguage = fkLanguage;
    }

    public Team() {
    }

}
