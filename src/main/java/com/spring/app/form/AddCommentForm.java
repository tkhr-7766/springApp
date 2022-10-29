package com.spring.app.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;


@Data
public class AddCommentForm {
    @NotBlank
    @Size(min = 1, max = 500)
    private String content;
}