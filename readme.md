## 1. 概述
在我们日常开发过程中，会存在大量需要对领域对象转换为dto的场景，程序员往往使用getter和setter来对对象进行赋值，当对象的字段过多，手写赋值过程将变得非常麻烦，遗漏字段的事情也常常发生。为发减轻程序员的工作，automapper可以自动完成对象之间的互相转换工作。

## 2. 快速应用AutoMapper

```
// 省略对象定义和赋值部分...

IMapper mapper = new AutoMapper();
UserDo userDo = mapper.map(dto, UserDo.class);
```
> 直接使用automapper进行对象转换时，automapper将使用默认的转换规则进行转换，具体转换规则见 [3.2 默认转换规则](#32-默认转换规则)


## 3. 转换演示

### 3.1 准备工作

为了模拟各场景下的对象转换过程，我们需要先准备两个对象。

**UserDto**

```
package com.zhaofujun.automapper;

public class UserDto {
    private String id;
    private String  sex;
    private int age;
    private String realName;
    private String contactAddress;
    private String contactTel;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
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

**UserDo**


```
package com.zhaofujun.automapper;

public class UserDo {
    enum  Sex{
        female,male
    }
    private String id;
    private Sex sex;
    private int age;
    private String name;
    private Contact contact;

    public String getId() {
        return id;
    }


    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
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
**Contact**

``` 
public class Contact {
    private String address;
    private int tel;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getTel() {
        return tel;
    }

    public void setTel(int tel) {
        this.tel = tel;
    }
}
```


### 3.2 默认转换规则

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

### 3.3 映射目标对象中字段没有setter的方式

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



### 3.4 源对象与目标对象字段名不同时的映射方式

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



### 3.5 排除不需要映射的字段

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

### 3.6 映射复杂对象到简单对象

当源对象中的字段是一个子对象时，可以将子对象下的字段映射到目标对象的字段中。
只需自动配置field映射关系时，指定字段名时用“.”来指向子对象下的字段信息。

**演示代码**


```
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
```

### 3.7 简单对象映射对复杂对象

当目标对象中的字段是一个子对象时，可以将源对象的字段映射到目标对象子对象下的字段。
只需自动配置field映射关系时，指定字段名时用“.”来指向子对象下的字段信息。

**演示代码**


```
    @Test
    public void SimpleToComplex() {
        IMapper mapper = new AutoMapper();
        mapper.mapping(UserDto.class, UserDo.class)
                .field("contact.address", "contactAddress")
                .field("contact.tel", "contactTel")
                .field("contactTel", "contact.tel")
                .field("contactAddress", "contact.address");


        UserDto userDto = new UserDto();
        userDto.setContactTel("123");
        userDto.setContactAddress("address");

        UserDo userDo = mapper.map(userDto, UserDo.class);

        Assert.assertEquals(userDo.getContact().getAddress(), userDto.getContactAddress());


    }
    
```


### 3.8 允许添加自定义转换器，用于解决任何类型之间的自定义转换

当两个类型的转换比较通用，可以自定义转换器，

**IMapper定义的配置方式**

```
    IMapper registerConverter(Converter converter);
```

**演示代码**

``` 
// 创建字符串与性别枚举的转换器
class SexAndStringConverter extends Converter<String, UserDo.Sex> {

    @Override
    protected Class<String> getSourceClass() {
        return String.class;
    }

    @Override
    protected Class<UserDo.Sex> getTargetClass() {
        return UserDo.Sex.class;
    }

    @Override
    public UserDo.Sex toTarget(String source) {
        if (source.equals("男"))
            return UserDo.Sex.male;
        else
            return UserDo.Sex.female;
    }

    @Override
    public String toSource(UserDo.Sex target) {
        if (target.equals(UserDo.Sex.male))
            return "男";
        else
            return "女";
    }
}

```

枚举转换成字符串

```
    @Test
    public void EnumToString() {
        IMapper mapper = new AutoMapper();
        mapper.registerConverter(new SexAndStringConverter());
        UserDo userDo = new UserDo();
        userDo.setSex(UserDo.Sex.female);

        UserDto userDto = mapper.map(userDo, UserDto.class);

        Assert.assertEquals(userDto.getSex(), "女");

    }
```

字符串转枚举


```
    @Test
    public void StringToEnum() {
        IMapper mapper = new AutoMapper();
        mapper.registerConverter(new SexAndStringConverter());
        UserDto userDto = new UserDto();
        userDto.setSex("女");

        UserDo userDo = mapper.map(userDto, UserDo.class);

        Assert.assertEquals(userDo.getSex(), UserDo.Sex.female);

    }
```
