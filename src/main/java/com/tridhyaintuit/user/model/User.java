package com.tridhyaintuit.user.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.*;
import java.util.Date;

@Getter
@Setter
@Document(collection = "User")
@AllArgsConstructor
@NoArgsConstructor
@ToString
@SuppressWarnings("ALL")
public class User {

    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;

    @NotNull(message = "Name can not be Null!")
    private String name;

    @NotNull(message = "Birth Date can not be Null!")
//    @DateTimeFormat(pattern="dd-MM-yyyy HH:mm:ss",timezone="IST")
    @JsonFormat(pattern="dd-MM-yyyy HH:mm:ss",timezone = "IST")
    @Past(message = "BirthDate should be in the past!")
    private Date birthDate;

    @Email
    @NotBlank(message = "email Must not be null")
    private String email;

    @NotNull(message = "Mobile number contain only 10 digits")
    @Pattern(regexp="(^$|[0-9]{10})")
    private String phone;

    @NotNull(message = "Password must not be null")
    @Size(min=6, max=16, message = "user.password_must_have_6-16_characters")
//    @Pattern(regexp = "^$(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#_$%^&-+=()])(?=\\S+$).{6,16}$")
    protected String passWord;

    private Age age;

    @NotNull(message = "Address must not be Null!")
    private String address;

    @NotNull(message = "PinCode must not be null!")
    @Pattern(regexp = "^[1-9]{1}[0-9]{2}\\s{0,1}[0-9]{3}$")
    private String pinCode;

    @JsonFormat(pattern="dd-MM-yyyy HH:mm:ss",timezone = "IST")
    private Date created;

    @JsonFormat(pattern="dd-MM-yyyy HH:mm:ss",timezone = "Kolkata")
    private Date lastUpdated = null;

    private boolean deleted=false;

    private boolean active=true;

//    private boolean login = false;

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof User)) return false;
//        User user = (User) o;
//        return Objects.equals(email, user.email) &&
//                Objects.equals(passWord, user.passWord);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(id, email, passWord,login);
//    }

}
