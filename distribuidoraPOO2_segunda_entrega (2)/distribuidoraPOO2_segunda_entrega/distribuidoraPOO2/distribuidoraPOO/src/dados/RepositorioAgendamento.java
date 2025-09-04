
package dados;

import java.io.*;

import java.util.ArrayList;
import negocio.Agendamento;

public class RepositorioAgendamento {
    private ArrayList<Agendamento> agendamentos = new ArrayList();

    public void cadastrar(Agendamento agendamento) {
        this.agendamentos.add(agendamento);
        System.out.println("Agendamento cadastrado para pedido: " + agendamento.getPedido().getNumero());
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
                return true;
            }
        }

        return false;
    }

    public boolean remover(int numeroPedido) {
        Agendamento a = this.buscarPorPedido(numeroPedido);
        if (a != null) {
            this.agendamentos.remove(a);
            return true;
        } else {
            return false;
        }
    }

    // Persistência simples usando serialização Java
    public void salvar(String caminhoArquivo) throws IOException {
        File f = new File(caminhoArquivo);
        f.getParentFile().mkdirs();
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f))) {
            oos.writeObject(this.agendamentos);
        }
    }

    @SuppressWarnings("unchecked")
    public void carregar(String caminhoArquivo) throws IOException, ClassNotFoundException {
        File f = new File(caminhoArquivo);
        if (!f.exists()) return;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            this.agendamentos = (ArrayList) ois.readObject();;
        }
    }

}
