package dados;

import java.io.*;
import negocio.*;

import java.util.ArrayList;
import java.util.List;

public class RepositorioPatio {

    private ArrayList<Caminhao> caminhoesPatio = new ArrayList();

    public boolean cadastrarCaminhaoPatio(Caminhao caminhao){
        if(this.caminhoesPatio.add(caminhao)){
            return true;
        }
        return false;
    }
    public Caminhao buscarPorPlaca(String placa) {
        for(Caminhao c : this.caminhoesPatio) {
            if (c.getPlaca().equals(placa)) {
                return c;
            }
        }
        return null;
    }
    public void listarTodos() {
        if (this.caminhoesPatio.isEmpty()) {
            System.out.println("Não há caminhoes cadastrados no patio.");
            return;
        }
        System.out.println("Caminhoes: ");
        for (Caminhao c : this.caminhoesPatio) {
            System.out.println(c.getPlaca());
        }
    }

    // Persistência simples usando serialização Java
    public void salvar(String caminhoArquivo) throws IOException {
        File f = new File(caminhoArquivo);
        f.getParentFile().mkdirs();
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f))) {
            oos.writeObject(this.caminhoesPatio);
        }
    }

    @SuppressWarnings("unchecked")
    public void carregar(String caminhoArquivo) throws IOException, ClassNotFoundException {
        File f = new File(caminhoArquivo);
        if (!f.exists()) return;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            this.caminhoesPatio = (ArrayList) ois.readObject();;
        }
    }

}
