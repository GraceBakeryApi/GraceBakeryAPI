package cohort46.gracebakeryapi.bakery.image.service;

import cohort46.gracebakeryapi.bakery.filter.model.Filter;
import cohort46.gracebakeryapi.bakery.image.dto.ImageDto;
import cohort46.gracebakeryapi.bakery.image.model.Image;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


public interface ImageService {
    ImageDto addImage(MultipartFile file, Long product_id);//Long
    ImageDto findImageById(Long imageId);
    ImageDto deleteImage(Long id);
    ImageDto updateImage(MultipartFile file, Long image_id, Long product_id);
    Image store(Image image);
    Iterable<ImageDto> findByProductId(Long product_id);
    Boolean updateImageFile(MultipartFile file, String name);

    String updateImageFileLink(String newLink, String lastLink);
    String updateImageFileLinkCloudinary(String newLink, String lastLink);
    String updateImageFileLinkFromDiskSpace(String newLink, String lastLink);

    String pushImageFile(MultipartFile file);
    Boolean deleteImageFile(String link);
    String pushImageFileCloudinary(MultipartFile file);
    Boolean deleteImageFileCloudinary(String link);
    String pushImageFileFromDiskSpace(MultipartFile file);
    Boolean deleteImageFileFromDiskSpace(String link);
}
