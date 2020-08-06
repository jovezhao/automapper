package com.zhaofujun.automapper;

import com.esotericsoftware.reflectasm.FieldAccess;
import com.esotericsoftware.reflectasm.MethodAccess;
import com.zhaofujun.automapper.beans.UserDo;
import org.junit.Test;

import java.lang.reflect.Field;

public class ASMTest {
    @Test
    public void testAsm() throws NoSuchFieldException {
        UserDo userDo = new UserDo();
        Field field=UserDo.class.getDeclaredField("nId");
        field.setAccessible(true);
        FieldAccess fieldAccess = FieldAccess.get(UserDo.class);
        fieldAccess.set(userDo,"nId","111");
        String id = userDo.getId();
    }
}
