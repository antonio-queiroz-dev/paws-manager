package com.example.desafioCadastro.config;

import com.example.desafioCadastro.model.Pergunta;
import com.example.desafioCadastro.repository.PerguntaRespository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private PerguntaRespository perguntaRespository;

    public DataInitializer(PerguntaRespository perguntaRespository) {
        this.perguntaRespository = perguntaRespository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (perguntaRespository.count() == 0){
            List<Pergunta> perguntasIniciais = List.of(
                    new Pergunta("Qual o nome e sobrenome do pet?"),
                    new Pergunta("Qual o tipo do pet (Cachorro/Gato)?"),
                    new Pergunta("Qual o sexo do animal?"),
                    new Pergunta("Qual endereço e bairro que ele foi encontrado?"),
                    new Pergunta("Qual a idade aproximada do pet?"),
                    new Pergunta("Qual o peso aproximado do pet?"),
                    new Pergunta("Qual a raça do pet?")
            );

            perguntaRespository.saveAll(perguntasIniciais);
            System.out.printf("Perguntas iniciais criadas com sucesso!");
        } else {
            System.out.printf("Perguntas já existentes, nenhuma inserção feita");
        }
    }
}