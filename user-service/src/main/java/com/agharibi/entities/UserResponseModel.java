package com.agharibi.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseModel {

    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private List<AlbumResponseModel> albums = new ArrayList<>();
}
