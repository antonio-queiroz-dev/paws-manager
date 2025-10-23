package com.example.desafioCadastro.controler;

import com.example.desafioCadastro.model.Pergunta;
import com.example.desafioCadastro.service.PerguntaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/perguntas")
public class PerguntaController {

    private PerguntaService perguntaService;

    public PerguntaController(PerguntaService perguntaService) {
        this.perguntaService = perguntaService;
    }

    @GetMapping
    public List<Pergunta> listarPerguntas() {
        return perguntaService.listarPerguntas();
    }
}
