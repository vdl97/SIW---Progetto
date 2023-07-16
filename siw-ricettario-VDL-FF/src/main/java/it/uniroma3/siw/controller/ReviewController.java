package it.uniroma3.siw.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import it.uniroma3.siw.controller.validator.ReviewValidator;
import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Ricetta;
import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.RicettaService;
import it.uniroma3.siw.service.ReviewService;

@Controller
public class ReviewController {
	@Autowired
	private RicettaService ricettaService;
	@Autowired
	private ReviewService reviewService;
	@Autowired
	private ReviewValidator reviewValidator;
	@Autowired
	private RicettaController ricettaController;
	@Autowired
	private CredentialsService credentialsService;
	
	@GetMapping(value="/default/formNewReview/{id}")
	public String formNewReview(@PathVariable("id") Long id, Model model) {
		
		Ricetta ricetta=this.ricettaService.findById(id);
		model.addAttribute("ricetta", ricetta);
		if(this.reviewService.checkReviewForAuthorAndRelatedRicetta(id)) {
			boolean flag=this.reviewService.checkReviewForAuthorAndRelatedRicetta(id);
			model.addAttribute("flag", flag);
			model.addAttribute("text", new String("Hai già inserito una recensione per questo film!"));
			UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
			String role = credentials.getRole();
			if (role.equals(Credentials.ADMIN_ROLE)) 
				model.addAttribute("role", role);
			return "ricetta.html";
			}
		Review review = new Review();
		model.addAttribute("review",review);

		return "default/formNewReview.html";
		}

	@PostMapping("/default/review/{idR}")
	public String newReview(@PathVariable("idR") Long idR, @Valid @ModelAttribute("review") Review review,
			BindingResult bindingResult, Model model) {
		// DEVO PASSARE ANCHE L'ID DEL FILM, ALTRIMENTI NON SO COME CONTROLLARE SE
		// ESISTE GIA'
		this.reviewValidator.validate(idR, bindingResult);
		if (!bindingResult.hasErrors()) {
			this.reviewService.saveCreatedNewReview(review, idR);

			// NON SERVE ricetta.getReviews().add(review);
			model.addAttribute("review", review);
			return "review.html";
		} else {
			Ricetta ricetta=this.ricettaService.findById(idR);
			model.addAttribute("ricetta", ricetta);
			return "default/formNewReview.html";
			// return "index.html";

		}
	}

	@GetMapping("/review/{id}")
	public String getReview(@PathVariable("id") Long id, Model model) {
		Review review = this.reviewService.findById(id);
		model.addAttribute("review", review);
		return "review.html";
	}

	@GetMapping("/default/updateReview/{id}")
	public String getUpdateReview(@PathVariable("id") Long id, Model model) {
		model.addAttribute("review", this.reviewService.findById(id));

		return "/default/updateReview.html";
	}

	@PostMapping("/default/updateReview/{id}")
	public String updateReview(@PathVariable("id") Long id, @Valid @ModelAttribute("review") Review review,
			BindingResult bindingResult, Model model) {

		if (!bindingResult.hasErrors()) {
			review.setId(id);
			this.reviewService.saveUpdatedReview(review, id);
			// this.reviewRepository.save(review);
			// NON SERVE ricetta.getReviews().add(review);
			review = this.reviewService.findById(id);
			model.addAttribute("review", review);
			return "review.html";
		} else {

			model.addAttribute("review", review);
			return "/default/updateReview.html";

		}
	}
	

	@GetMapping(value = "/admin/deleteReview/{id}")
	public String deleteReview(@PathVariable("id") Long id, Model model) {
		Review review = this.reviewService.findById(id);
		if (review != null)
			model.addAttribute("review", review);
		else
			return "RicettaError.html";
		return "admin/deleteReview.html";
	}

	@GetMapping("admin/confirmDeletionReview/{id}")
	public String confirmDeletionReview(@PathVariable("id") Long id, Model model) {
		Ricetta ricetta=this.reviewService.findById(id).getRelatedRicetta();
		if (this.reviewService.deleteReview(id)) {
			model.addAttribute("ricetta",ricetta);
			return "ricetta.html";
		} else {
			return "RicettaError.html";
		}
	}

}