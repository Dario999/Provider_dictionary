package uk.singular.dfs.provider.sandbox.dictionary.model.primarykeys;

import java.io.Serializable;
import java.util.Objects;

public class LeagueId implements Serializable {

    private Integer leagueId;
    private Integer language;

    public LeagueId(){
    }

    public LeagueId(Integer leagueId, Integer language){
        this.leagueId = leagueId;
        this.language = language;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LeagueId leagueId1 = (LeagueId) o;
        return leagueId.equals(leagueId1.leagueId) && language == leagueId1.language;
    }

    @Override
    public int hashCode() {
        return Objects.hash(leagueId, language);
    }

    public Integer getLeagueId() {
        return leagueId;
    }

    public Integer getLanguage() {
        return language;
    }
}
