package com.zhaofujun.automapper;

public class MapperTest1 {

    public static void main(String[] args) {
        IMapper mapper = new AutoMapper();


        mapper.mapping(UserDo.class, UserDto.class)
                .field("contact.address", "contactAddress")
                .field("contact.tel", "contactTel");

        Contact contact = new Contact();
        contact.setAddress("address");
        contact.setTel(11);

        UserDo userDo = new UserDo();
        userDo.setAge(11);
        userDo.setContact(contact);
//        userDo.setId("121");
        userDo.setName("name");
        userDo.setSex(true);

        UserDto userDto = mapper.map(userDo, UserDto.class);
        System.out.println(userDto);


    }
}

