package br.ufrn.imd.collectiva_backend.utils.validators;

public class TelephoneValidator {
    public static boolean validateTelephone(String telephone) {
        telephone = telephone.replaceAll("[^0-9]", "");

        int minLength = 10;
        int maxLength = 15;

        int phoneLength = telephone.length();
        return phoneLength >= minLength && phoneLength <= maxLength;
    }
}
