package br.ufrn.imd.collectiva_backend.utils.validators;

public class EnumValidator {

    public static <T extends Enum<T>> boolean isValidFieldEnum(Class<T> enumClass, String value) {
        try {
            Enum.valueOf(enumClass, value);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
