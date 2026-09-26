package br.com.senai.teste.service;

import java.time.LocalDate;
import java.util.Optional;

import br.com.senai.teste.model.Aluno;
import br.com.senai.teste.model.Emprestimo;
import br.com.senai.teste.model.Livro;
import br.com.senai.teste.repository.AlunoRepository;
import br.com.senai.teste.repository.EmprestimoRepository;
import br.com.senai.teste.repository.LivroRepository;

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
            Integer alunoId, Integer livroId) {
                Optional<Aluno> aluno = alunoRepository.findById(alunoId);
                Optional<Livro> livro = livroRepository.findById(livroId);

                if (aluno.isEmpty() || livro.isEmpty()) {
                    return Optional.empty();
                }

                Emprestimo emprestimo = new Emprestimo();
                emprestimo.setAluno(aluno.get());
                emprestimo.setLivro(livro.get());
                emprestimo.setDataEmprestimo(LocalDate.now());

                return Optional.of(emprestimoRepository.save(emprestimo));
            }

}
