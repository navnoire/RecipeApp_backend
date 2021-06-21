package info.navnoire.recipeappserver_springboot.controller;

import info.navnoire.recipeappserver_springboot.controller.exhandling.ResourceNotFoundException;
import info.navnoire.recipeappserver_springboot.domain.recipe.Recipe;
import info.navnoire.recipeappserver_springboot.domain.recipe.Step;
import info.navnoire.recipeappserver_springboot.repository.recipe.StepRepository;
import info.navnoire.recipeappserver_springboot.service.ImageService;
import info.navnoire.recipeappserver_springboot.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
           Recipe recipe = recipeService.findFullRecipeById(recipeId)
                   .orElseThrow(() -> new ResourceNotFoundException("Not found recipe with id " + recipeId));
            return getImageResourceResponseEntity(recipe.getMain_image_url());

    }

    @GetMapping(path = "/step/{step}")
    public ResponseEntity<Resource> getStepImage(@PathVariable("step") long stepId) {
            Step step = stepRepository.findById(stepId)
                    .orElseThrow(() -> new ResourceNotFoundException("Not Found step with id " + stepId));
            return getImageResourceResponseEntity(step.getImageUrl());
    }

    private ResponseEntity<Resource> getImageResourceResponseEntity(String url) {
            Resource resource = imageService.getImageFromFileSystem(url)
                    .orElseThrow(() -> new ResourceNotFoundException("Image Not Found"));
            HttpHeaders headers = new HttpHeaders();
            headers.setCacheControl(CacheControl.noCache().getHeaderValue());
            headers.setContentType(MediaType.IMAGE_JPEG);
            return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }
}
