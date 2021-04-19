package uk.singular.dfs.provider.sandbox.dictionary.model.primarykeys;

import java.io.Serializable;
import java.util.Objects;

public class TeamId implements Serializable {

    private Integer teamId;
    private Integer language;

    public TeamId(){
    }

    public TeamId(Integer teamId, Integer language){
        this.teamId = teamId;
        this.language = language;
    }

    public Integer getTeamId() {
        return teamId;
    }

    public Integer getLanguage() {
        return language;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TeamId teamId1 = (TeamId) o;
        return teamId.equals(teamId1.teamId) && language == teamId1.language;
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamId, language);
    }
}
