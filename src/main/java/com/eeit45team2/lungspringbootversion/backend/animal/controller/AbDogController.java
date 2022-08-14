package com.eeit45team2.lungspringbootversion.backend.animal.controller;

import com.eeit45team2.lungspringbootversion.backend.animal.json.JsonExporter;
import com.eeit45team2.lungspringbootversion.backend.animal.model.AbDogBean;
import com.eeit45team2.lungspringbootversion.backend.animal.repository.AbDogRepository;
import com.eeit45team2.lungspringbootversion.backend.animal.service.AbDogService;
import com.eeit45team2.lungspringbootversion.backend.animal.util.FileUploadUtil;
import com.eeit45team2.lungspringbootversion.backend.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

@Controller
@RequestMapping("/Backendanimal")
public class AbDogController {
//	@Autowired
//	private EmailSenderService senderService;
@Autowired
private JavaMailSender mailSender;
	//自動實現
	@Autowired
	private AbDogRepository abdogRepository;
	@Autowired
	private JsonExporter jsonExporter;
//	@Autowired
//	ServletContext ctx;
	@Autowired
	private AbDogService abdogService;
//	@Autowired
//  public AbDogController(AbDogService abDogService, ServletContext ctx, AbDogRepository abdogRepository,JavaMailSender mailSender,JsonExporter jsonExporter ) {
//	this.abdogService = abDogService;
//	this.ctx = ctx;
//	this.abdogRepository = abdogRepository;
//	this.mailSender =mailSender;
//	this.jsonExporter = jsonExporter;
//}


	@GetMapping("/showjs")
	public String showjs1(Model model) {//call service撈資料 >連API>listall

		return "/Backendanimal/abchartjs";
		//真實程式 非虛擬路徑
	}

	@GetMapping("/abdoglist")
	public String listabdogs(Model model, @Param("keyword") String keyword) {//call service撈資料 >連API>listall
		List<AbDogBean> abdogbeans = abdogService.abdoglistAll(keyword);
		model.addAttribute("abdogs", abdogbeans);
		model.addAttribute("keyword", keyword);

		return "/Backendanimal/BackAbDog";
		//真實程式 非虛擬路徑
	}


	@RequestMapping("/abshowForm")
	public String showFormForAdd(Model model) {
		AbDogBean abdogbean = new AbDogBean();
		model.addAttribute("abdog", abdogbean);
		return "Backendanimal/abdogNewForm";
	}

	@PostMapping("/saveAbDog")
	public RedirectView AbDogSave(AbDogBean abdogbean,
								  @RequestParam("image") MultipartFile multipartFile) throws IOException {
		SimpleMailMessage message = new SimpleMailMessage();
		String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
		abdogbean.setAbphonto(fileName);
		abdogService.save(abdogbean);
		//String uploadDir = "./user-photos/" +book.getId();./是當前目錄/user-photos/book.getId()
		//   String uploadDir = "./user-photos/"  ;// ./是當前目錄/user-photos
		String uploadDir = "./src/main/resources/static/BackEnd/images/animal/";
		FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		message.setFrom("Lunghipeace0302@gmail.com");
		message.setTo(abdogbean.getAbemail());
		message.setSubject("謝謝您的來信");
		message.setText("目前表單正在審核中");
		mailSender.send(message);
		System.out.println("Mail Sent succesfully...");
		return new RedirectView("/Backendanimal/abdoglist", true);

	}


	@GetMapping(value = "/abdelete/{abid}")
	public String deleteabdog(@PathVariable Long abid) {
		abdogService.delete(abid);
		return "redirect:/Backendanimal/abdoglist";
	}


	@GetMapping("/abupdateForm/{abid}")
	public ModelAndView abUpdate(@PathVariable Long abid) {
		ModelAndView mav = new ModelAndView("Backendanimal/abdogEditForm");//指向orderEditForm.html
		AbDogBean abDogBean = abdogService.get(abid);
		mav.addObject("abdog", abDogBean);
		return mav;
	}


	@RequestMapping("/pass/{id}")
	public String pass(@PathVariable(name = "id") int abid) {
		AbDogBean abDogBean = abdogService.get(abid);
		abDogBean.setAbaudit(0);
		abdogService.save(abDogBean);
		return "redirect:/Backendanimal/abdoglist";
	}


	@RequestMapping(value = "/down", produces = "application/json;charset=UTF-8")
	//  @GetMapping("/down"     )
	public ResponseEntity<byte[]> downloadJsonFile() {
		List<AbDogBean> abdogbeans = (List<AbDogBean>) abdogRepository.findAll();
		String abdogJsonString = jsonExporter.export(abdogbeans);
		//byte[] productJsonBytes = productJsonString.getBytes();
		byte[] abdogJsonBytes = abdogJsonString.getBytes(Charset.forName("utf-8"));
		//(json.getBytes(Charset.forName("GBK"))); //將json資料寫入流中
		return ResponseEntity
				.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=customers.json")
				.contentType(MediaType.APPLICATION_JSON)
				.contentLength(abdogJsonBytes.length)
				.body(abdogJsonBytes);
	}



}


