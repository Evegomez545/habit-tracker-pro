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

    @GetMapping("/")
    public String listarHabitos(Model model) {
        List<Habito> habitos = repository.findAll();
        
       
        boolean diaFinalizado = !habitos.isEmpty() && habitos.stream().allMatch(Habito::isConcluido);
        
        
        String diaHoje = LocalDate.now().getDayOfWeek().name(); 
        

        if (diaFinalizado) {
           
            if (historicoRepo.findByDiaSemana(diaHoje).isEmpty()) {
                historicoRepo.save(new HistoricoDiario(diaHoje, LocalDate.now()));
            }
        } else {
            historicoRepo.findByDiaSemana(diaHoje).ifPresent(registro -> {
                historicoRepo.delete(registro);

            });
        }

        
        List<String> diasConcluidos = historicoRepo.findAll().stream()
                                         .map(HistoricoDiario::getDiaSemana)
                                         .collect(Collectors.toList());

        model.addAttribute("habitos", habitos);
        model.addAttribute("diaHoje", diaHoje);
        model.addAttribute("diasConcluidos", diasConcluidos); 
        model.addAttribute("diaFinalizado", diaFinalizado); 
        
        long concluidos = habitos.stream().filter(Habito::isConcluido).count();
        int progresso = habitos.isEmpty() ? 0 : (int) ((double) concluidos / habitos.size() * 100);
        
        model.addAttribute("progresso", progresso);
        return "index";
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

    @Scheduled(cron = "0 0 0 * * *")
    public void resetarHabitosDiarios() {
        List<Habito> todos = repository.findAll();
        todos.forEach(h -> h.setConcluido(false));
        repository.saveAll(todos);
    }
}