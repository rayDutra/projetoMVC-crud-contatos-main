package sistema.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import sistema.model.Professor;

@Controller
public class HomeController {

    @Autowired
    JdbcTemplate db;

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @PostMapping("novo")
    public String novoProfessor(Professor professor) {
        System.out.println("passei por aqui");
        db.update("insert into professores(nome) values(?)", professor.getNome());
        return "home";

    }

    @GetMapping("/professores")
    public String prof(Model model) {
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

    @GetMapping("excluir-professor")
    public String apagarProfessor(@RequestParam(value = "id", required = true) Integer cod) {
        db.update("delete from professores where id = ?", cod);
        return "redirect:/professores";
    }

    @GetMapping("editar-professor")
    public String mostraFormAlteraprofessor(@RequestParam(value = "id", required = true) Integer cod, Model model) {
        System.out.println("--------------------> " + cod);
        Professor professor = db.queryForObject(
                "select * from professores where id = ?",
                (rs, rowNum) -> {
                    Professor c = new Professor();
                    c.setId(rs.getInt("id"));
                    c.setNome(rs.getString("nome"));
                    return c;
                },
                cod);
        model.addAttribute("prof", professor);
        return "formeditaprofessor";
    }

    @PostMapping("gravaprofessormodificado")
    public String gravaProfessorModificado(Professor professor) {
        db.update(
                "update professores set nome=? where id = ?",
                professor.getNome(),
                professor.getId());
        return "redirect:/professores";
    }
}