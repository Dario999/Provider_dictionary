package uk.singular.dfs.provider.sandbox.dictionary.model.primarykeys;

import java.io.Serializable;
import java.util.Objects;

public class SportId implements Serializable {

    private Integer sportId;
    private Integer language;

    public SportId(){
    }

    public SportId(Integer sportId,Integer language){
        this.sportId = sportId;
        this.language = language;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SportId sportId1 = (SportId) o;
        return sportId.equals(sportId1.sportId) && language == sportId1.language;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sportId, language);
    }

    public Integer getSportId() {
        return sportId;
    }

    public Integer getLanguage() {
        return language;
    }

}
