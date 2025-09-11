package dados.mappers;

import negocio.Cliente;
import negocio.Pedido;
import negocio.Produto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ClienteCsvMapper {

    public static String toCsvLine(Cliente cliente) {
        // Converte a lista de pedidos em uma única string
        StringBuilder pedidosStr = new StringBuilder();
        if (cliente.getPedidos() != null && !cliente.getPedidos().isEmpty()) {
            for (Pedido p : cliente.getPedidos()) {
                StringBuilder produtosStr = new StringBuilder();
                for (Produto prod : p.getProdutos()) {
                    // Formato por produto: codigo:nome:desc:preco:qtd
                    produtosStr.append(String.format(Locale.US, "%s:%s:%s:%.2f:%d;",
                            prod.getCodigo(), prod.getNome(), prod.getDescricao(), prod.getPreco(), prod.getQuantidade()));
                }
                // Formato por pedido: numero|status|valor|dataPagamento!produtos
                pedidosStr.append(String.format(Locale.US, "%d|%s|%.2f|%s!%s#",
                        p.getNumero(), p.getStatus(), p.getValorTotal(), p.getDataPagamento(), produtosStr.toString()));
            }
        }

        return String.format(Locale.US, "\"%s\",\"%s\",%d,\"%s\",\"%s\",\"%s\",\"%s\",%b,\"%s\"",
                cliente.getCpf(),
                cliente.getNome(),
                cliente.getIdade(),
                cliente.getTelefone(),
                cliente.getEndereco(),
                cliente.getEmail(),
                cliente.getTipo(),
                cliente.isCadastrado(),
                pedidosStr.toString());
    }

    public static Cliente fromCsvLine(String linha) {
        String[] dados = linha.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
        for (int i = 0; i < dados.length; i++) {
            if (dados[i].startsWith("\"") && dados[i].endsWith("\"")) {
                dados[i] = dados[i].substring(1, dados[i].length() - 1);
            }
        }

        if (dados.length >= 8) { // Alterado para >= 8 para compatibilidade
            Cliente cliente = new Cliente(dados[1], Integer.parseInt(dados[2]), dados[0], dados[3], dados[4], dados[5], dados[6]);
            cliente.setCadastrado(Boolean.parseBoolean(dados[7]));

            // Reconstrói a lista de pedidos a partir da string
            if (dados.length == 9 && !dados[8].isEmpty()) {
                List<Pedido> pedidos = new ArrayList<>();
                String[] pedidosArray = dados[8].split("#");
                for (String pedStr : pedidosArray) {
                    String[] pedParts = pedStr.split("!");
                    String[] pedInfo = pedParts[0].split("\\|");

                    ArrayList<Produto> produtosDoPedido = new ArrayList<>();
                    if (pedParts.length > 1 && !pedParts[1].isEmpty()) {
                        String[] produtosArray = pedParts[1].split(";");
                        for (String prodStr : produtosArray) {
                            String[] prodInfo = prodStr.split(":");
                            produtosDoPedido.add(new Produto(prodInfo[0], prodInfo[1], prodInfo[2], Double.parseDouble(prodInfo[3]), Integer.parseInt(prodInfo[4])));
                        }
                    }

                    Pedido p = new Pedido(produtosDoPedido);
                    p.setNumero(Integer.parseInt(pedInfo[0]));
                    p.setStatus(pedInfo[1]);
                    p.setValorTotal(Double.parseDouble(pedInfo[2]));
                    if (!"null".equals(pedInfo[3])) {
                        p.setDataPagamento(LocalDateTime.parse(pedInfo[3]));
                    }
                    pedidos.add(p);
                }
                // Adiciona a lista de pedidos reconstruída ao cliente
                for (Pedido p : pedidos) {
                    cliente.getPedidos().add(p);
                }
            }

            return cliente;
        }
        throw new IllegalArgumentException("Linha CSV inválida para Cliente: " + linha);
    }
}