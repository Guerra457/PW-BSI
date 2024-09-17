package br.edu.ifg.luziania.model.bo;

import br.edu.ifg.luziania.model.dao.ChamadoDAO;
import br.edu.ifg.luziania.model.dto.ChamadoDTO;
import br.edu.ifg.luziania.model.entity.Chamado;
import br.edu.ifg.luziania.model.entity.Status;
import br.edu.ifg.luziania.model.entity.Usuario;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ChamadoBO {
    @Inject
    private ChamadoDAO chamadoDAO;

    public Chamado cadastrarChamado(ChamadoDTO chamadoDTO, Usuario usuarioLogado) throws Exception {

        System.out.println("Título recebido: " + chamadoDTO.getTitulo());
        System.out.println("Descrição recebida: " + chamadoDTO.getDescricao());
        System.out.println("Usuário logado: " + (usuarioLogado != null ? usuarioLogado.getNome() : "Nenhum usuário"));

        Chamado chamado = new Chamado();
        chamado.setTitulo(chamadoDTO.getTitulo());
        chamado.setDescricao(chamadoDTO.getDescricao());

        if (usuarioLogado == null) {
            throw new Exception("Usuário logado não encontrado");
        }

        Status statusPadrao = chamadoDAO.buscarStatusPadrao();
        if (statusPadrao == null) {
            throw new Exception("Status padrão não encontrado");
        }

        chamado.setSolicitante(usuarioLogado);
        chamado.setStatus(statusPadrao);

        System.out.println("Chamado antes de salvar: " + chamado);

        chamadoDAO.salvar(chamado);

        System.out.println("Chamado após salvar:" + chamado);

        return chamado;
    }
}
