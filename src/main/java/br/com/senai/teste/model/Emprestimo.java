package br.com.senai.teste.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.math.BigDecimal;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "emprestimo")
public class Emprestimo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDate dataEmprestimo;

    @ManyToOne
    @JoinColumn(name = "aluno_id", nullable = false)
    private Aluno aluno;

    @ManyToOne
    @JoinColumn(name = "livro_id", nullable = false)
    private Livro livro;

    private static final BigDecimal MULTA_POR_DIA = new BigDecimal("2.00");

    public Emprestimo() {
    }

    public Integer getId() {
        return id;
    }

    public LocalDate getDataEmprestimo() {
        return dataEmprestimo;
    }

    public void setDataEmprestimo(LocalDate dataEmprestimo) {
        this.dataEmprestimo = dataEmprestimo;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public Livro getLivro() {
        return livro;
    }

    public void setLivro(Livro livro) {
        this.livro = livro;
    }

    private LocalDate dataDevolucao;

    public LocalDate getDataDevolucao() {
        return dataDevolucao;
    }

    public void setDataDevolucao(LocalDate dataDevolucao) {
        this.dataDevolucao = dataDevolucao;
    }

    private LocalDate dataPrevistaDevolucao;

    public LocalDate getDataPrevistaDevolucao() {
        return dataPrevistaDevolucao;
    }

    public void setDataPrevistaDevolucao(
        LocalDate dataPrevistaDevolucao) {
            this.dataPrevistaDevolucao = dataPrevistaDevolucao;
        }
        public String getSituacao() {
            if(dataDevolucao != null) {
                return "Devolvido";
            }
            if(dataPrevistaDevolucao == null) {
                return "Sem previsão";
            }
            if (dataPrevistaDevolucao.isBefore(LocalDate.now())) {
                return "Atrasado";
            }
            return "Ativo";
        }

    public long getDiasAtraso() {
        if (dataDevolucao == null) {
            return 0;
        }

        LocalDate dataFinal;

        if (dataDevolucao == null) {
            dataFinal = LocalDate.now();
        } else {
            dataFinal = dataDevolucao;
        }

        if (!dataFinal.isAfter(dataPrevistaDevolucao)) {
            return 0;
        }

        return ChronoUnit.DAYS.between(dataPrevistaDevolucao, dataFinal);
    }
}
