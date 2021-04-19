package uk.singular.dfs.provider.sandbox.dictionary.model.primarykeys;

import java.io.Serializable;
import java.util.Objects;

public class CountryId implements Serializable {

    private Integer countryId;
    private Integer language;

    public CountryId(){
    }

    public CountryId(Integer countryId, Integer language){
        this.countryId = countryId;
        this.language = language;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CountryId countryId1 = (CountryId) o;
        return countryId.equals(countryId1.countryId) && language == countryId1.language;
    }

    @Override
    public int hashCode() {
        return Objects.hash(countryId, language);
    }

    public Integer getLanguage() {
        return language;
    }

    public Integer getCountryId() {
        return countryId;
    }
}
