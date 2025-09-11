package negocio.exceptions;

public class CpfJaExistenteException extends RuntimeException {
    public CpfJaExistenteException(String message) {
        super(message);
    }
}
