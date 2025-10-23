package com.example.desafioCadastro.service;

import com.example.desafioCadastro.model.Pergunta;
import com.example.desafioCadastro.repository.PerguntaRespository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PerguntaService {

    private PerguntaRespository perguntaRespository;

    public PerguntaService(PerguntaRespository perguntaRespository){
        this.perguntaRespository = perguntaRespository;
    }

    public List<Pergunta> listarPerguntas() {
        return perguntaRespository.findAll();
    }
}
