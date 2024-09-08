package br.com.alura.adopet.api.exception;

public class ValidacaoException extends RuntimeException{
    // Extende de RuntimeException para exceções não checadas

    public ValidacaoException(String message){
        super(message);
    }
}
