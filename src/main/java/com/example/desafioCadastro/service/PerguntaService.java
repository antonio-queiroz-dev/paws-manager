package com.example.desafioCadastro.service;

import com.example.desafioCadastro.model.Pergunta;
import com.example.desafioCadastro.repository.PerguntaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PerguntaService {

    private PerguntaRepository perguntaRepository;

    public PerguntaService(PerguntaRepository perguntaRepository){
        this.perguntaRepository = perguntaRepository;
    }

    public List<Pergunta> listarPerguntas() {
        return perguntaRepository.findAll();
    }
}
