package com.refugio.web.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.refugio.web.DAO.AnimalDAO;
import com.refugio.web.DAO.FamiliaDAO;
import com.refugio.web.DAO.RazaDAO;
import com.refugio.web.entity.Animal;
import com.refugio.web.entity.Raza;

@Controller
@RequestMapping("/animal")
public class AnimalController {

	@Autowired
	private AnimalDAO aDAO;
	
	@Autowired
	private RazaDAO rDAO;
	
	@Autowired
	private FamiliaDAO fDAO;
	
	
	
	
	@GetMapping("/listar")
	public String listar(Model model) {
		
		model.addAttribute("animales",aDAO.crud().findAll());
		return "listar.html";
	}
	
	@GetMapping("/eliminar")
	public String eliminar(Model model,RedirectAttributes ra,@RequestParam("id") int id) {
		
		String mensaje="";
		
		try {
			aDAO.crud().deleteById(id);
			mensaje="Eliminado correctamente";
		} catch (Exception e) {
			mensaje="error al eliminar "+e.getMessage();
		}
		
		ra.addFlashAttribute("mensaje", mensaje);
		
		return "redirect:listar";
	}
	
	@GetMapping("/crear")
	public String crear(Model model) {
		model.addAttribute("razas", rDAO.crud().findAll());
		model.addAttribute("familias", fDAO.crud().findAll());
		
		return "agregar.html";
	}
	
	@PostMapping("/almacenar")
	public String almacenar(Model model,RedirectAttributes ra,
			@RequestParam("txtNombre") String nombre,
			@RequestParam("txtFechaIngreso")
			@DateTimeFormat(pattern="yyyy-MM-dd") Date fechaIngreso,
			@RequestParam("txtPeso") float peso,
			@RequestParam("cboRaza") int razaId) {
		String mensaje= "error al agregar";
		
		try {
			
			Raza r=new Raza();
			r.setId(razaId);
			
			Animal a=new Animal();
			a.setNombre(nombre);
			a.setFechaIngreso(fechaIngreso);
			a.setPeso(peso);
			a.setRaza(r);
			
			Animal ag =aDAO.crud().save(a);
			
			if(ag!=null) {
				mensaje="¡Animal Agregado!";
			}
			
			ra.addFlashAttribute("mensaje", mensaje);	
			
		} catch (Exception e) {
			mensaje="error al agregar "+e.getMessage();
		}
		
		
		
		return "redirect:crear";
	}
	
	
	@GetMapping("/editar")
	public String editar(Model model, RedirectAttributes ra, @RequestParam("id") int id) {
		
		Animal a=null;
		try {
			a=aDAO.crud().findById(id).get();
		} catch (Exception e) {
			ra.addFlashAttribute("mensaje", "el animal no existe");
			return "redirect:listar";
		}
		model.addAttribute("a", a);
		model.addAttribute("razas", rDAO.crud().findAll());
		model.addAttribute("familias", fDAO.crud().findAll());
		
		return "modificar.html";
	}
	
	
	@PostMapping("/actualizar")
	public String actualizar(Model model,RedirectAttributes ra,
			@RequestParam("txtId") int Id,
			@RequestParam("txtNombre") String nombre,
			@RequestParam("txtFechaIngreso")
			@DateTimeFormat(pattern="yyyy-MM-dd") Date fechaIngreso,
			@RequestParam("txtPeso") float peso,
			@RequestParam("cboRaza") int razaId) {
		String mensaje= "error al modificar";
		
		try {
			
			Raza r=new Raza();
			r.setId(razaId);
			
			Animal a=null;
			try {
				a=aDAO.crud().findById(Id).get();
			} catch (Exception e) {
				ra.addFlashAttribute("mensaje", "el animal no existe");
				return "redirect:listar";
			}
			
			a.setId(Id);
			a.setNombre(nombre);
			a.setFechaIngreso(fechaIngreso);
			a.setPeso(peso);
			a.setRaza(r);
			
			Animal ag =aDAO.crud().save(a);
			
			if(ag!=null) {
				mensaje="¡Animal Modificado!";
			}
			
			ra.addFlashAttribute("mensaje", mensaje);	
			
		} catch (Exception e) {
			mensaje="error al modificar "+e.getMessage();
		}
		
		
		
		return "redirect:listar";
	}
	
}
