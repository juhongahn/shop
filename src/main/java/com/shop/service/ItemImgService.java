package com.shop.service;

import com.shop.entity.ItemImg;
import com.shop.repository.ItemImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemImgService {

    // application.yml 저장해두었던 상품 이미지 업로드 경로를 주입한다.
    @Value("${itemImgLocation}")
    private String itemImgLocation;
    
    private final ItemImgRepository itemImgRepository;
    
    // 파일을 실제 컴퓨터에 쓸 파일서비스
    private final FileService fileService;


    public void saveItemImg(ItemImg itemImg, MultipartFile itemImgFile) throws IOException {
        String oriImgName = itemImgFile.getOriginalFilename();
        String imgName = "";
        String imgUrl = "";

        // 파일 서비스를 통해서 업로드
        // 사용자가 상품의 이미지를 등록했다면 저장할 경로와 파일의 이름, 파일의 바이트 배열을 파일 업로드 파라미터로 해서
        // uploadFile 메서드를 호출한다.(저장된 파일의 이름을 반환) 호출결과를 로컬에 저장된 파일의 이름을 imgName 변수에 저장
        if (!StringUtils.isEmpty(oriImgName)) {
            imgName = fileService.uploadFile(itemImgLocation, oriImgName,
                    itemImgFile.getBytes());
            
            // 저장한 상품 이미지를 불러올 경로를 설정한다.
            imgUrl = "/images/item/" + imgName;
        }

        itemImg.updateItemImg(oriImgName, imgName, imgUrl);
        itemImgRepository.save(itemImg);
    }

    public void updateItemImg(Long itemImgId, MultipartFile itemImgFile) {
        if(!itemImgFile.isEmpty()) {
            // db에서 꺼내서 영속성 컨텍스트에 저장, 이후 변경이 일어나면 변경감지로 트랜잭션이 끝나면 자동으로 업데이트됨.
            ItemImg savedItemImg = itemImgRepository.findById(itemImgId).orElseThrow(EntityNotFoundException::new);


            // 로컬의 기존 이미지 파일은 삭제하고
            // (UUID 로 중복되지 않는 이름으로 파일명을 db에 저장했었다.)
            if (!StringUtils.isEmpty(savedItemImg.getImgName())) {
                fileService.deleteFile(itemImgLocation+"/"+savedItemImg.getImgName());
            }

            String oriImgName = itemImgFile.getOriginalFilename();
            try {
                String imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
                String imgUrl = "/image/item/" + imgName;
                // imgUrl는 브라우저가 해당 /image/item/... 형식의 imgUrl로 요청을 보내면 만들어둔 리소스핸들러가
                // file:///c:... 해당 경로로 연결해주도록 하기위해 사용될것이다.
                savedItemImg.updateItemImg(oriImgName, imgName, imgUrl);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
