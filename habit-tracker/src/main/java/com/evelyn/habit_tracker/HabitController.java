package com.evelyn.habit_tracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.time.LocalDate;
import java.util.stream.Collectors;

@Controller
public class HabitController {

    @Autowired
    private HabitoRepository repository;

    @Autowired
    private HistoricoRepository historicoRepo;

  
    @Autowired
    private UsuarioRepository usuarioRepository; 

    @GetMapping("/")
    public String listarHabitos(Model model) {
        List<Habito> habitos = repository.findAll();
        String diaHoje = LocalDate.now().getDayOfWeek().name();

        long concluidosCount = habitos.stream().filter(Habito::isConcluido).count();
        double progressoReal = habitos.isEmpty() ? 0 : ((double) concluidosCount / habitos.size()) * 100;

        HistoricoDiario registroDeHoje = historicoRepo.findByDiaSemana(diaHoje)
                .orElse(new HistoricoDiario(diaHoje, LocalDate.now()));

        registroDeHoje.setPorcentagem(progressoReal);
        historicoRepo.save(registroDeHoje);

        boolean diaTotalmenteConcluido = !habitos.isEmpty() && progressoReal == 100.0;

        List<String> diasVitoriosos = historicoRepo.findAll().stream()
                .filter(h -> h.getPorcentagem() == 100.0)
                .map(HistoricoDiario::getDiaSemana)
                .collect(Collectors.toList());

        model.addAttribute("habitos", habitos);
        model.addAttribute("diaHoje", diaHoje);
        model.addAttribute("diasConcluidos", diasVitoriosos);
        model.addAttribute("diaFinalizado", diaTotalmenteConcluido);
        model.addAttribute("progresso", (int) progressoReal);

        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login"; 
    }

    
    @GetMapping("/cadastro")
    public String mostrarCadastro() {
        return "cadastro";
    }

    @PostMapping("/cadastro")
    public String cadastrarUsuario(@RequestParam String nome, @RequestParam String email, @RequestParam String senha) {
        Usuario novo = new Usuario();
        novo.setNome(nome);
        novo.setEmail(email);
        novo.setSenha("{noop}" + senha); 
        usuarioRepository.save(novo);
        return "redirect:/login?sucesso";
    }

    @PostMapping("/adicionar")
    public String adicionarHabito(@RequestParam("nome") String nome) {
        Habito novoHabito = new Habito();
        novoHabito.setNome(nome);
        novoHabito.setConcluido(false);
        repository.save(novoHabito);
        return "redirect:/";
    }

    @GetMapping("/concluir/{id}")
    public String alternarStatus(@PathVariable Long id) {
        Habito habito = repository.findById(id).orElseThrow();
        habito.setConcluido(!habito.isConcluido());
        repository.save(habito);
        return "redirect:/";
    }

    @GetMapping("/deletar/{id}")
    public String deletarHabito(@PathVariable Long id) {
        repository.deleteById(id);
        return "redirect:/";
    }

    @GetMapping("/editar")
    public String editarHabito(@RequestParam("id") Long id, @RequestParam("novoNome") String novoNome) {
        Habito habito = repository.findById(id).orElseThrow();
        habito.setNome(novoNome);
        repository.save(habito);
        return "redirect:/";
    }

    @Scheduled(cron = "0 0 0 * * *", zone = "America/Sao_Paulo")
    public void resetarHabitosDiarios() {
        List<Habito> todos = repository.findAll();
        todos.forEach(h -> h.setConcluido(false));
        repository.saveAll(todos);
    }
}