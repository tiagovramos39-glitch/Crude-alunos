package br.com.senai.teste.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.senai.teste.model.Emprestimo;

public interface EmprestimoRepository 
    extends JpaRepository<Emprestimo, Integer>{

        boolean existsByLivroIdAndDataDevolucaoIsNull(Integer livroId);

        List<Emprestimo> findByDataDevolucaoIsNull();
        List<Emprestimo> findByAlunoId(Integer alunoId);
        List<Emprestimo> findByLivroId(Integer livroId);

        List<Emprestimo> findByDataPrevistaDevolucaoBeforeAndDataDevolucaoIsNull(
            LocalDate dataAtual);
    }