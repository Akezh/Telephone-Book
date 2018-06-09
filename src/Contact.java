/**
 * Created by Фора on 15.07.2016.
 */
public class Contact implements Comparable<Contact> {
    private String surname;
    private String name;
    private String number;
    private BirthDate birthdate;

    public Contact() {
        surname = null;
        name = null;
        number = null;
        birthdate = new BirthDate();
    }

    public Contact(String surname, String name, String number, BirthDate birthdate) {
        this.surname = surname;
        this.name = name;
        this.number = number;
        this.birthdate = birthdate;
    }

    public BirthDate getbirthdate() {
        return birthdate;
    }

    public void setBirthdate(BirthDate birthdate) {
        this.birthdate = birthdate;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return String.format("%s | %s | %s | %s", surname, name, number, birthdate.toString());
    }

    @Override
    public int compareTo(Contact o) {
        int i = this.surname.compareTo(o.surname);
        if (i == 0) {
            i = this.name.compareTo(o.name);
        }
        return i;
    }
}
