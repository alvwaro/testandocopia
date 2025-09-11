package negocio.exceptions;

public class PedidoNaoPagoException extends RuntimeException {
    public PedidoNaoPagoException(String message) {
        super(message);
    }
}
