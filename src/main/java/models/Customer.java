package models;

public class Customer {

    private String firstName;
    private String lastName;
    private String postalCode;

    public Customer(String firstName, String lastName, String postalCode) {
        setFirstName(firstName);
        setLastName(lastName);
        setPostalCode(postalCode);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        if (isValidName(firstName)) {
            throw new IllegalArgumentException("First name must contain only alphabetic characters");
        }
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        if (isValidName(lastName)) {
            throw new IllegalArgumentException("Last name must contain only alphabetic characters");
        }
        this.lastName = lastName;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        if (!isValidPostalCode(postalCode)) {
            throw new IllegalArgumentException("Postal code must contain only numeric characters");
        }
        this.postalCode = postalCode;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    private boolean isValidName(String name) {
        return name == null || !name.matches("^[a-zA-Z]+$");
    }

    private boolean isValidPostalCode(String postalCode) {
        return postalCode != null && postalCode.matches("^[0-9]+$");
    }

    @Override
    public String toString() {
        return "Customer{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", postalCode='" + postalCode + '\'' +
                '}';
    }
}
