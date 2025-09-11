package dados;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import negocio.Agendamento;
import negocio.Caminhao;
import negocio.Pedido;
import negocio.StatusAgendamento;

public class RepositorioAgendamento {
    private ArrayList<Agendamento> agendamentos = new ArrayList();
    private static final String ARQUIVO_CSV = "agendamentos.csv";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    public RepositorioAgendamento() {
        carregar();
    }

    public void cadastrar(Agendamento agendamento) {
        this.agendamentos.add(agendamento);
        System.out.println("Agendamento cadastrado para pedido: " + agendamento.getPedido().getNumero());
        salvar();
    }

    public Agendamento buscarPorPedido(int numeroPedido) {
        for(Agendamento a : this.agendamentos) {
            if (a.getPedido().getNumero() == numeroPedido) {
                return a;
            }
        }

        return null;
    }

    public ArrayList<Agendamento> listarTodos() {
        return new ArrayList(this.agendamentos);
    }

    public boolean atualizar(Agendamento agendamento) {
        for(int i = 0; i < this.agendamentos.size(); ++i) {
            if (((Agendamento)this.agendamentos.get(i)).getPedido().getNumero() == agendamento.getPedido().getNumero()) {
                this.agendamentos.set(i, agendamento);
                salvar();
                return true;
            }
        }

        return false;
    }

    public boolean remover(int numeroPedido) {
        Agendamento a = this.buscarPorPedido(numeroPedido);
        if (a != null) {
            this.agendamentos.remove(a);
            salvar();
            return true;
        } else {
            return false;
        }
    }


    private void salvar() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARQUIVO_CSV))) {
            writer.write("numeroPedido,placaCaminhao,dataHoraPrevista,status\n");
            for (Agendamento agendamento : agendamentos) {
                String linha = String.format("%d,\"%s\",\"%s\",%s",
                        agendamento.getPedido().getNumero(),
                        agendamento.getCaminhao().getPlaca(),
                        DATE_FORMAT.format(agendamento.getDataHoraPrevista()),
                        agendamento.getStatus().name()
                );
                writer.write(linha);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar agendamentos no arquivo CSV: " + e.getMessage());
        }
    }

    private void carregar() {
        File arquivo = new File(ARQUIVO_CSV);
        if (!arquivo.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(ARQUIVO_CSV))) {
            this.agendamentos.clear();
            reader.readLine(); // Pula cabeçalho

            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.replace("\"", "").split(",");
                if (dados.length == 4) {
                    int numeroPedido = Integer.parseInt(dados[0]);
                    String placaCaminhao = dados[1];
                    Date dataHoraPrevista = DATE_FORMAT.parse(dados[2]);
                    StatusAgendamento status = StatusAgendamento.valueOf(dados[3]);

                    Pedido pedido = new Pedido();
                    pedido.setNumero(numeroPedido);
                    Caminhao caminhao = new Caminhao(placaCaminhao);

                    Agendamento agendamento = new Agendamento(pedido, caminhao, dataHoraPrevista);
                    agendamento.setStatus(status);
                    this.agendamentos.add(agendamento);
                }
            }
        } catch (IOException | NumberFormatException | ParseException e) {
            System.err.println("Erro ao carregar agendamentos do arquivo CSV: " + e.getMessage());
        }
    }
}
