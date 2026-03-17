package br.uepb.techsupport.util;

import org.mindrot.jbcrypt.BCrypt;

public class SenhaUtil {

    public static String hashSenha(String senhaPura) {
        return BCrypt.hashpw(senhaPura, BCrypt.gensalt(12));
    }

    public static boolean verificarSenha(String senhaPura, String senhaHash) {
        if (senhaHash == null || !senhaHash.startsWith("$2a$")) {
            return false;
        }
        return BCrypt.checkpw(senhaPura, senhaHash);
    }
}