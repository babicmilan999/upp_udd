package upp_udd.project.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Magazine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String ISSN;

    @Column(nullable = false)
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private Status status = Status.INACTIVE;

    @ManyToMany
    @JoinTable(name = "magazine_scientific_field",
               joinColumns = @JoinColumn(name = "magazine_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "scientific_field_id", referencedColumnName = "id"))
    private Set<ScientificField> scientificFields = new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "main_editor_id")
    private User mainEditor;

    @ManyToMany
    @JoinTable(name = "magazine_employees",
               joinColumns = @JoinColumn(name = "magazine_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "employee_id", referencedColumnName = "id"))
    private Set<User> employees = new HashSet<>();

    public enum Status {
        ACTIVE,
        INACTIVE
    }

}
