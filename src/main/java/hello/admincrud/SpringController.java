package hello.admincrud;

import hello.admincrud.domain.FileStore;
import hello.admincrud.domain.Item.Item;
import hello.admincrud.domain.Item.ItemRepository;
import hello.admincrud.domain.Item.UploadFile;
import hello.admincrud.domain.ItemType;
import hello.admincrud.domain.SelCode;
import hello.admincrud.validation.form.ItemSaveForm;
import hello.admincrud.validation.form.ItemUpdateForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/spring")
@RequiredArgsConstructor
public class SpringController {

    private final ItemRepository itemRepository;
    private final FileStore fileStore;

    @ModelAttribute("itemTypes")
    public ItemType[] itemTypes() {
        return ItemType.values();
    }

    @ModelAttribute("selCodes")
    public List<SelCode> selCodes() {
        List<SelCode> selCodes = new ArrayList<>();
        selCodes.add(new SelCode("BOOK", "도서"));
        selCodes.add(new SelCode("MEDIA", "음반/DVD"));
        return selCodes;
    }


    @GetMapping  //home
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "springform/springitems";
    }

    @GetMapping("/add")  // form
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "springform/springforms";
    }

    @PostMapping("/add") //form 상품 등록시 리다이렉션
    public String addItem(@Validated @ModelAttribute ItemSaveForm form, BindingResult bindingResult, RedirectAttributes redirectAttributes) throws IOException {

        //특정 필드가 아닌 복합 룰 검증
        if (form.getPrice() != null && form.getQuantity() != null) {
            int resultPrice = form.getPrice() * form.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
            }
        }

        //검증에 실패하면 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            log.info("errors = {} ", bindingResult);
            return "springform/springforms";
        }

        List<UploadFile> storeImageFiles = fileStore.storeFiles(form.getImageFiles());

        //성공로직
        Item item = new Item();
        item.setItemName(form.getItemName());
        item.setPrice(form.getPrice());
        item.setItemType(form.getItemType());
        item.setQuantity(form.getQuantity());
        item.setSelCode(form.getSelCode());
        item.setImageFiles(storeImageFiles);


        log.info("item.itemType={}", item.getItemType());
        log.info("item.secondType={}", item.getSelCode());

        itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", item.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/spring/{itemId}";
    }


    @GetMapping("/{id}")  //상세 페이지 : home에서 상품클릭
    public String item(@PathVariable Long id, Model model) {
        Item item = itemRepository.findById(id);
        model.addAttribute("item", item);
        return "springform/springitem";
    }

    //이미지 업로드
    @ResponseBody
    @GetMapping("/images/{filename}")
    public Resource downloadImage(@PathVariable String filename) throws MalformedURLException {
        return new UrlResource("file:" + fileStore.getFullPath(filename));
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "springform/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @Validated @ModelAttribute("item") ItemUpdateForm form, BindingResult bindingResult) {

        //특정 필드가 아닌 복합 룰 검증
        if (form.getPrice() != null && form.getQuantity() != null) {
            int resultPrice = form.getPrice() * form.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
            }
        }

        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "springform/editForm";
        }

        Item itemParam = new Item();
        itemParam.setItemName(form.getItemName());
        itemParam.setPrice(form.getPrice());
        itemParam.setQuantity(form.getQuantity());
        itemParam.setItemType(form.getItemType());
        itemParam.setSelCode(form.getSelCode());
        itemRepository.update(itemId, itemParam);

        return "redirect:/spring/{itemId}";
    }


}
