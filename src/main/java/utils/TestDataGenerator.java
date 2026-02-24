package utils;


import java.util.Random;

public class TestDataGenerator {

    private static final Random random = new Random();
    private static final String[] FIRST_NAMES = {"John", "Jane", "Michael", "Sarah", "David", "Emma"};
    private static final String[] LAST_NAMES = {"Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia"};

    public static String[] generateValidCustomerData() {
        String firstName = FIRST_NAMES[random.nextInt(FIRST_NAMES.length)];
        String lastName = LAST_NAMES[random.nextInt(LAST_NAMES.length)];
        String postalCode = String.format("%05d", random.nextInt(100000));

        return new String[]{firstName, lastName, postalCode};
    }

    public static String generateFullName(String firstName, String lastName) {
        return firstName + " " + lastName;
    }


    public static String generateInvalidNameWithNumbers() {
        return "John123";
    }

    public static String generateInvalidNameWithSpecialChars() {
        return "John@Doe";
    }

    public static String generateInvalidPostalCodeWithLetters() {
        return "12ABC";
    }

    public static int generateDepositAmount() {
        return random.nextInt(1000) + 100;
    }

    public static int generateWithdrawalAmount(int maxAmount) {
        return random.nextInt(maxAmount) + 1;
    }
}
