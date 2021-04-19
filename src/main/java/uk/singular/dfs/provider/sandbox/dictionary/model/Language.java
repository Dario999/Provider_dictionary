package uk.singular.dfs.provider.sandbox.dictionary.model;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name = "languages")
public class Language {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "language_name")
    private String languageName;

}
