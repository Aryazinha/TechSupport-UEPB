package br.uepb.techsupport.util;

import java.util.regex.Pattern;

public class ValidadorDados {

    private static final String CPF_REGEX = "\\d{3}\\.?\\d{3}\\.?\\d{3}-?\\d{2}";
    private static final String EMAIL_REGEX = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
    private static final String TELEFONE_REGEX = "^(?:\\(?\\d{2}\\)?\\s?)?\\d{4,5}-?\\d{4}$";

    public static boolean isCpfValido(String cpf) {
        return Pattern.matches(CPF_REGEX, cpf);
    }

    public static boolean isEmailValido(String email) {
        return Pattern.matches(EMAIL_REGEX, email);
    }

    public static boolean isTelefoneValido(String telefone) { return Pattern.matches(TELEFONE_REGEX, telefone); }

    public static boolean isDescricaoValida(String descricao) { return descricao != null && descricao.trim().length() >= 10; }
}
