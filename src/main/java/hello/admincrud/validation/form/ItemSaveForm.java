package hello.admincrud.validation.form;

import hello.admincrud.domain.ItemType;
import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class ItemSaveForm {

    @NotBlank
    private String itemName;

    @NotNull
    @Range(min =1000, max = 1000000)
    private Integer price;

    @NotNull
    @Max(value = 9999)
    private  Integer quantity;

    private ItemType itemType;  //상품 상태
    private String selCode; // 분야 분류

    private List<MultipartFile> imageFiles;

}
