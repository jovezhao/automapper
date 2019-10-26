## 概述
在我们日常开发过程中，会存在大量需要对领域对象转换为dto的场景，程序员往往使用getter和setter来对对象进行赋值，当对象的字段过多，手写赋值过程将变得非常麻烦，遗漏字段的事情也常常发生。为发减轻程序员的工作，automapper可以自动完成对象之间的互相转换工作。

## 快速应用AutoMapper

```
// 省略对象定义和赋值部分...

IMapper mapper = new AutoMapper();
UserDo userDo = mapper.map(dto, UserDo.class);
```
> 直接使用automapper进行对象转换时，automapper将使用默认的转换规则进行转换，具体转换规则见 [默认转换规则](#defaultRule)


## 转换演示

### 准备工作

为了模拟各场景下的对象转换过程，我们需要先准备两个对象。

**UserDto**

```
package com.zhaofujun.automapper;

public class UserDo {
    private String id;
    private boolean sex;
    private int age;
    private String name;
    private Contact contact;

    public String getId() {
        return id;
    }


    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }
}


```

**UserDo**


```
package com.zhaofujun.automapper;

public class UserDto {
    private String id;
    private boolean  sex;
    private int age;
    private String name;
    private String contactAddress;
    private String contactTel;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactAddress() {
        return contactAddress;
    }

    public void setContactAddress(String contactAddress) {
        this.contactAddress = contactAddress;
    }

    public String getContactTel() {
        return contactTel;
    }

    public void setContactTel(String contactTel) {
        this.contactTel = contactTel;
    }
}

```


### 默认转换规则

默认转换规则只转换目标对象中字段带有setter并且在源对象中有同名字段的所有字段信息。


```
    @Test
    public  void defaultMapper() {
        UserDo userDo=new UserDo();
        userDo.setAge(100);

        IMapper mapper=new AutoMapper();
        UserDto userDto = mapper.map(userDo, UserDto.class);
        Assert.assertEquals(userDo.getAge(),userDto.getAge());
    }
```

### 映射目标对象中字段没有setter的方式

当目标对象中的字段名与源对象中的字段名相同，但目标对象没有setter方法时，可以在配置两个对象的映射关系时设置允许私有字段为true

**IMapper接口定义的配置方式**
```
    //自动匹配所有带setter，并且同名的字段
    ClassMappingBuilder mapping(Class sourceClass,Class targetClass);
    //自动匹配所有同名字段
    ClassMappingBuilder mapping(Class sourceClass,Class targetClass,boolean allowPrivate);
```

**演示代码**
```
   @Test
    public void noSetterMap(){
        UserDto userDto=new UserDto();
        userDto.setId("idid");

        IMapper mapper=new AutoMapper();
        //允许映射包括没有setter的字段
        mapper.mapping(UserDto.class,UserDo.class,true);
        UserDo userDo = mapper.map(userDto, UserDo.class);
        Assert.assertEquals(userDo.getId(),userDto.getId());
    }
```



### 源对象与目标对象字段名不同时的映射方式

当源对象与目标对象中有不同名字段时，需要为源对象与目标对象配置字段映射关系。

**ClassMappingBuilder定义的配置方式**

```
    ClassMappingBuilder field( String sourceFieldName,String targetFieldName);

```
**演示代码**


```
    @Test
    public void differentFieldMap(){
        UserDto userDto=new UserDto();
        userDto.setRealName("name");

        IMapper mapper=new AutoMapper();
        //允许映射包括没有setter的字段
        mapper.mapping(UserDto.class,UserDo.class,true)
                .field("realName","name");

        UserDo userDo = mapper.map(userDto, UserDo.class);
        Assert.assertEquals(userDo.getName(),userDto.getRealName());
    }
```



### 排除不需要映射的字段

当目标对象中有的字段不需要映射出去时，可以手动排除指定的字段。

**ClassMappingBuilder定义的配置方式**

```
    ClassMappingBuilder excludes(String... targetFieldNames);

```

**演示代码**

```
    @Test
    public void excludeField(){
        UserDto userDto=new UserDto();
        userDto.setRealName("name");
        userDto.setAge(10);

        IMapper mapper=new AutoMapper();
        //允许映射包括没有setter的字段
        mapper.mapping(UserDto.class,UserDo.class,true)
                .field("realName","name")
                .excludes("age");

        UserDo userDo = mapper.map(userDto, UserDo.class);
        Assert.assertEquals(userDo.getAge(),0);
    }
```

### 映射复杂对象
- 目标对象字段包括复杂对象时，复杂对象的创建方式使用默认构造函数
- 源对象字段包括复杂对象时，要求源对象字段有getter方法

### 允许添加自定义转换器，源字段与目标字段类型不致时可用
转换器支持单向转换，也支持双向转换
