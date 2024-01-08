package com.blog.payload;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private long cmt_id;
    @NotBlank
    @Size(min = 3, message = "name must be at least 3 Characters")
    private String name;
    @Email()
    private String email;
    @NotBlank
    @Size(min = 10, message = "comment must be at least 10 Characters")
    private String body;

}
