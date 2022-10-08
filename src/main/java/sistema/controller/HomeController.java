package sistema.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import sistema.model.Professor;

import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HomeController {

    @Autowired
    JdbcTemplate db;

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/professores")
    public String contatos(Model model) {
        List<Professor> listaDeProfessores = db.query(
                "select * from professores",
                (res, rowNum) -> {
                    Professor professor = new Professor(res.getInt("id"), res.getString("nome"));
                    return professor;
                });
        model.addAttribute("professor", listaDeProfessores);
        return "professor";
    }

    @GetMapping("novo")
    public String exibeForm(Model model) {
        model.addAttribute("professor", new Professor());
        return "formulario";
    }

    @PostMapping("novo")
    public String gravaDados(Professor professores) {
        System.out.println("-----------------------");
        System.out.println(professores.getId());
        System.out.println(professores.getNome());

        db.update("insert into professores (nome) values (?)",
                professores.getNome());
        return "redirect:/professores";
    }

}
