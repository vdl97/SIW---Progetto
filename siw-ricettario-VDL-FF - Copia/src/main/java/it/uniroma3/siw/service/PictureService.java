package it.uniroma3.siw.service;

import java.io.IOException;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.model.Ingrediente;
import it.uniroma3.siw.model.Picture;
import it.uniroma3.siw.model.Ricetta;
import it.uniroma3.siw.repository.PictureRepository;

@Service
public class PictureService {

	@Autowired 
	private PictureRepository pictureRepository;
	
	public Picture findPictureById(Long id) {
		return this.pictureRepository.findById(id).orElse(null);
	}

	
	public void setPicturesForRicetta(Ricetta ricetta, MultipartFile[] files) throws IOException {
		
		if (ricetta.getPictures().isEmpty())
			ricetta.setPictures(new HashSet<Picture>());
		Picture[] pictures = this.savePictureIfNotExistsOrRetrieve(files);
		for (Picture p : pictures) {
			ricetta.getPictures().add(p);
		}

	}
	
	public void setPicturesForIngrediente(Ingrediente ingrediente, MultipartFile[] files) throws IOException {
		
		if (ingrediente.getPictures().isEmpty())
			ingrediente.setPictures(new HashSet<Picture>());
		Picture[] pictures = this.savePictureIfNotExistsOrRetrieve(files);
		for (Picture p : pictures) {
			ingrediente.getPictures().add(p);
		}

	}
	
	private Picture[] savePictureIfNotExistsOrRetrieve(MultipartFile[] files) throws IOException {
        Picture[] pictures = new Picture[files.length];
        int i=0;
        for(MultipartFile f:files) {
            Picture picture;
            picture = new Picture();
            picture.setName(f.getResource().getFilename());
            picture.setData(f.getBytes());
            this.pictureRepository.save(picture);
            pictures[i] = picture;
            i++;    
        }
        return pictures;
    }
}
