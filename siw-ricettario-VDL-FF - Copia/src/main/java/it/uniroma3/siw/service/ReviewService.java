package it.uniroma3.siw.service;


import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Ricetta;
import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.repository.ReviewRepository;

@Service
public class ReviewService {

	@Autowired
	private ReviewRepository reviewRepository;
	
	@Autowired
	private CredentialsService credentialsService;
	
	@Autowired
	private RicettaService ricettaService;
	
	@Transactional
	public void saveCreatedNewReview(Review review,Long idM) {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
		
		review.setAuthor(credentials);
		
		Ricetta ricetta=this.ricettaService.findById(idM);
		review.setRelatedRicetta(ricetta);
		this.reviewRepository.save(review); 
				
	}

	@Transactional
	public void saveUpdatedReview(Review review,Long id) {
		
		this.reviewRepository.updateReviewInfo(review.getTitle(), review.getRating(), review.getDescription(), id); 
		
	}

	public Review findById(Long id) {
		return this.reviewRepository.findById(id).orElse(null);
	}
	
	public boolean checkReviewForAuthorAndRelatedRicetta(Long idM) {
		
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(userDetails==null) return false;
		Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
		if(this.reviewRepository.checkReviewForAuthorAndRelatedRicetta(idM,credentials.getId()).isEmpty())
			return false;
		return true;
		
	}

	@Transactional
	public boolean deleteReview(Long id) {
		boolean res=false;
		Review review=this.reviewRepository.findById(id).orElse(null);
		if(review==null)
			return res;
		else {
		this.reviewRepository.delete(review);
		return true;
		}
	}
}
