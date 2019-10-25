package com.zhaofujun.automapper;

public class MapperTest {

    public static void main(String[] args) {
        IMapper mapper = new AutoMapper();


        mapper.mapping(UserDto.class, UserDo.class)
                .field("contactAddress", "contact.address")
                .field("contactTel", "contact.tel");

        UserDto dto = new UserDto();
        dto.setAge(1);
        dto.setContactAddress("address");
        dto.setContactTel("123");
        dto.setId("11");
        dto.setName("name");
        dto.setSex(true);

        UserDo userDo = mapper.map(dto, UserDo.class);
        System.out.println(userDo);


    }
}

