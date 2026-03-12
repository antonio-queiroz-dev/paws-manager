package com.example.gestaoPetApi.service;

import com.example.gestaoPetApi.model.Pergunta;
import com.example.gestaoPetApi.repository.PerguntaRepository;
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
