package com.zhaofujun.automapper;

import com.zhaofujun.automapper.beans.Contact;
import com.zhaofujun.automapper.beans.UserDo;
import com.zhaofujun.automapper.beans.UserDto;
import com.zhaofujun.automapper.beans.UserDtoSubClass;
import org.junit.Assert;
import org.junit.Test;

public class ComplexMapperTest {

    @Test
    public void complexToSimple() {
        IMapper mapper = new AutoMapper();
        mapper.mapping(UserDo.class, UserDto.class)
                .field("contact.address", "contactAddress")
                .field("contact.tel", "contactTel");

        Contact contact = new Contact();
        contact.setAddress("address");
        contact.setTel(11);

        UserDo userDo = new UserDo();
        userDo.setContact(contact);

        UserDto userDto = mapper.map(userDo, UserDto.class);

        Assert.assertEquals(userDo.getContact().getAddress(), userDto.getContactAddress());


    }

    @Test
    public void SimpleToComplex() {
        IMapper mapper = new AutoMapper();
        mapper.mapping(UserDto.class, UserDo.class)
                .field("contactTel", "contact.tel")
                .field("contactAddress", "contact.address");


        UserDto userDto = new UserDto();
        userDto.setContactTel("123");
        userDto.setContactAddress("address");

        UserDo userDo = mapper.map(userDto, UserDo.class);

        Assert.assertEquals(userDo.getContact().getAddress(), userDto.getContactAddress());


    }

    @Test
    public void subClassTest() {
        IMapper mapper = new AutoMapper();
        mapper.mapping(UserDto.class, UserDo.class)
                .field("contactTel", "contact.tel")
                .field("contactAddress", "contact.address");


        UserDtoSubClass userDto = new UserDtoSubClass();
        userDto.setContactTel("123");
        userDto.setContactAddress("address");

        UserDo userDo = mapper.map(userDto, UserDo.class);

        Assert.assertEquals(userDo.getContact().getAddress(), userDto.getContactAddress());
    }
}

