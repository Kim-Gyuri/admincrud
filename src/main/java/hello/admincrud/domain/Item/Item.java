package hello.admincrud.domain.Item;

import hello.admincrud.domain.ItemType;
import lombok.Data;

import java.util.List;

@Data
public class Item {

    private Long id;

    private String itemName;

    private Integer price;

    private Integer quantity;

    private ItemType itemType;  //상품 상태
    private String selCode; // 분야 분류

    private List<UploadFile> imageFiles;

    public Item() {
    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;

    }

}
