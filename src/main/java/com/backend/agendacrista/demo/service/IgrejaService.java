package com.backend.agendacrista.demo.service;

import com.backend.agendacrista.demo.controller.form.AtualizaIgrejaForm;
import com.backend.agendacrista.demo.error.ResourceNotFoundException;
import com.backend.agendacrista.demo.error.UserPricipalNotAutorizedException;
import com.backend.agendacrista.demo.model.Endereco;
import com.backend.agendacrista.demo.model.Igreja;
import com.backend.agendacrista.demo.model.StatusIgreja;
import com.backend.agendacrista.demo.model.Usuario;
import com.backend.agendacrista.demo.repository.CidadeRepository;
import com.backend.agendacrista.demo.repository.IgrejaRepository;
import com.backend.agendacrista.demo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IgrejaService {
    boolean isFavoritada;
    @Autowired
    IgrejaRepository igrejaRepository;

    @Autowired
    CidadeRepository cidadeRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    public List<Igreja> igrejasFavoritasPorUsuarioLogado() {
        return usuarioRepository.getOne(UsusarioService.getIdUsuarioLogado()).getIgrejasFavoritas();
    }

    public void adicionaIgrejaFavoritaPorId(Long id) {
        Igreja igreja = igrejaRepository.getOne(id);
        verificaSeIgrejaNaoEhFavorito(igreja);
        usuarioRepository.getOne(UsusarioService.getIdUsuarioLogado()).getIgrejasFavoritas().add(igreja);
    }

    public boolean verificaIgrejaEFavoritada(Long id) {
        List<Igreja> igrejasFavoritas = usuarioRepository.getOne(UsusarioService.getIdUsuarioLogado()).getIgrejasFavoritas();
        return igrejasFavoritas.contains(new Igreja(id));
    }

    public void removeIgrejaFavoritaPorId(Long id) {
        Igreja igreja = igrejaRepository.getOne(id);
        verificaSeIgrejaEhFavorito(igreja);
        usuarioRepository.getOne(UsusarioService.getIdUsuarioLogado()).getIgrejasFavoritas().remove(igreja);
    }

    public void verificaSeIdIgrejaExiste(Long id) {
        if (igrejaRepository.findById(id).isEmpty())
            throw new ResourceNotFoundException("Id Igreja inválido");
    }

    private boolean verificaSeIgrejaNaoEhFavorito(Igreja igreja) {
        Usuario usuario = usuarioRepository.getOne(UsusarioService.getIdUsuarioLogado());
        if (usuario.getIgrejasFavoritas().contains(igreja))
            throw new UnsupportedOperationException("Igreja ja é favorita");
        else{
            return false;
        }
    }

    private void verificaSeIgrejaEhFavorito(Igreja igreja) {
        Usuario usuario = usuarioRepository.getOne(UsusarioService.getIdUsuarioLogado());
        if (!usuario.getIgrejasFavoritas().contains(igreja))
            throw new UnsupportedOperationException("Igreja não é favorita");
    }

    public void verificaSeUsuarioLogadoAutorIgreja(Long idIgreja) {
        if (igrejaRepository.getOne(idIgreja).getUsuario().getId() != UsusarioService.getIdUsuarioLogado())
            throw new UserPricipalNotAutorizedException("Usuário não tem permição");
    }

    public void alteraStausIgreja(Long idIgreja, StatusIgreja statusIgreja) {
        verificaSeIdIgrejaExiste(idIgreja);
        Igreja igreja = igrejaRepository.getOne(idIgreja);
        igreja.setStatusIgreja(statusIgreja);
    }

    public Igreja atualizaIgreja(Long idIgreja, AtualizaIgrejaForm igrejaForm) {
        Igreja igreja = igrejaRepository.getOne(idIgreja);
        igreja.setNome(igrejaForm.getNome());
        igreja.setDescricao(igrejaForm.getDescricao());
        igreja.setImagem_url(igrejaForm.getImagem_url());
        igreja.setTelefone(igrejaForm.getTelefone());
        Endereco endereco = igreja.getEndereco();
        endereco.setRua(igrejaForm.getEndereco().getRua());
        endereco.setNumero(igrejaForm.getEndereco().getNumero());
        endereco.setBairro(igrejaForm.getEndereco().getBairro());
        endereco.setComplemento(igrejaForm.getEndereco().getComplemento());
        endereco.setCidade(cidadeRepository.getOne(igrejaForm.getEndereco().getCidade_id()));
        return igreja;
    }
}
