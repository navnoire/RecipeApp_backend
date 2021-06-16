package info.navnoire.recipeappserver_springboot.controller;

import info.navnoire.recipeappserver_springboot.repository.StepRepository;
import info.navnoire.recipeappserver_springboot.service.ImageService;
import info.navnoire.recipeappserver_springboot.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.NoSuchElementException;

/**
 * Created by Victoria Berezina on 01/06/2021 in RecipeApp project
 */

@RestController
@RequestMapping("/api/image")
public class ImageApiController {
    private ImageService imageService;
    private RecipeService recipeService;
    private StepRepository stepRepository;

    @Autowired
    public void setImageService(ImageService imageService) {
        this.imageService = imageService;
    }

    @Autowired
    public void setRecipeService(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @Autowired
    public void setStepRepository(StepRepository stepRepository) {
        this.stepRepository = stepRepository;
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Resource> getMainImage(@PathVariable("id") int recipeId) {
        try {
            String mainImageUrl = recipeService.findFullRecipeById(recipeId).getMain_image_url();
            return getImageResourceResponseEntity(mainImageUrl);
        } catch (NoSuchElementException elementException) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(path = "/step/{step}")
    public ResponseEntity<Resource> getStepImage(@PathVariable("step") long stepId) {
        try {
            String url = stepRepository.findStepById(stepId).getImageUrl();
            return getImageResourceResponseEntity(url);
        } catch (NoSuchElementException elementException) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    private ResponseEntity<Resource> getImageResourceResponseEntity(String url) {
        try {
            Resource resource = imageService.getImageFromFileSystem(url);
            HttpHeaders headers = new HttpHeaders();
            headers.setCacheControl(CacheControl.noCache().getHeaderValue());
            headers.setContentType(MediaType.IMAGE_JPEG);
            headers.setContentLength(resource.contentLength());
            return new ResponseEntity<>(resource, headers, HttpStatus.OK);
        } catch (IOException ioe) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
