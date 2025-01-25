package cohort46.gracebakeryapi.bakery.image.controller;

import cohort46.gracebakeryapi.bakery.image.dto.ImageDto;
import cohort46.gracebakeryapi.bakery.image.service.ImageService;
import cohort46.gracebakeryapi.helperclasses.GlobalVariables;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")

public class ImageController {
    private final ImageService imageService;

    @RequestMapping(value = "/image", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ImageDto addImage(@RequestParam("file") MultipartFile file, @RequestParam("product_id") Long product_id) {
        return imageService.addImage(file, product_id)  ;
    }

    @GetMapping("/image/{id}")
    public ImageDto findImageById(@PathVariable Long id) {
        return imageService.findImageById(id);
    }

    @DeleteMapping("/image/{id}")
    public ImageDto deleteImage(@PathVariable Long id) {
        return imageService.deleteImage(id);
    }

    @RequestMapping(value = "/image", method = RequestMethod.PUT)
    public ImageDto updateImage(@RequestParam("file") MultipartFile file, @RequestParam("image_id") Long image_id, @RequestParam("product_id") Long product_id) {
        return imageService.updateImage(file, image_id, product_id);
    }

    @GetMapping("/images/product/{product_id}")
    public Iterable<ImageDto> findByProductId(@PathVariable Long product_id) {
        return imageService.findByProductId(product_id);
    }

    @RequestMapping(value = "/image/file", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public String pushImageFile(@RequestParam("file") MultipartFile file) {
        return imageService.pushImageFileCloudinary(file);
    }

    @DeleteMapping("/image/file")
    public Boolean deleteImageFile(@RequestBody String link)
    {
        return imageService.deleteImageFileCloudinary(link);
    }

    @GetMapping("/image/file/{filename}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        // Путь к файлу на сервере
        File file = new File(GlobalVariables.getImagesPath() + filename);

        if (file.exists()) {
            Resource resource = new FileSystemResource(file);
            return ResponseEntity.ok()
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}