//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package dados;

import java.io.*;

import java.util.ArrayList;
import negocio.Caminhao;
import negocio.Cliente;

public class RepositorioCaminhao {
    private ArrayList<Caminhao> caminhoes = new ArrayList();

    public boolean cadastrar(Caminhao caminhao) {
        if(this.caminhoes.add(caminhao)){
            System.out.println("Caminhão cadastrado: " + caminhao.getPlaca());
            return true;
        }
        return false;
    }

    public Caminhao buscarPorPlaca(String placa) {
        for(Caminhao c : this.caminhoes) {
            if (c.getPlaca().equals(placa)) {
                return c;
            }
        }
        return null;
    }


    public ArrayList<Caminhao> listarTodos() {
        return new ArrayList(this.caminhoes);
    }

    public boolean atualizar(Caminhao caminhao) {
        for(int i = 0; i < this.caminhoes.size(); ++i) {
            if (((Caminhao)this.caminhoes.get(i)).getPlaca().equals(caminhao.getPlaca())) {
                this.caminhoes.set(i, caminhao);
                return true;
            }
        }

        return false;
    }

    public boolean remover(String placa) {
        Caminhao c = this.buscarPorPlaca(placa);
        if (c != null) {
            this.caminhoes.remove(c);
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
            oos.writeObject(this.caminhoes);
        }
    }

    @SuppressWarnings("unchecked")
    public void carregar(String caminhoArquivo) throws IOException, ClassNotFoundException {
        File f = new File(caminhoArquivo);
        if (!f.exists()) return;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            this.caminhoes = (ArrayList) ois.readObject();;
        }
    }

}
