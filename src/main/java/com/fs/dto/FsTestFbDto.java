package com.fs.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

/**
 * Created by chesterjavier.
 */
@Getter
@Setter
@Component
public class FsTestFbDto {
	private String email;
	private String password;
}
