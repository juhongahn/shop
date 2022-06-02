package com.shop.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "item_img")
@Data
public class ItemImg extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "item_img_id")
    private Long id;

    private String imgName;

    private String oriImgName;

    private String imgUrl;

    // 대표이미지 여부
    private String repimgYn;

    // 상품 하나당 여러 사진 가능
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;


    // 도메인과 직접 관련된 메서드는 도메인 클래스에 넣어주면 좋음.
    public void updateItemImg(String oriImgName, String imgName, String imgUrl) {
        this.oriImgName = oriImgName;
        this.imgName = imgName;
        this.imgUrl = imgUrl;
    }
}
