package com.boot.learningspirit.dto;

import com.boot.learningspirit.entity.BanJi;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Project: learningSpirit
 * @Author: DengYinzhe
 * @Date: 2023/3/1 21:59
 * @FileName: BanJiDto
 * @Description:
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BanJiDto extends BanJi {

    private String id;

}
