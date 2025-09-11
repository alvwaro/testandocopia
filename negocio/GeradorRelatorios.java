package negocio;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GeradorRelatorios {

    public static List<Pedido> filtrarPedidosPorPeriodo(List<Pedido> pedidos, LocalDateTime inicio, LocalDateTime fim) {
        return pedidos.stream()
                .filter(p -> p.getDataPagamento() != null && !p.getDataPagamento().isBefore(inicio) && !p.getDataPagamento().isAfter(fim))
                .collect(Collectors.toList());
    }

    public static Map<String, Integer> calcularProdutosMaisVendidos(List<Pedido> pedidos) {
        Map<String, Integer> contagemProdutos = new HashMap<>();
        for (Pedido p : pedidos) {
            for (Produto prod : p.getProdutos()) {
                contagemProdutos.put(prod.getNome(), contagemProdutos.getOrDefault(prod.getNome(), 0) + prod.getQuantidade());
            }
        }
        return contagemProdutos;
    }

    public static Map<String, Double> calcularClientesQueMaisCompram(List<Cliente> clientes) {
        Map<String, Double> totalPorCliente = new HashMap<>();
        for (Cliente c : clientes) {
            double total = c.getPedidos().stream()
                    .filter(p -> "PAGO".equalsIgnoreCase(p.getStatus()))
                    .mapToDouble(Pedido::getValorTotal)
                    .sum();
            if (total > 0) {
                totalPorCliente.put(c.getNome(), total);
            }
        }
        return totalPorCliente;
    }

    public static List<Produto> gerarRelatorioEstoqueBaixo(Estoque estoque, int limite) {
        return estoque.getProdutos().stream()
                .filter(p -> p.getQuantidade() <= limite)
                .sorted(Comparator.comparingInt(Produto::getQuantidade))
                .collect(Collectors.toList());
    }
}
