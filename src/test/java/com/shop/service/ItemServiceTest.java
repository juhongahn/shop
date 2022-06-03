package com.shop.service;

import com.shop.constant.ItemSellStatus;
import com.shop.dto.ItemFormDto;
import com.shop.dto.MemberFormDto;
import com.shop.entity.Item;
import com.shop.entity.ItemImg;
import com.shop.entity.Member;
import com.shop.repository.ItemImgRepository;
import com.shop.repository.ItemRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityExistsException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestPropertySource(locations = "classpath:application-test.yml")
@Transactional
@SpringBootTest
class ItemServiceTest {

    @Autowired
    ItemService itemService;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    ItemImgRepository itemImgRepository;

    List<MultipartFile> createMultipartFiles() {
        List<MultipartFile> multipartFileList = new ArrayList<>();

        for (int i=0;i<5;i++){
            String path ="C:/my-workspace/shop/item";
            String imageName = "image" + i + ".jpg";
            MockMultipartFile multipartFile =
                    new MockMultipartFile(path, imageName, "image/jpg", new byte[]{1,2,3,4});
            multipartFileList.add(multipartFile);
        }
        return multipartFileList;
    }


    @Test
    @DisplayName("상품 등록 테스트")
    @WithMockUser(username = "juhong", roles = "ADMIN")
    void saveItem() {

        ItemFormDto itemFormDto = ItemFormDto.builder()
                .itemNm("테스트 상품")
                .itemSellStatus(ItemSellStatus.SELL)
                .itemDetail("테스트 상품 입니다.")
                .price(1000)
                .stockNumber(100)
                .build();

        List<MultipartFile> multipartFileList = createMultipartFiles();
        try{
            Long itemId = itemService.saveItem(itemFormDto, multipartFileList);
            List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);
            Item item = itemRepository.findById(itemId).orElseThrow(EntityExistsException::new);

            assertEquals(itemFormDto.getItemNm(), item.getItemNm());
            assertEquals(itemFormDto.getItemSellStatus(), item.getItemSellStatus());
            assertEquals(itemFormDto.getItemDetail(), item.getItemDetail());
            assertEquals(itemFormDto.getPrice(), item.getPrice());
            assertEquals(itemFormDto.getStockNumber(), item.getStockNumber());
            assertEquals(multipartFileList.get(0).getOriginalFilename(),
                    itemImgList.get(0).getOriImgName());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}