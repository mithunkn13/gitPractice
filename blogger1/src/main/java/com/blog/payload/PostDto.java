package com.blog.payload;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data@NoArgsConstructor@AllArgsConstructor
public class PostDto
{
    private long id;
    @NotBlank
    @Size(min = 5,message = "The Tiltle Should be Atleast 5 Characters")
    private String title;
    @NotEmpty
    @Size(min=10,message = "Description should be atleast 10 Characters")
    private String description;
    @NotBlank
    @Size(min = 20,message = "Content should atleast have 20 Characters")
    private String content;

}
