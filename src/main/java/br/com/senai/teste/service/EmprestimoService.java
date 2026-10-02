package br.com.senai.teste.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.com.senai.teste.model.Aluno;
import br.com.senai.teste.model.Emprestimo;
import br.com.senai.teste.model.Livro;
import br.com.senai.teste.repository.AlunoRepository;
import br.com.senai.teste.repository.EmprestimoRepository;
import br.com.senai.teste.repository.LivroRepository;

@Service
public class EmprestimoService {
    private final EmprestimoRepository emprestimoRepository;
    private final AlunoRepository alunoRepository;
    private final LivroRepository livroRepository;

    public EmprestimoService(
            EmprestimoRepository emprestimoRepository,
            AlunoRepository alunoRepository,
            LivroRepository livroRepository) {
        this.emprestimoRepository = emprestimoRepository;
        this.alunoRepository = alunoRepository;
        this.livroRepository = livroRepository;
    }

    public Optional<Emprestimo> cadastrar(
            Integer alunoId, Integer livroId, LocalDate dataPrevistaDevolucao) {
        Optional<Aluno> aluno = alunoRepository.findById(alunoId);
        Optional<Livro> livro = livroRepository.findById(livroId);

        if (aluno.isEmpty() || livro.isEmpty()) {
            return Optional.empty();
        }

        boolean livroEmprestado = emprestimoRepository.existsByLivroIdAndDataDevolucaoIsNull(livroId);

        if (livroEmprestado) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "O livro já está emprestado");
        }

        Emprestimo emprestimo = new Emprestimo();
        emprestimo.setAluno(aluno.get());
        emprestimo.setLivro(livro.get());
        emprestimo.setDataEmprestimo(LocalDate.now());
        emprestimo.setDataPrevistaDevolucao(dataPrevistaDevolucao);

        return Optional.of(emprestimoRepository.save(emprestimo));
    }

    public Optional<Emprestimo> devolver(Integer id) {
        Optional<Emprestimo> encontrado = emprestimoRepository.findById(id);

        if (encontrado.isEmpty()) {
            return Optional.empty();
        }

        Emprestimo emprestimo = encontrado.get();

        if (emprestimo.getDataDevolucao() == null) {
            emprestimo.setDataDevolucao(LocalDate.now());
            emprestimoRepository.save(emprestimo);
        }
        return Optional.of(emprestimo);
    }

    public List<Emprestimo> listar() {
        return emprestimoRepository.findAll();
    }

    public List<Emprestimo> listarAtivos() {
        return emprestimoRepository.findByDataDevolucaoIsNull();
    }

    public List<Emprestimo> listarPorAluno(Integer alunoId) {
        return emprestimoRepository.findByAlunoId(alunoId);
    }

    public List<Emprestimo> listarPorLivro(Integer livroId) {
        return emprestimoRepository.findByLivroId(livroId);
    }

    public List<Emprestimo> listarAtrasados() {
        LocalDate dataAtual = LocalDate.now();
        return emprestimoRepository.findByDataPrevistaDevolucaoBeforeAndDataDevolucaoIsNull(dataAtual);
    }

    public Optional<Emprestimo> renovar(
        Integer id, LocalDate novaDataPrevista) {
            Optional<Emprestimo> emprestimOptional = emprestimoRepository.findById(id);

            if (emprestimOptional.isEmpty()) {
                return Optional.empty();
            }

            Emprestimo emprestimo = emprestimOptional.get();

            if (emprestimo.getDataDevolucao() != null) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Um empréstimo não pode ser renovado");
            }

            if(!novaDataPrevista.isAfter(emprestimo.getDataPrevistaDevolucao())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "A nova data deve ser posterior ao prazo atual");
            }

            emprestimo.setDataPrevistaDevolucao(novaDataPrevista);

            return Optional.of(emprestimoRepository.save(emprestimo));
    }
}