package com.example.gestaoPetApi.config;

import com.example.gestaoPetApi.model.Pergunta;
import com.example.gestaoPetApi.repository.PerguntaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Profile("!test")
public class DataInitializer implements CommandLineRunner {

    private final PerguntaRepository perguntaRepository;

    public DataInitializer(PerguntaRepository perguntaRepository) {
        this.perguntaRepository = perguntaRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (perguntaRepository.count() == 0){
            List<Pergunta> perguntasIniciais = List.of(
                    new Pergunta("Qual o nome e sobrenome do pet?"),
                    new Pergunta("Qual o tipo do pet (Cachorro/Gato)?"),
                    new Pergunta("Qual o sexo do animal?"),
                    new Pergunta("Qual endereço e bairro que ele foi encontrado?"),
                    new Pergunta("Qual a idade aproximada do pet?"),
                    new Pergunta("Qual o peso aproximado do pet?"),
                    new Pergunta("Qual a raça do pet?")
            );

            perguntaRepository.saveAll(perguntasIniciais);
            System.out.println("Perguntas iniciais criadas com sucesso!");
        } else {
            System.out.println("Perguntas já existentes, nenhuma inserção feita");
        }
    }
}
