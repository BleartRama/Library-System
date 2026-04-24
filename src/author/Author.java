package author;

import java.time.LocalDate;

public class Author {
    private int id;
    private String firstName;
    private String lastName;
    private String nationality;
    private LocalDate birthDate;

    public Author(int id, String firstName, String lastName, String nationality, LocalDate birthDate) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.nationality = nationality;
        this.birthDate = birthDate;
    }

    public Author(String firstName, String lastName, String nationality, LocalDate birthDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.nationality = nationality;
        this.birthDate = birthDate;
    }

    public int getId()            { return id; }
    public String getFirstName()  { return firstName; }
    public String getLastName()   { return lastName; }
    public String getNationality(){ return nationality; }
    public LocalDate getBirthDate(){ return birthDate; }
    public String getFullName()   { return firstName + " " + lastName; }

    @Override
    public String toString() {
        return "[" + id + "] " + firstName + " " + lastName + " (" + nationality + ")";
    }
}